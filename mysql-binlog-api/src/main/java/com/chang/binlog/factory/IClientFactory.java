package com.chang.binlog.factory;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.chang.binlog.config.SyncConfig;
import com.chang.binlog.event.lifecycle.ILifeCycleFactory;
import com.chang.binlog.position.IPositionHandler;

public interface IClientFactory {
   BinaryLogClient getClient(SyncConfig var1) throws Exception;

   BinaryLogClient getCachedClient(SyncConfig var1);

   void setPositionHandler(IPositionHandler var1);

   IPositionHandler getPositionHandler();

   void setLifeCycleFactory(ILifeCycleFactory var1);

   ILifeCycleFactory getLifeCycleFactory();
}
