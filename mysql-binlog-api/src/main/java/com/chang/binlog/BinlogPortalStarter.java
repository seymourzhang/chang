package com.chang.binlog;

import com.chang.binlog.config.BinlogPortalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinlogPortalStarter {
   private final Logger log = LoggerFactory.getLogger(BinlogPortalStarter.class);
   private BinlogPortalConfig binlogPortalConfig;

   public BinlogPortalConfig getBinlogPortalConfig() {
      return this.binlogPortalConfig;
   }

   public void setBinlogPortalConfig(BinlogPortalConfig binlogPortalConfig) {
      this.binlogPortalConfig = binlogPortalConfig;
   }

   public void start() throws BinlogPortalException {
      this.binlogPortalConfig.getDistributedHandler().start(this.binlogPortalConfig);
   }
}
