package org.hswebframework.web.authorization.basic.define;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.DataAccess;
import org.hswebframework.web.authorization.annotation.DataAccessType;
import org.hswebframework.web.authorization.annotation.Dimension;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.authorization.annotation.ResourceAction;
import org.hswebframework.web.authorization.define.AopAuthorizeDefinition;
import org.hswebframework.web.authorization.define.ResourceActionDefinition;
import org.hswebframework.web.authorization.define.ResourceDefinition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

public class AopAuthorizeDefinitionParser {
   private static final Set<Class<? extends Annotation>> types = new HashSet(Arrays.asList(Authorize.class, DataAccess.class, Dimension.class, Resource.class, ResourceAction.class, DataAccessType.class));
   private final Set<Annotation> methodAnnotation;
   private final Set<Annotation> classAnnotation;
   private final Map<Class<? extends Annotation>, List<Annotation>> classAnnotationGroup;
   private final Map<Class<? extends Annotation>, List<Annotation>> methodAnnotationGroup;
   private final DefaultBasicAuthorizeDefinition definition = new DefaultBasicAuthorizeDefinition();

   AopAuthorizeDefinitionParser(Class<?> targetClass, Method method) {
      this.definition.setTargetClass(targetClass);
      this.definition.setTargetMethod(method);
      this.methodAnnotation = AnnotatedElementUtils.findAllMergedAnnotations(method, types);
      this.classAnnotation = AnnotatedElementUtils.findAllMergedAnnotations(targetClass, types);
      this.classAnnotationGroup = (Map)this.classAnnotation.stream().collect(Collectors.groupingBy(Annotation::annotationType));
      this.methodAnnotationGroup = (Map)this.methodAnnotation.stream().collect(Collectors.groupingBy(Annotation::annotationType));
   }

   private void initClassAnnotation() {
      Iterator var1 = this.classAnnotation.iterator();

      while(var1.hasNext()) {
         Annotation annotation = (Annotation)var1.next();
         if (annotation instanceof Authorize) {
            this.definition.putAnnotation((Authorize)annotation);
         }

         if (annotation instanceof Resource) {
            this.definition.putAnnotation((Resource)annotation);
         }
      }

   }

   private void initMethodAnnotation() {
      Iterator var1 = this.methodAnnotation.iterator();

      while(var1.hasNext()) {
         Annotation annotation = (Annotation)var1.next();
         if (annotation instanceof Authorize) {
            this.definition.putAnnotation((Authorize)annotation);
         }

         if (annotation instanceof Resource) {
            this.definition.putAnnotation((Resource)annotation);
         }

         if (annotation instanceof Dimension) {
            this.definition.putAnnotation((Dimension)annotation);
         }
      }

   }

   private void initClassDataAccessAnnotation() {
      Iterator var1 = this.classAnnotation.iterator();

      while(true) {
         Annotation annotation;
         do {
            if (!var1.hasNext()) {
               return;
            }

            annotation = (Annotation)var1.next();
         } while(!(annotation instanceof DataAccessType) && !(annotation instanceof DataAccess));

         Iterator var3 = this.definition.getResources().getResources().iterator();

         while(var3.hasNext()) {
            ResourceDefinition resource = (ResourceDefinition)var3.next();
            Iterator var5 = resource.getActions().iterator();

            while(var5.hasNext()) {
               ResourceActionDefinition action = (ResourceActionDefinition)var5.next();
               if (annotation instanceof DataAccessType) {
                  this.definition.putAnnotation(action, (DataAccessType)annotation);
               } else {
                  this.definition.putAnnotation(action, (DataAccess)annotation);
               }
            }
         }
      }
   }

   private void initMethodDataAccessAnnotation() {
      Iterator var1 = this.methodAnnotation.iterator();

      while(var1.hasNext()) {
         Annotation annotation = (Annotation)var1.next();
         if (annotation instanceof ResourceAction) {
            this.getAnnotationByType(Resource.class).map((res) -> {
               return (ResourceDefinition)this.definition.getResources().getResource(res.id()).orElse((Object)null);
            }).filter(Objects::nonNull).forEach((res) -> {
               ResourceAction ra = (ResourceAction)annotation;
               ResourceActionDefinition action = this.definition.putAnnotation(res, ra);
               this.getAnnotationByType(DataAccessType.class).findFirst().ifPresent((dat) -> {
                  this.definition.putAnnotation(action, dat);
               });
            });
         }

         Optional<ResourceActionDefinition> actionDefinition = this.getAnnotationByType(Resource.class).map((res) -> {
            return (ResourceDefinition)this.definition.getResources().getResource(res.id()).orElse((Object)null);
         }).filter(Objects::nonNull).flatMap((res) -> {
            return this.getAnnotationByType(ResourceAction.class).map((ra) -> {
               return (ResourceActionDefinition)res.getAction(ra.id()).orElse((Object)null);
            });
         }).filter(Objects::nonNull).findFirst();
         if (annotation instanceof DataAccessType) {
            actionDefinition.ifPresent((ra) -> {
               this.definition.putAnnotation(ra, (DataAccessType)annotation);
            });
         }

         if (annotation instanceof DataAccess) {
            actionDefinition.ifPresent((ra) -> {
               this.definition.putAnnotation(ra, (DataAccess)annotation);
               this.getAnnotationByType(DataAccessType.class).findFirst().ifPresent((dat) -> {
                  this.definition.putAnnotation(ra, dat);
               });
            });
         }
      }

   }

   AopAuthorizeDefinition parse() {
      if (CollectionUtils.isEmpty(this.classAnnotation) && CollectionUtils.isEmpty(this.methodAnnotation)) {
         return EmptyAuthorizeDefinition.instance;
      } else {
         this.initClassAnnotation();
         this.initClassDataAccessAnnotation();
         this.initMethodAnnotation();
         this.initMethodDataAccessAnnotation();
         return this.definition;
      }
   }

   private <T extends Annotation> Stream<T> getAnnotationByType(Class<T> type) {
      Stream var10000 = (Stream)Optional.ofNullable(this.methodAnnotationGroup.getOrDefault(type, this.classAnnotationGroup.get(type))).map(Collection::stream).orElseGet(Stream::empty);
      type.getClass();
      return var10000.map(type::cast);
   }
}
