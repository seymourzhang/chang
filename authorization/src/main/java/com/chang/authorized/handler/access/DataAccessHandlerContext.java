package org.hswebframework.web.authorization.basic.handler.access;

import java.util.List;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.hswebframework.utils.ClassUtils;
import org.hswebframework.web.aop.MethodInterceptorContext;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.Dimension;
import org.hswebframework.web.authorization.define.AuthorizeDefinition;
import org.hswebframework.web.authorization.define.AuthorizingContext;
import org.hswebframework.web.crud.web.reactive.ReactiveDeleteController;
import org.hswebframework.web.crud.web.reactive.ReactiveQueryController;
import org.hswebframework.web.crud.web.reactive.ReactiveSaveController;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceDeleteController;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceQueryController;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceSaveController;

public class DataAccessHandlerContext {
   private Class<?> entityType;
   private ReactiveRepository<?, Object> repository;
   private Authentication authentication;
   private List<Dimension> dimensions;
   private MethodInterceptorContext paramContext;
   private AuthorizeDefinition definition;

   public static DataAccessHandlerContext of(AuthorizingContext context, String type) {
      DataAccessHandlerContext requestContext = new DataAccessHandlerContext();
      Authentication authentication = context.getAuthentication();
      requestContext.setDimensions(authentication.getDimensions(type));
      requestContext.setAuthentication(context.getAuthentication());
      requestContext.setParamContext(context.getParamContext());
      requestContext.setDefinition(context.getDefinition());
      Object target = context.getParamContext().getTarget();
      Class entityType = ClassUtils.getGenericType(org.springframework.util.ClassUtils.getUserClass(target));
      if (entityType != Object.class) {
         requestContext.setEntityType(entityType);
      }

      if (target instanceof ReactiveQueryController) {
         requestContext.setRepository(((ReactiveQueryController)target).getRepository());
      } else if (target instanceof ReactiveSaveController) {
         requestContext.setRepository(((ReactiveSaveController)target).getRepository());
      } else if (target instanceof ReactiveDeleteController) {
         requestContext.setRepository(((ReactiveDeleteController)target).getRepository());
      } else if (target instanceof ReactiveServiceQueryController) {
         requestContext.setRepository(((ReactiveServiceQueryController)target).getService().getRepository());
      } else if (target instanceof ReactiveServiceSaveController) {
         requestContext.setRepository(((ReactiveServiceSaveController)target).getService().getRepository());
      } else if (target instanceof ReactiveServiceDeleteController) {
         requestContext.setRepository(((ReactiveServiceDeleteController)target).getService().getRepository());
      }

      return requestContext;
   }

   public Class<?> getEntityType() {
      return this.entityType;
   }

   public ReactiveRepository<?, Object> getRepository() {
      return this.repository;
   }

   public Authentication getAuthentication() {
      return this.authentication;
   }

   public List<Dimension> getDimensions() {
      return this.dimensions;
   }

   public MethodInterceptorContext getParamContext() {
      return this.paramContext;
   }

   public AuthorizeDefinition getDefinition() {
      return this.definition;
   }

   public void setEntityType(Class<?> entityType) {
      this.entityType = entityType;
   }

   public void setRepository(ReactiveRepository<?, Object> repository) {
      this.repository = repository;
   }

   public void setAuthentication(Authentication authentication) {
      this.authentication = authentication;
   }

   public void setDimensions(List<Dimension> dimensions) {
      this.dimensions = dimensions;
   }

   public void setParamContext(MethodInterceptorContext paramContext) {
      this.paramContext = paramContext;
   }

   public void setDefinition(AuthorizeDefinition definition) {
      this.definition = definition;
   }
}
