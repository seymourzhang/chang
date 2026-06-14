package com.chang.until.redisApi.lettuce.codec;

import java.util.concurrent.TimeUnit;

public interface MRedisTemplate {
   void set(String var1, Object var2) throws Exception;

   void set(String var1, Object var2, Long var3, TimeUnit var4) throws Exception;

   <T> T get(String var1, Class<T> var2) throws Exception;
}
