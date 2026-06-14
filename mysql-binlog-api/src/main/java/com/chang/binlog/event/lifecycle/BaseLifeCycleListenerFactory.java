package com.chang.binlog.event.lifecycle;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.chang.binlog.config.SyncConfig;

public class BaseLifeCycleListenerFactory implements ILifeCycleFactory {
   public BinaryLogClient.LifecycleListener getLifeCycleListener(SyncConfig syncConfig) {
      return new BaseLifeCycleEventListener(syncConfig);
   }
}
