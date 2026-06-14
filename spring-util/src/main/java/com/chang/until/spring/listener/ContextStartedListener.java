package com.chang.until.spring.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

public abstract class ContextStartedListener extends ContextListener implements ApplicationListener<ContextStartedEvent> {
   protected abstract void DoEvent(ContextStartedEvent var1);

   public void onApplicationEvent(ContextStartedEvent event) {
      ApplicationContext app = this.getFinallyParent(event.getApplicationContext());
      if (null == app.getParent()) {
         this.DoEvent(event);
      }

   }
}
