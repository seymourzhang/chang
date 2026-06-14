package com.chang.util.source.spring.start.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(
   prefix = "wg.data.source.bin-log-properties-in-put-sources"
)
public class BinLogProperties {
   private String sourceKey;
   private Map<String, BinLogDbWatchProperties> binLogDbPropertiesMap;
   private Map<String, Object> parm = new HashMap();
   private String binLogRedisKey;

   public String getSourceKey() {
      return this.sourceKey;
   }

   public Map<String, BinLogDbWatchProperties> getBinLogDbPropertiesMap() {
      return this.binLogDbPropertiesMap;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public String getBinLogRedisKey() {
      return this.binLogRedisKey;
   }

   public void setSourceKey(String sourceKey) {
      this.sourceKey = sourceKey;
   }

   public void setBinLogDbPropertiesMap(Map<String, BinLogDbWatchProperties> binLogDbPropertiesMap) {
      this.binLogDbPropertiesMap = binLogDbPropertiesMap;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public void setBinLogRedisKey(String binLogRedisKey) {
      this.binLogRedisKey = binLogRedisKey;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BinLogProperties)) {
         return false;
      } else {
         BinLogProperties other = (BinLogProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$sourceKey = this.getSourceKey();
               Object other$sourceKey = other.getSourceKey();
               if (this$sourceKey == null) {
                  if (other$sourceKey == null) {
                     break label59;
                  }
               } else if (this$sourceKey.equals(other$sourceKey)) {
                  break label59;
               }

               return false;
            }

            Object this$binLogDbPropertiesMap = this.getBinLogDbPropertiesMap();
            Object other$binLogDbPropertiesMap = other.getBinLogDbPropertiesMap();
            if (this$binLogDbPropertiesMap == null) {
               if (other$binLogDbPropertiesMap != null) {
                  return false;
               }
            } else if (!this$binLogDbPropertiesMap.equals(other$binLogDbPropertiesMap)) {
               return false;
            }

            Object this$parm = this.getParm();
            Object other$parm = other.getParm();
            if (this$parm == null) {
               if (other$parm != null) {
                  return false;
               }
            } else if (!this$parm.equals(other$parm)) {
               return false;
            }

            Object this$binLogRedisKey = this.getBinLogRedisKey();
            Object other$binLogRedisKey = other.getBinLogRedisKey();
            if (this$binLogRedisKey == null) {
               if (other$binLogRedisKey != null) {
                  return false;
               }
            } else if (!this$binLogRedisKey.equals(other$binLogRedisKey)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof BinLogProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $sourceKey = this.getSourceKey();
      result = result * 59 + ($sourceKey == null ? 43 : $sourceKey.hashCode());
      Object $binLogDbPropertiesMap = this.getBinLogDbPropertiesMap();
      result = result * 59 + ($binLogDbPropertiesMap == null ? 43 : $binLogDbPropertiesMap.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      Object $binLogRedisKey = this.getBinLogRedisKey();
      result = result * 59 + ($binLogRedisKey == null ? 43 : $binLogRedisKey.hashCode());
      return result;
   }

   public String toString() {
      return "BinLogProperties(sourceKey=" + this.getSourceKey() + ", binLogDbPropertiesMap=" + this.getBinLogDbPropertiesMap() + ", parm=" + this.getParm() + ", binLogRedisKey=" + this.getBinLogRedisKey() + ")";
   }
}
