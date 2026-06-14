package org.hswebframework.web.authorization.basic.aop;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hswebframework.web.aop.MethodInterceptorContext;
import org.hswebframework.web.aop.MethodInterceptorHolder;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.basic.handler.AuthorizingHandler;
import org.hswebframework.web.authorization.define.AuthorizeDefinition;
import org.hswebframework.web.authorization.define.AuthorizeDefinitionInitializedEvent;
import org.hswebframework.web.authorization.define.AuthorizingContext;
import org.hswebframework.web.authorization.define.Phased;
import org.hswebframework.web.authorization.exception.UnAuthorizedException;
import org.hswebframework.web.utils.AnnotationUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AopAuthorizingController extends StaticMethodMatcherPointcutAdvisor implements CommandLineRunner, MethodInterceptor {
   private static final Logger log = LoggerFactory.getLogger(AopAuthorizingController.class);
   private static final long serialVersionUID = 1154190623020670672L;
   @Autowired
   private ApplicationEventPublisher eventPublisher;
   @Autowired
   private AuthorizingHandler authorizingHandler;
   @Autowired
   private AopMethodAuthorizeDefinitionParser aopMethodAuthorizeDefinitionParser;
   private boolean autoParse = false;

   public void setAutoParse(boolean autoParse) {
      this.autoParse = autoParse;
   }

   protected Publisher<?> handleReactive0(AuthorizeDefinition definition, MethodInterceptorHolder holder, AuthorizingContext context, Supplier<? extends Publisher<?>> invoker) {
      return Authentication.currentReactive().switchIfEmpty(Mono.error(UnAuthorizedException::new)).flatMapMany((auth) -> {
         context.setAuthentication(auth);
         Function<Runnable, Publisher> afterRuner = (runnable) -> {
            MethodInterceptorContext interceptorContext = holder.createParamContext(invoker.get());
            context.setParamContext(interceptorContext);
            runnable.run();
            return (Publisher)interceptorContext.getInvokeResult();
         };
         if (context.getDefinition().getPhased() != Phased.after) {
            this.authorizingHandler.handRBAC(context);
            if (context.getDefinition().getResources().getPhased() != Phased.after) {
               this.authorizingHandler.handleDataAccess(context);
               return (Publisher)invoker.get();
            } else {
               return (Publisher)afterRuner.apply(() -> {
                  this.authorizingHandler.handleDataAccess(context);
               });
            }
         } else if (context.getDefinition().getResources().getPhased() != Phased.after) {
            this.authorizingHandler.handleDataAccess(context);
            return (Publisher)invoker.get();
         } else {
            return (Publisher)afterRuner.apply(() -> {
               this.authorizingHandler.handRBAC(context);
               this.authorizingHandler.handleDataAccess(context);
            });
         }
      });
   }

   private <T> T doProceed(MethodInvocation invocation) {
      try {
         return invocation.proceed();
      } catch (Throwable var3) {
         throw var3;
      }
   }

   public Object invoke(MethodInvocation methodInvocation) throws Throwable {
      MethodInterceptorHolder holder = MethodInterceptorHolder.create(methodInvocation);
      MethodInterceptorContext paramContext = holder.createParamContext();
      AuthorizeDefinition definition = this.aopMethodAuthorizeDefinitionParser.parse(methodInvocation.getThis().getClass(), methodInvocation.getMethod(), paramContext);
      Object result = null;
      boolean isControl = false;
      if (null != definition && !definition.isEmpty()) {
         AuthorizingContext context = new AuthorizingContext();
         context.setDefinition(definition);
         context.setParamContext(paramContext);
         Class<?> returnType = methodInvocation.getMethod().getReturnType();
         if (Publisher.class.isAssignableFrom(returnType)) {
            Publisher publisher = this.handleReactive0(definition, holder, context, () -> {
               return (Publisher)this.doProceed(methodInvocation);
            });
            if (Mono.class.isAssignableFrom(returnType)) {
               return Mono.from(publisher);
            }

            if (Flux.class.isAssignableFrom(returnType)) {
               return Flux.from(publisher);
            }

            throw new UnsupportedOperationException("unsupported reactive type:" + returnType);
         }

         Authentication authentication = (Authentication)Authentication.current().orElseThrow(UnAuthorizedException::new);
         context.setAuthentication(authentication);
         isControl = true;
         Phased dataAccessPhased = null;
         dataAccessPhased = definition.getResources().getPhased();
         if (definition.getPhased() == Phased.before) {
            this.authorizingHandler.handRBAC(context);
            if (dataAccessPhased == Phased.before) {
               this.authorizingHandler.handleDataAccess(context);
            }

            result = methodInvocation.proceed();
            if (dataAccessPhased == Phased.after) {
               context.setParamContext(holder.createParamContext(result));
               this.authorizingHandler.handleDataAccess(context);
            }
         } else {
            if (dataAccessPhased == Phased.before) {
               this.authorizingHandler.handleDataAccess(context);
            }

            result = methodInvocation.proceed();
            context.setParamContext(holder.createParamContext(result));
            this.authorizingHandler.handRBAC(context);
            if (dataAccessPhased == Phased.after) {
               this.authorizingHandler.handleDataAccess(context);
            }
         }
      }

      if (!isControl) {
         result = methodInvocation.proceed();
      }

      return result;
   }

   public AopAuthorizingController(AuthorizingHandler authorizingHandler, AopMethodAuthorizeDefinitionParser aopMethodAuthorizeDefinitionParser) {
      this.authorizingHandler = authorizingHandler;
      this.aopMethodAuthorizeDefinitionParser = aopMethodAuthorizeDefinitionParser;
      this.setAdvice(this);
   }

   public boolean matches(Method method, Class<?> aClass) {
      Authorize authorize;
      boolean support = AnnotationUtils.findAnnotation(aClass, Controller.class) != null || AnnotationUtils.findAnnotation(aClass, RestController.class) != null || (authorize = (Authorize)AnnotationUtils.findAnnotation(aClass, method, Authorize.class)) != null && !authorize.ignore();
      if (support && this.autoParse) {
         this.aopMethodAuthorizeDefinitionParser.parse(aClass, method);
      }

      return support;
   }

   public void run(String... args) throws Exception {
      if (this.autoParse) {
         List<AuthorizeDefinition> definitions = (List)this.aopMethodAuthorizeDefinitionParser.getAllParsed().stream().filter((def) -> {
            return !def.isEmpty();
         }).collect(Collectors.toList());
         log.info("publish AuthorizeDefinitionInitializedEvent,definition size:{}", definitions.size());
         this.eventPublisher.publishEvent(new AuthorizeDefinitionInitializedEvent(definitions));
      }

   }
}
