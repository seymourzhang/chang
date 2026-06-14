package com.chang.binlog.position;

import com.chang.binlog.config.SyncConfig;

public interface IPositionHandler {
   BinlogPositionEntity getPosition(SyncConfig var1) throws Exception;

   void savePosition(SyncConfig var1, BinlogPositionEntity var2) throws Exception;
}
