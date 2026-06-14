package com.chang.binlog.event.lifecycle;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.chang.binlog.config.SyncConfig;

public interface ILifeCycleFactory {
   BinaryLogClient.LifecycleListener getLifeCycleListener(SyncConfig var1);
}
