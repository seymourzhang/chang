package com.chang.design.eda;

import com.alibaba.fastjson.JSONObject;
import com.chang.until.spring.context.SpringContext;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdaManageService implements ManageServiceInterface {
   private String edaName;
   private static final Logger logger = LoggerFactory.getLogger(EdaManageService.class);
   private final ConcurrentHashMap<String, String> serviceMap = new ConcurrentHashMap();

   public EdaManageService(String edaName) {
      this.edaName = edaName;
   }

   public boolean registEventService(String eventId, String ServiceName) {
      Object service = SpringContext.getBean(ServiceName);
      if (null != service && service instanceof EventProcessingInterface) {
         if (!this.serviceMap.containsKey(eventId)) {
            this.serviceMap.put(eventId, ServiceName);
            return true;
         }

         logger.info("[EDA] RegistrationService registEventService Servcie bean not implements EventProcessingInterface eventId: " + eventId + " ServiceName:" + ServiceName);
      }

      logger.info("[EDA] RegistrationService  registEventService  regist eventId:" + eventId + " ServiceName:" + ServiceName + " fail");
      return false;
   }

   public boolean modifyEventService(String eventId, String ServiceName) {
      Object service = SpringContext.getBean(ServiceName);
      if (null != service && service instanceof EventProcessingInterface && this.serviceMap.containsKey(eventId)) {
         this.serviceMap.put(eventId, ServiceName);
         return true;
      } else {
         logger.info("[EDA] RegistrationService  modifyEventService  regist " + eventId + " " + ServiceName + " fail");
         return false;
      }
   }

   public boolean delEventService(String eventId) {
      if (this.serviceMap.containsKey(eventId)) {
         this.serviceMap.remove(eventId);
         return true;
      } else {
         logger.info("RegistrationService delEventService eventId not exit " + eventId);
         return false;
      }
   }

   public EventProcessingInterface getServiceByEventId(String eventId) {
      if (this.hasService(eventId)) {
         String ServiceName = (String)this.serviceMap.get(eventId);
         Object service = SpringContext.getBean(ServiceName);
         if (null != service && service instanceof EventProcessingInterface) {
            return (EventProcessingInterface)service;
         }
      }

      logger.info("RegistrationService getServiceByEventId event not exit: " + eventId);
      return null;
   }

   public <T> Object executeServiceByEventId(String eventId, T event) {
      if (this.hasService(eventId)) {
         String ServiceName = (String)this.serviceMap.get(eventId);
         Object service = SpringContext.getBean(ServiceName);
         if (null != service && service instanceof EventProcessingInterface) {
            EventProcessingInterface<T> eventProcessingInterface = (EventProcessingInterface)service;
            return eventProcessingInterface.process(event);
         }
      }

      logger.info("RegistrationService executeServiceByEventId eventId not exit " + eventId);
      return null;
   }

   public boolean hasService(String eventId) {
      return this.serviceMap.containsKey(eventId);
   }

   public JSONObject getAllEventService() {
      JSONObject eventInfo = new JSONObject();
      Iterator var2 = this.serviceMap.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var2.next();
         eventInfo.put((String)entry.getKey(), entry.getValue());
      }

      return eventInfo;
   }
}
