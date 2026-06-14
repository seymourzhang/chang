package com.chang.until.spring.interceptor;

import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

public class MethodInterceptorProcessor {
   public static <T> T BeforeInterceptor(String pattern, MethodBeforeAdvice interceptor, Object target, Class<T> clazz) {
      if (!(target instanceof Advised)) {
         ProxyFactoryBean factory = new ProxyFactoryBean();
         factory.setTarget(target);
         factory.setProxyTargetClass(true);
         target = factory.getObject();
      }

      if (target instanceof Advised) {
         Advised advised = (Advised)Advised.class.cast(target);
         JdkRegexpMethodPointcut j = new JdkRegexpMethodPointcut();
         j.setPattern(pattern);
         MethodBeforeAdviceInterceptor beforeAdviceInterceptor = new MethodBeforeAdviceInterceptor(interceptor);
         Advisor myAdvice = new DefaultPointcutAdvisor(j, beforeAdviceInterceptor);
         advised.addAdvisor(myAdvice);
      }

      return clazz.cast(target);
   }

   public static <T> T AfterInterceptor(String pattern, AfterReturningAdvice interceptor, Object target, Class<T> clazz) {
      if (!(target instanceof Advised)) {
         ProxyFactoryBean factory = new ProxyFactoryBean();
         factory.setTarget(target);
         factory.setProxyTargetClass(true);
         target = factory.getObject();
      }

      if (target instanceof Advised) {
         Advised advised = (Advised)Advised.class.cast(target);
         JdkRegexpMethodPointcut j = new JdkRegexpMethodPointcut();
         j.setPattern(pattern);
         AfterReturningAdviceInterceptor afterReturningAdviceInterceptor = new AfterReturningAdviceInterceptor(interceptor);
         Advisor myAdvice = new DefaultPointcutAdvisor(j, afterReturningAdviceInterceptor);
         advised.addAdvisor(myAdvice);
      }

      return clazz.cast(target);
   }
}
