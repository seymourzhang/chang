package com.chang.until.spring.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public abstract class ContextClosedListener extends ContextListener implements ApplicationListener<ContextClosedEvent> {
   protected abstract void DoEvent(ContextClosedEvent var1);

   public void onApplicationEvent(ContextClosedEvent event) {
      ApplicationContext app = this.getFinallyParent(event.getApplicationContext());
      if (null == app.getParent()) {
         this.DoEvent(event);
      }

   }
}
