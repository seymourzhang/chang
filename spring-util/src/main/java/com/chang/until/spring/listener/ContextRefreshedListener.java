package com.chang.until.spring.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public abstract class ContextRefreshedListener extends ContextListener implements ApplicationListener<ContextRefreshedEvent> {
   protected abstract void DoEvent(ContextRefreshedEvent var1);

   public void onApplicationEvent(ContextRefreshedEvent event) {
      ApplicationContext app = this.getFinallyParent(event.getApplicationContext());
      if (null == app.getParent()) {
         this.DoEvent(event);
      }

   }
}
