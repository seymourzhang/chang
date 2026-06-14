package com.chang.util.source.manage;

import com.chang.util.source.common.DataBaseApi;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageMySqlSource {
   private static final Map<String, DataBaseApi> dataBaseMap = new ConcurrentHashMap();

   public static void addDbSource(String name, DataBaseApi dataBase) {
      if (!dataBaseMap.containsKey(name)) {
         dataBaseMap.put(name, dataBase);
      } else {
         throw new RuntimeException("dataBase already has name: " + name);
      }
   }

   public static DataBaseApi getDbSource(String name) {
      if (!dataBaseMap.containsKey(name)) {
         throw new RuntimeException("dataBase not exist！");
      } else {
         return (DataBaseApi)dataBaseMap.get(name);
      }
   }
}
