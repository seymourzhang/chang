package org.hswebframework.web.authorization.basic.handler.access;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hswebframework.ezorm.core.param.QueryParam;
import org.hswebframework.web.authorization.access.DataAccessConfig;
import org.hswebframework.web.authorization.access.DataAccessHandler;
import org.hswebframework.web.authorization.access.FieldFilterDataAccessConfig;
import org.hswebframework.web.authorization.define.AuthorizingContext;
import org.hswebframework.web.authorization.define.Phased;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FieldFilterDataAccessHandler implements DataAccessHandler {
   private Logger logger = LoggerFactory.getLogger(this.getClass());

   public boolean isSupport(DataAccessConfig access) {
      return access instanceof FieldFilterDataAccessConfig;
   }

   public boolean handle(DataAccessConfig access, AuthorizingContext context) {
      FieldFilterDataAccessConfig filterDataAccessConfig = (FieldFilterDataAccessConfig)access;
      switch (access.getAction()) {
         case "query":
         case "get":
            return this.doQueryAccess(filterDataAccessConfig, context);
         case "add":
         case "save":
         case "update":
            return this.doUpdateAccess(filterDataAccessConfig, context);
         default:
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("field filter not support for {}", access.getAction());
            }

            return true;
      }
   }

   protected void applyUpdateParam(FieldFilterDataAccessConfig config, Object... parameter) {
      Object[] var3 = parameter;
      int var4 = parameter.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object data = var3[var5];
         Iterator var7 = config.getFields().iterator();

         while(var7.hasNext()) {
            String field = (String)var7.next();

            try {
               BeanUtilsBean.getInstance().getPropertyUtils().setProperty(data, field, (Object)null);
            } catch (Exception var10) {
               this.logger.warn("can't set {} null", field, var10);
            }
         }
      }

   }

   protected boolean doUpdateAccess(FieldFilterDataAccessConfig accesses, AuthorizingContext params) {
      boolean reactive = params.getParamContext().handleReactiveArguments((publisher) -> {
         if (publisher instanceof Mono) {
            return Mono.from(publisher).doOnNext((data) -> {
               this.applyUpdateParam(accesses, data);
            });
         } else {
            return (Publisher)(publisher instanceof Flux ? Flux.from(publisher).doOnNext((data) -> {
               this.applyUpdateParam(accesses, data);
            }) : publisher);
         }
      });
      if (reactive) {
         return true;
      } else {
         this.applyUpdateParam(accesses, params.getParamContext().getArguments());
         return true;
      }
   }

   protected void applyQueryParam(FieldFilterDataAccessConfig config, Object param) {
      if (param instanceof QueryParam) {
         Set<String> denyFields = config.getFields();
         ((QueryParam)param).excludes((String[])denyFields.toArray(new String[0]));
      } else {
         Object r = InvokeResultUtils.convertRealResult(param);
         if (r instanceof Collection) {
            ((Collection)r).forEach((o) -> {
               this.setObjectPropertyNull(o, config.getFields());
            });
         } else {
            this.setObjectPropertyNull(r, config.getFields());
         }

      }
   }

   protected boolean doQueryAccess(FieldFilterDataAccessConfig access, AuthorizingContext context) {
      if (context.getDefinition().getResources().getPhased() == Phased.before) {
         boolean reactive = context.getParamContext().handleReactiveArguments((publisher) -> {
            return (Publisher)(publisher instanceof Mono ? Mono.from(publisher).doOnNext((param) -> {
               this.applyQueryParam(access, param);
            }) : publisher);
         });
         if (reactive) {
            return true;
         }

         Object[] var4 = context.getParamContext().getArguments();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object argument = var4[var6];
            this.applyQueryParam(access, argument);
         }
      } else {
         if (context.getParamContext().getInvokeResult() instanceof Publisher) {
            context.getParamContext().setInvokeResult(Flux.from((Publisher)context.getParamContext().getInvokeResult()).doOnNext((result) -> {
               this.applyQueryParam(access, result);
            }));
            return true;
         }

         this.applyQueryParam(access, context.getParamContext().getInvokeResult());
      }

      return true;
   }

   protected void setObjectPropertyNull(Object obj, Set<String> fields) {
      if (null != obj) {
         Iterator var3 = fields.iterator();

         while(var3.hasNext()) {
            String field = (String)var3.next();

            try {
               BeanUtilsBean.getInstance().getPropertyUtils().setProperty(obj, field, (Object)null);
            } catch (Exception var6) {
            }
         }

      }
   }
}
