package com.chang.until.spring.listener;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.context.ApplicationContext;

public abstract class ContextListener {
   protected ApplicationContext getFinallyParent(ApplicationContext ac) {
      ApplicationContext app = ac.getParent();
      if (null != app) {
         this.getFinallyParent(app);
      }

      return app;
   }

   protected Map<String, Object> getBeansWithAnnotation(ApplicationContext ac, Class<? extends Annotation> annotationType) {
      return null == ac.getParent() ? ac.getBeansWithAnnotation(annotationType) : null;
   }
}
