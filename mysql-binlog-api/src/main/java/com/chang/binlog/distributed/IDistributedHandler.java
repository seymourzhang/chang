package com.chang.binlog.distributed;

import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.config.BinlogPortalConfig;

public interface IDistributedHandler {
   void start(BinlogPortalConfig var1) throws BinlogPortalException;
}
