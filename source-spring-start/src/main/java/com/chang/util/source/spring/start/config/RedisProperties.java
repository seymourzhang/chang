package com.chang.util.source.spring.start.config;

import com.chang.until.redisApi.lettuce.conf.Client;
import com.chang.until.redisApi.lettuce.conf.ClusterClient;
import com.chang.until.redisApi.lettuce.conf.SentinelClient;

public class RedisProperties {
   private RedisModeType redisMode;
   private Client client;
   private ClusterClient clusterClient;
   private SentinelClient sentinelClient;

   public RedisModeType getRedisMode() {
      return this.redisMode;
   }

   public Client getClient() {
      return this.client;
   }

   public ClusterClient getClusterClient() {
      return this.clusterClient;
   }

   public SentinelClient getSentinelClient() {
      return this.sentinelClient;
   }

   public void setRedisMode(RedisModeType redisMode) {
      this.redisMode = redisMode;
   }

   public void setClient(Client client) {
      this.client = client;
   }

   public void setClusterClient(ClusterClient clusterClient) {
      this.clusterClient = clusterClient;
   }

   public void setSentinelClient(SentinelClient sentinelClient) {
      this.sentinelClient = sentinelClient;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RedisProperties)) {
         return false;
      } else {
         RedisProperties other = (RedisProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$redisMode = this.getRedisMode();
               Object other$redisMode = other.getRedisMode();
               if (this$redisMode == null) {
                  if (other$redisMode == null) {
                     break label59;
                  }
               } else if (this$redisMode.equals(other$redisMode)) {
                  break label59;
               }

               return false;
            }

            Object this$client = this.getClient();
            Object other$client = other.getClient();
            if (this$client == null) {
               if (other$client != null) {
                  return false;
               }
            } else if (!this$client.equals(other$client)) {
               return false;
            }

            Object this$clusterClient = this.getClusterClient();
            Object other$clusterClient = other.getClusterClient();
            if (this$clusterClient == null) {
               if (other$clusterClient != null) {
                  return false;
               }
            } else if (!this$clusterClient.equals(other$clusterClient)) {
               return false;
            }

            Object this$sentinelClient = this.getSentinelClient();
            Object other$sentinelClient = other.getSentinelClient();
            if (this$sentinelClient == null) {
               if (other$sentinelClient != null) {
                  return false;
               }
            } else if (!this$sentinelClient.equals(other$sentinelClient)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof RedisProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $redisMode = this.getRedisMode();
      result = result * 59 + ($redisMode == null ? 43 : $redisMode.hashCode());
      Object $client = this.getClient();
      result = result * 59 + ($client == null ? 43 : $client.hashCode());
      Object $clusterClient = this.getClusterClient();
      result = result * 59 + ($clusterClient == null ? 43 : $clusterClient.hashCode());
      Object $sentinelClient = this.getSentinelClient();
      result = result * 59 + ($sentinelClient == null ? 43 : $sentinelClient.hashCode());
      return result;
   }

   public String toString() {
      return "RedisProperties(redisMode=" + this.getRedisMode() + ", client=" + this.getClient() + ", clusterClient=" + this.getClusterClient() + ", sentinelClient=" + this.getSentinelClient() + ")";
   }
}
