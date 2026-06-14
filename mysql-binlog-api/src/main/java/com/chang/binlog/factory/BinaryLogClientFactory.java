package com.chang.binlog.factory;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer.CompatibilityMode;
import com.chang.binlog.config.SyncConfig;
import com.chang.binlog.event.MultiEventHandlerListener;
import com.chang.binlog.event.lifecycle.ILifeCycleFactory;
import com.chang.binlog.event.parser.EventParserFactory;
import com.chang.binlog.position.BinlogPositionEntity;
import com.chang.binlog.position.IPositionHandler;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryLogClientFactory implements IClientFactory {
   private static final Logger log = LoggerFactory.getLogger(BinaryLogClientFactory.class);
   private ConcurrentHashMap<String, BinaryLogClient> cache = new ConcurrentHashMap();
   private IPositionHandler positionHandler;
   private ILifeCycleFactory lifeCycleFactory;

   public IPositionHandler getPositionHandler() {
      return this.positionHandler;
   }

   public void setPositionHandler(IPositionHandler positionHandler) {
      this.positionHandler = positionHandler;
   }

   public ConcurrentHashMap<String, BinaryLogClient> getCache() {
      return this.cache;
   }

   public void setCache(ConcurrentHashMap<String, BinaryLogClient> cache) {
      this.cache = cache;
   }

   public ILifeCycleFactory getLifeCycleFactory() {
      return this.lifeCycleFactory;
   }

   public void setLifeCycleFactory(ILifeCycleFactory lifeCycleFactory) {
      this.lifeCycleFactory = lifeCycleFactory;
   }

   public BinaryLogClient getClient(SyncConfig syncConfig) throws Exception {
      String key = syncConfig.getDbKey();
      if (this.cache.get(key) != null) {
         return (BinaryLogClient)this.cache.get(key);
      } else {
         BinaryLogClient client = new BinaryLogClient(syncConfig.getHost(), syncConfig.getPort(), StringUtils.isBlank(syncConfig.getDatabase()) ? null : syncConfig.getDatabase(), syncConfig.getUserName(), syncConfig.getPassword());
         EventDeserializer eventDeserializer = new EventDeserializer();
         eventDeserializer.setCompatibilityMode(CompatibilityMode.DATE_AND_TIME_AS_LONG, new EventDeserializer.CompatibilityMode[]{CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY});
         client.setEventDeserializer(eventDeserializer);
         client.setServerId(this.getRandomServerId());
         if (this.positionHandler != null && this.positionHandler.getPosition(syncConfig) != null) {
            BinlogPositionEntity positionEntity = this.positionHandler.getPosition(syncConfig);
            if (positionEntity != null && !StringUtils.isBlank(positionEntity.getBinlogName()) && positionEntity.getPosition() != null) {
               client.setBinlogFilename(positionEntity.getBinlogName());
               long position = positionEntity.getPosition();
               client.setBinlogPosition(position);
            }
         }

         MultiEventHandlerListener multiEventHandlerListener = new MultiEventHandlerListener();
         multiEventHandlerListener.setEventParserDispatcher(EventParserFactory.getEventParserDispatcher(syncConfig));
         multiEventHandlerListener.setSyncConfig(syncConfig);
         multiEventHandlerListener.setPositionHandler(this.positionHandler);
         syncConfig.getEventHandlerList().forEach(multiEventHandlerListener::registerEventHandler);
         syncConfig.getSqlHandlersList().forEach(multiEventHandlerListener::registerSqlHandler);
         client.registerEventListener(multiEventHandlerListener);
         client.registerLifecycleListener(this.lifeCycleFactory.getLifeCycleListener(syncConfig));
         this.cache.put(key, client);
         return client;
      }
   }

   public BinaryLogClient getCachedClient(SyncConfig syncConfig) {
      String key = syncConfig.toString();
      return (BinaryLogClient)this.cache.get(key);
   }

   private long getRandomServerId() {
      try {
         return SecureRandom.getInstanceStrong().nextLong();
      } catch (NoSuchAlgorithmException var2) {
         return RandomUtils.nextLong();
      }
   }
}
