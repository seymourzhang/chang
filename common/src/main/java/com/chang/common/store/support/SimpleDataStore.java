package com.chang.common.store.support;

import com.chang.common.store.DataStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleDataStore implements DataStore {
   private ConcurrentMap<String, ConcurrentMap<String, Object>> data = new ConcurrentHashMap();

   public Map<String, Object> get(String componentName) {
      ConcurrentMap<String, Object> value = (ConcurrentMap)this.data.get(componentName);
      return value == null ? new HashMap() : new HashMap(value);
   }

   public Object get(String componentName, String key) {
      return !this.data.containsKey(componentName) ? null : ((ConcurrentMap)this.data.get(componentName)).get(key);
   }

   public void put(String componentName, String key, Object value) {
      Map<String, Object> componentData = (Map)this.data.computeIfAbsent(componentName, (k) -> {
         return new ConcurrentHashMap();
      });
      componentData.put(key, value);
   }

   public void remove(String componentName, String key) {
      if (this.data.containsKey(componentName)) {
         ((ConcurrentMap)this.data.get(componentName)).remove(key);
      }
   }
}
