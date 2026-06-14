package com.chang.util.source.manage;

import com.chang.util.source.common.SqliteDataBaseApi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageSqliteSource {
   private static final Map<String, SqliteDataBaseApi> dataBaseMap = new ConcurrentHashMap();

   public static void addDbSource(String name, SqliteDataBaseApi dataBase) {
      if (!dataBaseMap.containsKey(name)) {
         dataBaseMap.put(name, dataBase);
      } else {
         throw new RuntimeException("dataBase already has name: " + name);
      }
   }

   public static SqliteDataBaseApi getDbSource(String name) {
      if (!dataBaseMap.containsKey(name)) {
         throw new RuntimeException("dataBase not exist！");
      } else {
         return (SqliteDataBaseApi)dataBaseMap.get(name);
      }
   }
}
