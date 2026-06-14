package org.hswebframework.web.authorization.basic.handler.access;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.hswebframework.ezorm.core.param.Param;
import org.hswebframework.ezorm.core.param.QueryParam;
import org.hswebframework.web.api.crud.entity.Entity;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.Dimension;
import org.hswebframework.web.authorization.access.DataAccessConfig;
import org.hswebframework.web.authorization.access.DataAccessHandler;
import org.hswebframework.web.authorization.annotation.DimensionDataAccess;
import org.hswebframework.web.authorization.define.AuthorizingContext;
import org.hswebframework.web.authorization.define.Phased;
import org.hswebframework.web.authorization.exception.AccessDenyException;
import org.hswebframework.web.authorization.simple.DimensionDataAccessConfig;
import org.hswebframework.web.bean.FastBeanCopier;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DimensionDataAccessHandler implements DataAccessHandler {
   private static final Logger log = LoggerFactory.getLogger(DimensionDataAccessHandler.class);
   private Map<Method, Map<String, MappingInfo>> cache = new ConcurrentHashMap();
   private Set<Class<? extends Annotation>> ann = new HashSet(Arrays.asList(DimensionDataAccess.class, DimensionDataAccess.Mapping.class));

   public boolean isSupport(DataAccessConfig access) {
      return access instanceof DimensionDataAccessConfig;
   }

   public boolean handle(DataAccessConfig access, AuthorizingContext context) {
      DimensionDataAccessConfig config = (DimensionDataAccessConfig)access;
      DataAccessHandlerContext requestContext = DataAccessHandlerContext.of(context, config.getScopeType());
      if (!this.checkSupported(config, requestContext)) {
         return false;
      } else {
         switch (access.getAction()) {
            case "query":
            case "get":
               return this.doHandleQuery(config, requestContext);
            case "add":
            case "save":
            case "update":
               return this.doHandleUpdate(config, requestContext);
            case "delete":
               return this.doHandleDelete(config, requestContext);
            default:
               if (log.isDebugEnabled()) {
                  log.debug("data access [{}] not support for {}", config.getType().getId(), access.getAction());
               }

               return true;
         }
      }
   }

   protected String getProperty(DimensionDataAccessConfig cfg, DataAccessHandlerContext ct) {
      try {
         return (String)Optional.ofNullable(this.getMappingInfo(ct).get(cfg.getScopeType())).map(MappingInfo::getProperty).orElseGet(() -> {
            log.warn("{} not supported dimension data access", ct.getParamContext().getMethod());
            return null;
         });
      } catch (Throwable var4) {
         throw var4;
      }
   }

   protected boolean checkSupported(DimensionDataAccessConfig cfg, DataAccessHandlerContext ctx) {
      Authentication authentication = ctx.getAuthentication();
      if (CollectionUtils.isEmpty(ctx.getDimensions())) {
         log.warn("user:[{}] dimension not setup", authentication.getUser().getId());
         return false;
      } else if (!this.getMappingInfo(ctx).containsKey(cfg.getScopeType())) {
         log.warn("{} not supported dimension data access.see annotation: @DimensionDataAccess", ctx.getParamContext().getMethod());
         return false;
      } else {
         return true;
      }
   }

   protected boolean doHandleDelete(DimensionDataAccessConfig cfg, DataAccessHandlerContext context) {
      return this.doHandleUpdate(cfg, context);
   }

   protected Object handleById(DimensionDataAccessConfig config, DataAccessHandlerContext context, MappingInfo mappingInfo, Object id) {
      if (!(id instanceof Param) && !(id instanceof Entity)) {
         List<Dimension> dimensions = context.getDimensions();
         Set<Object> scope = CollectionUtils.isNotEmpty(config.getScope()) ? config.getScope() : (Set)dimensions.stream().map(Dimension::getId).collect(Collectors.toSet());
         Function<Collection<Object>, Mono<Void>> reactiveCheck = (obj) -> {
            return context.getRepository().findById(obj).doOnNext((r) -> {
               Object val = ((HashMap)FastBeanCopier.copy(r, new HashMap(), FastBeanCopier.include(new String[]{mappingInfo.getProperty()}))).get(mappingInfo.getProperty());
               if (!StringUtils.isEmpty(val) && !scope.contains(val)) {
                  throw new AccessDenyException();
               }
            }).then();
         };
         if (id instanceof Publisher) {
            if (id instanceof Mono) {
               return ((Mono)id).flatMap((r) -> {
                  if (r instanceof Param) {
                     this.applyQueryParam(config, context, r);
                     return Mono.just(r);
                  } else {
                     return reactiveCheck.apply(r instanceof Collection ? (Collection)r : Collections.singleton(r));
                  }
               }).then((Mono)id);
            }

            if (id instanceof Flux) {
               return ((Flux)id).filter((v) -> {
                  if (v instanceof Param) {
                     this.applyQueryParam(config, context, v);
                     return false;
                  } else {
                     return true;
                  }
               }).collectList().flatMap(reactiveCheck).thenMany((Flux)id);
            }
         }

         Collection<Object> idVal = id instanceof Collection ? (Collection)id : Collections.singleton(id);
         Object result = context.getParamContext().getInvokeResult();
         if (result instanceof Mono) {
            context.getParamContext().setInvokeResult(((Mono)reactiveCheck.apply(idVal)).then((Mono)result));
         } else if (result instanceof Flux) {
            context.getParamContext().setInvokeResult(((Mono)reactiveCheck.apply(idVal)).thenMany((Flux)result));
         } else {
            log.warn("unsupported handle data access by id :{}", context.getParamContext().getMethod());
         }

         return id;
      } else {
         this.applyQueryParam(config, context, id);
         return id;
      }
   }

   protected boolean doHandleUpdate(DimensionDataAccessConfig cfg, DataAccessHandlerContext context) {
      MappingInfo info = (MappingInfo)this.getMappingInfo(context).get(cfg.getScopeType());
      if (info != null) {
         if (info.idParamIndex != -1) {
            Object param = context.getParamContext().getArguments()[info.idParamIndex];
            context.getParamContext().getArguments()[info.idParamIndex] = this.handleById(cfg, context, info, param);
            return true;
         } else {
            boolean reactive = context.getParamContext().handleReactiveArguments((publisher) -> {
               if (publisher instanceof Mono) {
                  return Mono.from(publisher).flatMap((payload) -> {
                     return this.applyReactiveUpdatePayload(cfg, info, Collections.singleton(payload), context).thenReturn(payload);
                  });
               } else {
                  return (Publisher)(publisher instanceof Flux ? Flux.from(publisher).collectList().flatMapMany((list) -> {
                     return this.applyReactiveUpdatePayload(cfg, info, list, context).flatMapIterable((v) -> {
                        return list;
                     });
                  }) : publisher);
               }
            });
            if (!reactive) {
               Stream var10003 = Arrays.stream(context.getParamContext().getArguments()).flatMap((obj) -> {
                  return obj instanceof Collection ? ((Collection)obj).stream() : Stream.of(obj);
               });
               Entity.class.getClass();
               this.applyUpdatePayload(cfg, info, (Collection)var10003.filter(Entity.class::isInstance).collect(Collectors.toSet()), context);
               return true;
            } else {
               return true;
            }
         }
      } else {
         return true;
      }
   }

   protected void applyUpdatePayload(DimensionDataAccessConfig config, MappingInfo mappingInfo, Collection<?> payloads, DataAccessHandlerContext context) {
      List<Dimension> dimensions = context.getDimensions();
      Set<Object> scope = CollectionUtils.isNotEmpty(config.getScope()) ? config.getScope() : (Set)dimensions.stream().map(Dimension::getId).collect(Collectors.toSet());
      Iterator var7 = payloads.iterator();

      while(var7.hasNext()) {
         Object payload = var7.next();
         if (payload instanceof Entity) {
            if (payload instanceof Param) {
               this.applyQueryParam(config, context, (Param)payload);
            } else {
               String property = mappingInfo.getProperty();
               Map<String, Object> map = (Map)FastBeanCopier.copy(payload, new HashMap(), FastBeanCopier.include(new String[]{property}));
               Object value = map.get(property);
               if (StringUtils.isEmpty(value)) {
                  if (dimensions.size() == 1) {
                     map.put(property, ((Dimension)dimensions.get(0)).getId());
                     FastBeanCopier.copy(map, payload, new String[]{property});
                  }
               } else if (CollectionUtils.isNotEmpty(scope) && !scope.contains(value)) {
                  throw new AccessDenyException();
               }
            }
         }
      }

   }

   protected Mono<Void> applyReactiveUpdatePayload(DimensionDataAccessConfig config, MappingInfo info, Collection<?> payloads, DataAccessHandlerContext context) {
      return Mono.fromRunnable(() -> {
         this.applyUpdatePayload(config, info, payloads, context);
      });
   }

   protected boolean hasAccessByProperty(Set<Object> scope, String property, Object payload) {
      Map<String, Object> values = (Map)FastBeanCopier.copy(payload, new HashMap(), FastBeanCopier.include(new String[]{property}));
      Object val = values.get(property);
      return val == null || scope.contains(val);
   }

   protected boolean doHandleQuery(DimensionDataAccessConfig cfg, DataAccessHandlerContext context) {
      MappingInfo mappingInfo = (MappingInfo)this.getMappingInfo(context).get(cfg.getScopeType());
      Object result;
      if (context.getDefinition().getResources().getPhased() == Phased.after) {
         result = context.getParamContext().getInvokeResult();
         Set<Object> scope = CollectionUtils.isNotEmpty(cfg.getScope()) ? cfg.getScope() : (Set)context.getDimensions().stream().map(Dimension::getId).collect(Collectors.toSet());
         String property = mappingInfo.getProperty();
         if (result instanceof Mono) {
            context.getParamContext().setInvokeResult(((Mono)result).filter((data) -> {
               return this.hasAccessByProperty(scope, property, data);
            }));
            return true;
         } else if (result instanceof Flux) {
            context.getParamContext().setInvokeResult(((Flux)result).filter((data) -> {
               return this.hasAccessByProperty(scope, property, data);
            }));
            return true;
         } else {
            return this.hasAccessByProperty(scope, property, result);
         }
      } else if (mappingInfo.getIdParamIndex() >= 0) {
         result = context.getParamContext().getArguments()[mappingInfo.idParamIndex];
         context.getParamContext().getArguments()[mappingInfo.idParamIndex] = this.handleById(cfg, context, mappingInfo, result);
         return true;
      } else {
         boolean reactive = context.getParamContext().handleReactiveArguments((publisher) -> {
            return (Publisher)(publisher instanceof Mono ? Mono.from(publisher).flatMap((param) -> {
               return this.applyReactiveQueryParam(cfg, context, param).thenReturn(param);
            }) : publisher);
         });
         if (!reactive) {
            Object[] args = context.getParamContext().getArguments();
            this.applyQueryParam(cfg, context, args);
         }

         return true;
      }
   }

   protected String getTermType(DimensionDataAccessConfig cfg) {
      return "in";
   }

   protected void applyQueryParam(DimensionDataAccessConfig cfg, DataAccessHandlerContext requestContext, Param param) {
      Set<Object> scope = CollectionUtils.isNotEmpty(cfg.getScope()) ? cfg.getScope() : (Set)requestContext.getDimensions().stream().map(Dimension::getId).collect(Collectors.toSet());
      QueryParamEntity entity = new QueryParamEntity();
      entity.setTerms(new ArrayList(param.getTerms()));
      entity.toNestQuery((query) -> {
         query.where(this.getProperty(cfg, requestContext), this.getTermType(cfg), scope);
      });
      param.setTerms(entity.getTerms());
   }

   protected void applyQueryParam(DimensionDataAccessConfig cfg, DataAccessHandlerContext requestContext, Object... params) {
      Object[] var4 = params;
      int var5 = params.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object param = var4[var6];
         if (param instanceof QueryParam) {
            this.applyQueryParam(cfg, requestContext, (Param)((QueryParam)param));
         }
      }

   }

   protected Mono<Void> applyReactiveQueryParam(DimensionDataAccessConfig cfg, DataAccessHandlerContext requestContext, Object... param) {
      return Mono.fromRunnable(() -> {
         this.applyQueryParam(cfg, requestContext, param);
      });
   }

   public Map<String, MappingInfo> getMappingInfo(DataAccessHandlerContext context) {
      return this.getMappingInfo(ClassUtils.getUserClass(context.getParamContext().getTarget()), context.getParamContext().getMethod());
   }

   private Map<String, MappingInfo> getMappingInfo(Class target, Method method) {
      return (Map)this.cache.computeIfAbsent(method, (m) -> {
         Set<Annotation> methodAnnotation = AnnotatedElementUtils.findAllMergedAnnotations(method, this.ann);
         Set<Annotation> classAnnotation = AnnotatedElementUtils.findAllMergedAnnotations(target, this.ann);
         List<Annotation> all = new ArrayList(classAnnotation);
         all.addAll(methodAnnotation);
         if (CollectionUtils.isEmpty(all)) {
            return Collections.emptyMap();
         } else {
            Map<String, MappingInfo> mappingInfoMap = new HashMap();
            Iterator var8 = all.iterator();

            while(var8.hasNext()) {
               Annotation annotation = (Annotation)var8.next();
               if (annotation instanceof DimensionDataAccess) {
                  DimensionDataAccess.Mapping[] var10 = ((DimensionDataAccess)annotation).mapping();
                  int var11 = var10.length;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     DimensionDataAccess.Mapping mapping = var10[var12];
                     mappingInfoMap.put(mapping.dimensionType(), DimensionDataAccessHandler.MappingInfo.of(mapping));
                  }
               }

               if (annotation instanceof DimensionDataAccess.Mapping) {
                  mappingInfoMap.put(((DimensionDataAccess.Mapping)annotation).dimensionType(), DimensionDataAccessHandler.MappingInfo.of((DimensionDataAccess.Mapping)annotation));
               }
            }

            return mappingInfoMap;
         }
      });
   }

   static class MappingInfo {
      String dimension;
      String property;
      int idParamIndex;

      static MappingInfo of(DimensionDataAccess.Mapping mapping) {
         return new MappingInfo(mapping.dimensionType(), mapping.property(), mapping.idParamIndex());
      }

      public String getDimension() {
         return this.dimension;
      }

      public String getProperty() {
         return this.property;
      }

      public int getIdParamIndex() {
         return this.idParamIndex;
      }

      public void setDimension(String dimension) {
         this.dimension = dimension;
      }

      public void setProperty(String property) {
         this.property = property;
      }

      public void setIdParamIndex(int idParamIndex) {
         this.idParamIndex = idParamIndex;
      }

      public MappingInfo(String dimension, String property, int idParamIndex) {
         this.dimension = dimension;
         this.property = property;
         this.idParamIndex = idParamIndex;
      }
   }
}
