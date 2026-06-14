package com.chang.until.spring.context;

import org.springframework.beans.factory.FactoryBean;

public abstract class ProxyFactoryBean<T> implements FactoryBean<T> {
   public T getObject() throws Exception {
      return null;
   }

   public Class<?> getObjectType() {
      return null;
   }

   public boolean isSingleton() {
      return false;
   }
}
