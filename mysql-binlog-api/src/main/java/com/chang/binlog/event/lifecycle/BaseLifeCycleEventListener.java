package com.chang.binlog.event.lifecycle;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.chang.binlog.config.SyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLifeCycleEventListener implements BinaryLogClient.LifecycleListener {
   private final Logger log = LoggerFactory.getLogger(BaseLifeCycleEventListener.class);
   SyncConfig syncConfig;

   public BaseLifeCycleEventListener(SyncConfig syncConfig) {
      this.syncConfig = syncConfig;
   }

   public void onConnect(BinaryLogClient client) {
   }

   public void onCommunicationFailure(BinaryLogClient client, Exception ex) {
      this.log.error(this.syncConfig.getHost() + ":" + this.syncConfig.getPort() + "," + ex.getMessage() + "." + client.getBinlogFilename() + "/" + client.getBinlogPosition(), ex);
   }

   public void onEventDeserializationFailure(BinaryLogClient client, Exception ex) {
      this.log.error(this.syncConfig.getHost() + ":" + this.syncConfig.getPort() + "," + ex.getMessage(), ex);
   }

   public void onDisconnect(BinaryLogClient client) {
   }
}
