package com.chang.util.mybatis.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.chang.until.spring.context.SpringContext;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class MyInterceptorContextRefreshedListener implements SmartLifecycle {
   private static final Logger log = LoggerFactory.getLogger(MyInterceptorContextRefreshedListener.class);
   private static final AtomicBoolean firstRun = new AtomicBoolean(false);
   @Autowired
   private MybatisPlusProperties plusProperties;
   @Autowired
   private MybatisPlusInterceptor me;

   public void start() {
      try {
         if (ObjectUtil.equal(firstRun.get(), false)) {
            firstRun.set(true);
            String[] beanNames = this.plusProperties.getInnerInterceptorBeanNames();
            if (ObjectUtil.isNotNull(beanNames)) {
               String[] var2 = beanNames;
               int var3 = beanNames.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  String beanName = var2[var4];
                  Object o = SpringContext.getBean(beanName);
                  if (!(o instanceof InnerInterceptor)) {
                     throw new RuntimeException("Class not implements InnerInterceptor : " + beanName);
                  }

                  InnerInterceptor innerInterceptor = (InnerInterceptor)o;
                  this.me.addInnerInterceptor(innerInterceptor);
               }
            }
         }

      } catch (Exception var8) {
         log.error("DoWithContext Err", var8);
         throw new RuntimeException(var8);
      }
   }

   public void stop() {
   }

   public boolean isRunning() {
      return false;
   }
}
