package com.chang.design.eda;

import com.chang.until.reflect.AopTargetUtils;
import com.chang.until.spring.context.SpringContext;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Order(99)
public class EdaHandle implements ApplicationListener<ContextRefreshedEvent> {
   private static final Logger logger = LoggerFactory.getLogger(EdaHandle.class);

   public void onApplicationEvent(ContextRefreshedEvent event) {
      try {
         Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(AddToEdaManage.class);
         Iterator var3 = beans.values().iterator();

         while(true) {
            while(true) {
               while(var3.hasNext()) {
                  Object bean = var3.next();
                  if (null != bean) {
                     bean = AopTargetUtils.getTarget(bean);
                     logger.info("[EDA] EdaHandle.onApplicationEvent bean name: " + bean.getClass().getName());
                     AddToEdaManage addToEdaManage = (AddToEdaManage)bean.getClass().getAnnotation(AddToEdaManage.class);
                     String mn = addToEdaManage.manageName();
                     String eventId = addToEdaManage.eventId();
                     String processName = addToEdaManage.processName();
                     if (StringUtils.hasText(mn) && StringUtils.hasText(eventId) && StringUtils.hasText(processName)) {
                        EdaManageService edaManageService = (EdaManageService)SpringContext.getBean(mn, EdaManageService.class);
                        if (edaManageService != null) {
                           logger.info("[EDA] EdaHandle.onApplicationEvent registEventService eventId: " + eventId + " processName: " + processName);
                           edaManageService.registEventService(eventId, processName);
                        } else {
                           logger.info("[EDA] EdaHandle.onApplicationEventb edaManageService is null name: " + mn);
                        }
                     } else {
                        logger.info("[EDA] EdaHandle.onApplicationEvent AddToEdaManage info err!");
                     }
                  } else {
                     logger.info("[EDA] EdaHandle.onApplicationEvent bean is null");
                  }
               }

               return;
            }
         }
      } catch (Exception var10) {
         logger.error("[EDA] EdaHandle.onApplicationEvent err", var10);
      }
   }
}
