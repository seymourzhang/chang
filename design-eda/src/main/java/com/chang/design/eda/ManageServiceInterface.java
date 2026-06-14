package com.chang.design.eda;

import com.alibaba.fastjson.JSONObject;

public interface ManageServiceInterface {
   boolean registEventService(String var1, String var2);

   boolean modifyEventService(String var1, String var2);

   boolean delEventService(String var1);

   EventProcessingInterface getServiceByEventId(String var1);

   <T> Object executeServiceByEventId(String var1, T var2);

   boolean hasService(String var1);

   JSONObject getAllEventService();
}
