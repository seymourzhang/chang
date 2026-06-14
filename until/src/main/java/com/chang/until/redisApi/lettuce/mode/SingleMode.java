package com.chang.until.redisApi.lettuce.mode;

import com.chang.common.CommUtils;
import com.chang.until.redisApi.lettuce.codec.MRedisTemplate;
import com.chang.until.redisApi.lettuce.codec.StringValueCodec;
import com.chang.until.redisApi.lettuce.conf.Client;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class SingleMode implements MRedisTemplate {
   private final RedisClient redisClient;
   private final ClientResources res = DefaultClientResources.builder().ioThreadPoolSize(8).computationThreadPoolSize(8).build();
   private final GenericObjectPool<StatefulRedisConnection<String, byte[]>> pools;

   public SingleMode(Client client) {
      RedisURI redisUri = RedisURI.builder().withHost(client.getHost()).withPort(client.getPort()).withPassword(client.getPassword().toCharArray()).withTimeout(Duration.ofMillis(30000L)).withDatabase(client.getDatabase()).build();
      this.redisClient = RedisClient.create(this.res, redisUri);
      GenericObjectPoolConfig<StatefulRedisConnection<String, byte[]>> poolConfig = new GenericObjectPoolConfig();
      poolConfig.setMaxIdle(8);
      poolConfig.setMinIdle(1);
      poolConfig.setMaxTotal(10);
      poolConfig.setMaxWait(Duration.ofMillis(-1L));
      poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(60000L));
      this.pools = ConnectionPoolSupport.createGenericObjectPool(() -> {
         return this.redisClient.connect(new StringValueCodec());
      }, poolConfig);
   }

   public void set(String key, Object value) throws Exception {
      StatefulRedisConnection<String, byte[]> connect = null;

      try {
         connect = (StatefulRedisConnection)this.pools.borrowObject();
         connect.sync().set(key, CommUtils.serialize(value));
      } finally {
         this.pools.returnObject(connect);
      }

   }

   public void set(String key, Object value, Long time, TimeUnit unit) throws Exception {
      StatefulRedisConnection<String, byte[]> connect = null;

      try {
         connect = (StatefulRedisConnection)this.pools.borrowObject();
         connect.sync().psetex(key, unit.toMillis(time), CommUtils.serialize(value));
      } finally {
         this.pools.returnObject(connect);
      }

   }

   public <T> T get(String key, Class<T> type) throws Exception {
      StatefulRedisConnection<String, byte[]> connect = null;

      Object var4;
      try {
         connect = (StatefulRedisConnection)this.pools.borrowObject();
         var4 = CommUtils.deserialize((byte[])connect.sync().get(key), type);
      } finally {
         this.pools.returnObject(connect);
      }

      return (T) var4;
   }

   public void close() {
      this.pools.close();
      this.redisClient.shutdown();
      this.res.shutdown();
   }
}
