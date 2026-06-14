package com.chang.binlog.position;

import com.chang.binlog.config.SyncConfig;
import com.chang.until.redisApi.lettuce.codec.MRedisTemplate;

public class DefaultPositionHandler implements IPositionHandler {
   private final MRedisTemplate mRedisTemplate;

   public DefaultPositionHandler(MRedisTemplate mRedisTemplate) {
      this.mRedisTemplate = mRedisTemplate;
   }

   public BinlogPositionEntity getPosition(SyncConfig syncConfig) throws Exception {
      return (BinlogPositionEntity)this.mRedisTemplate.get(syncConfig.getHost() + ":" + syncConfig.getPort() + ":" + syncConfig.getDbKey() + ":" + syncConfig.getDatabase(), BinlogPositionEntity.class);
   }

   public void savePosition(SyncConfig syncConfig, BinlogPositionEntity binlogPositionEntity) throws Exception {
      this.mRedisTemplate.set(syncConfig.getHost() + ":" + syncConfig.getPort() + ":" + syncConfig.getDbKey() + ":" + syncConfig.getDatabase(), binlogPositionEntity);
   }
}
