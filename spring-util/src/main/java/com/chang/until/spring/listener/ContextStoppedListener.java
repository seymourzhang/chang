package com.chang.until.spring.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

public abstract class ContextStoppedListener extends ContextListener implements ApplicationListener<ContextStoppedEvent> {
   protected abstract void DoEvent(ContextStoppedEvent var1);

   public void onApplicationEvent(ContextStoppedEvent event) {
      ApplicationContext app = this.getFinallyParent(event.getApplicationContext());
      if (null == app.getParent()) {
         this.DoEvent(event);
      }

   }
}
