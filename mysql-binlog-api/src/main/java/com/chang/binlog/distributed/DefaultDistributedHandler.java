package com.chang.binlog.distributed;

import cn.hutool.core.thread.ThreadUtil;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.config.BinlogPortalConfig;
import com.chang.binlog.factory.IClientFactory;
import com.chang.until.timeTaskApi.TimeTaskManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDistributedHandler implements IDistributedHandler {
   private static final Logger log = LoggerFactory.getLogger(DefaultDistributedHandler.class);

   public void start(BinlogPortalConfig binlogPortalConfig) throws BinlogPortalException {
      IClientFactory binaryLogClientFactory = binlogPortalConfig.getClientFactory();
      binaryLogClientFactory.setPositionHandler(binlogPortalConfig.getPositionHandler());
      binaryLogClientFactory.setLifeCycleFactory(binlogPortalConfig.getLifeCycleFactory());
      binlogPortalConfig.getSyncConfigMap().forEach((key, syncConfig) -> {
         try {
            TimeTaskManage.createNewTask(() -> {
               try {
                  BinaryLogClient binaryLogClient = binaryLogClientFactory.getClient(syncConfig);
                  binaryLogClient.setHeartbeatInterval(10000L);
                  binaryLogClient.connect();
               } catch (Exception var3) {
                  log.error(var3.getMessage(), var3);
               }

            });
            ThreadUtil.sleep(1000L);
         } catch (Exception var4) {
            log.error(var4.getMessage(), var4);
            throw new RuntimeException(var4);
         }
      });
   }
}
