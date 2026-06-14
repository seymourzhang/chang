package com.chang.until.redisApi.lettuce.mode;

import com.chang.common.CommUtils;
import com.chang.until.redisApi.lettuce.codec.MRedisTemplate;
import com.chang.until.redisApi.lettuce.codec.StringValueCodec;
import com.chang.until.redisApi.lettuce.conf.ClusterClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions.RefreshTrigger;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ClusterMode implements MRedisTemplate {
   private final RedisClusterClient clusterClient;
   private final ClientResources res = DefaultClientResources.builder().ioThreadPoolSize(8).computationThreadPoolSize(8).build();
   private final GenericObjectPool<StatefulRedisClusterConnection<String, byte[]>> pools;

   public ClusterMode(ClusterClient clusterClientConf) {
      List<RedisURI> redisURIS = (List)clusterClientConf.getNodes().stream().map((x) -> {
         return RedisURI.builder().withHost(x.getHost()).withPort(x.getPort()).withPassword(clusterClientConf.getPassword().toCharArray()).withDatabase(clusterClientConf.getDatabase()).withTimeout(Duration.ofMillis(30000L)).build();
      }).collect(Collectors.toList());
      RedisClusterClient clusterClientNew = RedisClusterClient.create(this.res, redisURIS);
      ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder().enableAdaptiveRefreshTrigger(new ClusterTopologyRefreshOptions.RefreshTrigger[]{RefreshTrigger.MOVED_REDIRECT, RefreshTrigger.PERSISTENT_RECONNECTS}).adaptiveRefreshTriggersTimeout(30L, TimeUnit.SECONDS).build();
      clusterClientNew.setOptions(ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).maxRedirects(3).build());
      this.clusterClient = clusterClientNew;
      GenericObjectPoolConfig<StatefulRedisClusterConnection<String, byte[]>> poolConfig = new GenericObjectPoolConfig();
      poolConfig.setMaxIdle(16);
      poolConfig.setMinIdle(8);
      poolConfig.setMaxTotal(20);
      poolConfig.setMaxWait(Duration.ofMillis(-1L));
      poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(60000L));
      this.pools = ConnectionPoolSupport.createGenericObjectPool(() -> {
         return this.clusterClient.connect(new StringValueCodec());
      }, poolConfig);
   }

   public void set(String key, Object value) throws Exception {
      StatefulRedisClusterConnection<String, byte[]> connect = null;

      try {
         connect = (StatefulRedisClusterConnection)this.pools.borrowObject();
         connect.sync().set(key, CommUtils.serialize(value));
      } finally {
         this.pools.returnObject(connect);
      }

   }

   public void set(String key, Object value, Long time, TimeUnit unit) throws Exception {
      StatefulRedisClusterConnection<String, byte[]> connect = null;

      try {
         connect = (StatefulRedisClusterConnection)this.pools.borrowObject();
         connect.sync().psetex(key, unit.toMillis(time), CommUtils.serialize(value));
      } finally {
         this.pools.returnObject(connect);
      }

   }

   public <T> T get(String key, Class<T> type) throws Exception {
      StatefulRedisClusterConnection<String, byte[]> connect = null;

      Object var4;
      try {
         connect = (StatefulRedisClusterConnection)this.pools.borrowObject();
         var4 = CommUtils.deserialize((byte[])connect.sync().get(key), type);
      } finally {
         this.pools.returnObject(connect);
      }

      return (T) var4;
   }

   public void close() {
      this.pools.close();
      this.clusterClient.shutdown();
      this.res.shutdown();
   }
}
