package com.chang.until.spring.context;

import com.chang.until.reflect.ReflectionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("SpringContext")
public class SpringContext implements ApplicationContextAware {
   private static final Logger logger = LoggerFactory.getLogger(SpringContext.class);
   private static ConfigurableApplicationContext context = null;
   private static DefaultListableBeanFactory beanFactory = null;

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      context = (ConfigurableApplicationContext)applicationContext;
      beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
      logger.info("!----setApplicationContext----!");
   }

   public static ApplicationContext getContext() {
      return context;
   }

   public static void registerBeans(String beanName, Class<?> clazz) throws BeansException {
      if (!beanFactory.containsBeanDefinition(beanName)) {
         BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
         beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
      } else {
         logger.info("register bean name already exists.");
      }

   }

   public static void registerBeans(String beanName, BeanDefinition beanDefinition) throws BeansException {
      beanFactory.registerBeanDefinition(beanName, beanDefinition);
   }

   public static void registerSingletonBean(String beanName, Object singletonObject) {
      if (!beanFactory.containsBeanDefinition(beanName)) {
         beanFactory.registerSingleton(beanName, singletonObject);
      } else {
         logger.info("register bean name already exists.");
      }

   }

   public static boolean registerSingletonBeanAndCheck(String beanName, Object singletonObject) {
      if (!beanFactory.containsBeanDefinition(beanName)) {
         beanFactory.registerSingleton(beanName, singletonObject);
         return !ObjectUtils.notEqual(beanFactory.getSingleton(beanName), singletonObject);
      } else {
         logger.info("register bean name already exists.");
         return false;
      }
   }

   public static void removeBean(String beanName) {
      if (beanFactory.containsBeanDefinition(beanName)) {
         beanFactory.removeBeanDefinition(beanName);
      }

   }

   public static void removeSingletonBean(String beanName) {
      if (beanFactory.containsBeanDefinition(beanName)) {
         beanFactory.destroySingleton(beanName);
      }

   }

   public static Class<?> readClassFileRegisterBean(String filename, String className, String beanName) throws Exception {
      ReflectionUtils reflectionUtils = new ReflectionUtils();
      Class<?> calzz = reflectionUtils.readClassFile(filename, className);
      registerBeans(beanName, calzz);
      return calzz;
   }

   public static ApplicationContext getApplicationContext() {
      return context;
   }

   public static Object getBean(String name) {
      return context.getBean(name);
   }

   public static Object getBeanForClassName(String className) throws ClassNotFoundException {
      Class aClass = Class.forName(className);
      return context.getBean(aClass);
   }

   public static <T> T getBean(Class<T> clazz) {
      try {
         return context.getBean(clazz);
      } catch (NoSuchBeanDefinitionException var2) {
         return null;
      }
   }

   public static <T> List<T> getBeans(Class<T> clazz) {
      try {
         Map<String, T> beansOfType = context.getBeansOfType(clazz);
         Collection<T> values = beansOfType.values();
         List<T> lists = new ArrayList();
         Iterator var4 = values.iterator();

         while(var4.hasNext()) {
            T v = (T) var4.next();
            lists.add(v);
         }

         return lists;
      } catch (Exception var6) {
         return null;
      }
   }

   public static <T> T getBeanForClass(Class targetClz) {
      T beanInstance = null;

      try {
         beanInstance = (T) context.getBean(targetClz);
      } catch (Exception var3) {
      }

      if (beanInstance == null) {
         String simpleName = targetClz.getSimpleName();
         simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
         beanInstance = (T) context.getBean(simpleName);
      }

      if (beanInstance == null) {
         new RuntimeException("Component " + targetClz + " can not be found in Spring Container");
      }

      return beanInstance;
   }

   public static <T> void order(List<T> interceptorIList) {
      if (interceptorIList != null && interceptorIList.size() > 1) {
         T newInterceptor = interceptorIList.get(interceptorIList.size() - 1);
         Order order = (Order)newInterceptor.getClass().getDeclaredAnnotation(Order.class);
         if (order != null) {
            int index = interceptorIList.size() - 1;

            for(int i = interceptorIList.size() - 2; i >= 0; index = i--) {
               int itemOrderInt = Integer.MAX_VALUE;
               Order itemOrder = (Order)interceptorIList.get(i).getClass().getDeclaredAnnotation(Order.class);
               if (itemOrder != null) {
                  itemOrderInt = itemOrder.value();
               }

               if (itemOrderInt <= order.value()) {
                  break;
               }

               interceptorIList.set(index, interceptorIList.get(i));
            }

            if (index < interceptorIList.size() - 1) {
               interceptorIList.set(index, newInterceptor);
            }

         }
      }
   }

   public static <T> T getBean(String name, Class<T> clazz) {
      return context.getBean(name, clazz);
   }

   public static String[] getBeanName(Class<?> clazz) {
      return context.getBeanNamesForType(clazz);
   }
}
