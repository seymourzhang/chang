package com.chang.util.source.manage;

import com.chang.until.redisApi.lettuce.codec.MRedisTemplate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageRedisSource {
   private static final Map<String, MRedisTemplate> redisMaps = new ConcurrentHashMap();

   public static void addRedisDb(String name, MRedisTemplate mRedisTemplate) {
      if (!redisMaps.containsKey(name)) {
         redisMaps.put(name, mRedisTemplate);
      } else {
         throw new RuntimeException("dataBase already has name: " + name);
      }
   }

   public static MRedisTemplate getRedisDb(String name) {
      if (!redisMaps.containsKey(name)) {
         throw new RuntimeException("dataBase not exist！");
      } else {
         return (MRedisTemplate)redisMaps.get(name);
      }
   }
}
