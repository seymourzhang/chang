package com.chang.binlog.config;

import com.chang.binlog.distributed.IDistributedHandler;
import com.chang.binlog.event.lifecycle.BaseLifeCycleListenerFactory;
import com.chang.binlog.event.lifecycle.ILifeCycleFactory;
import com.chang.binlog.factory.BinaryLogClientFactory;
import com.chang.binlog.factory.IClientFactory;
import com.chang.binlog.position.IPositionHandler;
import java.util.HashMap;
import java.util.Map;

public class BinlogPortalConfig {
   private Map<String, SyncConfig> syncConfigMap = new HashMap();
   private IPositionHandler positionHandler;
   private IDistributedHandler distributedHandler;
   private ILifeCycleFactory lifeCycleFactory = new BaseLifeCycleListenerFactory();
   private IClientFactory clientFactory = new BinaryLogClientFactory();

   public void addSyncConfig(String key, SyncConfig syncConfig) {
      this.syncConfigMap.put(key, syncConfig);
   }

   public Map<String, SyncConfig> getSyncConfigMap() {
      return this.syncConfigMap;
   }

   public IPositionHandler getPositionHandler() {
      return this.positionHandler;
   }

   public IDistributedHandler getDistributedHandler() {
      return this.distributedHandler;
   }

   public ILifeCycleFactory getLifeCycleFactory() {
      return this.lifeCycleFactory;
   }

   public IClientFactory getClientFactory() {
      return this.clientFactory;
   }

   public void setSyncConfigMap(Map<String, SyncConfig> syncConfigMap) {
      this.syncConfigMap = syncConfigMap;
   }

   public void setPositionHandler(IPositionHandler positionHandler) {
      this.positionHandler = positionHandler;
   }

   public void setDistributedHandler(IDistributedHandler distributedHandler) {
      this.distributedHandler = distributedHandler;
   }

   public void setLifeCycleFactory(ILifeCycleFactory lifeCycleFactory) {
      this.lifeCycleFactory = lifeCycleFactory;
   }

   public void setClientFactory(IClientFactory clientFactory) {
      this.clientFactory = clientFactory;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BinlogPortalConfig)) {
         return false;
      } else {
         BinlogPortalConfig other = (BinlogPortalConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label71: {
               Object this$syncConfigMap = this.getSyncConfigMap();
               Object other$syncConfigMap = other.getSyncConfigMap();
               if (this$syncConfigMap == null) {
                  if (other$syncConfigMap == null) {
                     break label71;
                  }
               } else if (this$syncConfigMap.equals(other$syncConfigMap)) {
                  break label71;
               }

               return false;
            }

            Object this$positionHandler = this.getPositionHandler();
            Object other$positionHandler = other.getPositionHandler();
            if (this$positionHandler == null) {
               if (other$positionHandler != null) {
                  return false;
               }
            } else if (!this$positionHandler.equals(other$positionHandler)) {
               return false;
            }

            label57: {
               Object this$distributedHandler = this.getDistributedHandler();
               Object other$distributedHandler = other.getDistributedHandler();
               if (this$distributedHandler == null) {
                  if (other$distributedHandler == null) {
                     break label57;
                  }
               } else if (this$distributedHandler.equals(other$distributedHandler)) {
                  break label57;
               }

               return false;
            }

            Object this$lifeCycleFactory = this.getLifeCycleFactory();
            Object other$lifeCycleFactory = other.getLifeCycleFactory();
            if (this$lifeCycleFactory == null) {
               if (other$lifeCycleFactory != null) {
                  return false;
               }
            } else if (!this$lifeCycleFactory.equals(other$lifeCycleFactory)) {
               return false;
            }

            Object this$clientFactory = this.getClientFactory();
            Object other$clientFactory = other.getClientFactory();
            if (this$clientFactory == null) {
               if (other$clientFactory == null) {
                  return true;
               }
            } else if (this$clientFactory.equals(other$clientFactory)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof BinlogPortalConfig;
   }

   public int hashCode() {
      int result = 1;
      Object $syncConfigMap = this.getSyncConfigMap();
      result = result * 59 + ($syncConfigMap == null ? 43 : $syncConfigMap.hashCode());
      Object $positionHandler = this.getPositionHandler();
      result = result * 59 + ($positionHandler == null ? 43 : $positionHandler.hashCode());
      Object $distributedHandler = this.getDistributedHandler();
      result = result * 59 + ($distributedHandler == null ? 43 : $distributedHandler.hashCode());
      Object $lifeCycleFactory = this.getLifeCycleFactory();
      result = result * 59 + ($lifeCycleFactory == null ? 43 : $lifeCycleFactory.hashCode());
      Object $clientFactory = this.getClientFactory();
      result = result * 59 + ($clientFactory == null ? 43 : $clientFactory.hashCode());
      return result;
   }

   public String toString() {
      return "BinlogPortalConfig(syncConfigMap=" + this.getSyncConfigMap() + ", positionHandler=" + this.getPositionHandler() + ", distributedHandler=" + this.getDistributedHandler() + ", lifeCycleFactory=" + this.getLifeCycleFactory() + ", clientFactory=" + this.getClientFactory() + ")";
   }
}
