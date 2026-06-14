package com.chang.util.source.mysqlBinLog;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.BinlogPortalStarter;
import com.chang.binlog.common.SqlUtil;
import com.chang.binlog.config.BinlogPortalConfig;
import com.chang.binlog.config.SyncConfig;
import com.chang.binlog.distributed.DefaultDistributedHandler;
import com.chang.binlog.position.DefaultPositionHandler;
import com.chang.until.redisApi.lettuce.codec.MRedisTemplate;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.BinLogDbProperties;
import com.chang.util.source.common.SourceContext;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlBinLogInput implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(MySqlBinLogInput.class);
   private final Map<String, Object> parm;
   private final BinlogPortalConfig binlogPortalConfig;
   private static final String sqlUrl = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true";

   public MySqlBinLogInput(Map<String, BinLogDbProperties> binLogDbPropertiesMap, MRedisTemplate mRedisTemplate, Map<String, Object> parm) throws SQLException {
      this.parm = parm;
      this.binlogPortalConfig = new BinlogPortalConfig();

      SyncConfig syncConfig;
      String key;
      for(Iterator var4 = binLogDbPropertiesMap.entrySet().iterator(); var4.hasNext(); this.binlogPortalConfig.addSyncConfig(key, syncConfig)) {
         Map.Entry<String, BinLogDbProperties> entry = (Map.Entry)var4.next();
         syncConfig = new SyncConfig();
         key = (String)entry.getKey();
         BinLogDbProperties binLogDbProperties = (BinLogDbProperties)entry.getValue();
         syncConfig.setHost(binLogDbProperties.getHost());
         syncConfig.setPort(binLogDbProperties.getPort());
         syncConfig.setDatabase(binLogDbProperties.getDatabase());
         syncConfig.setUserName(binLogDbProperties.getUserName());
         syncConfig.setPassword(binLogDbProperties.getPassword());
         syncConfig.setLookTableList(binLogDbProperties.getLookTableList());
         syncConfig.setDbKey(key);
         syncConfig.setSyncDbType(binLogDbProperties.getSyncDbType());
         syncConfig.setDatabase(binLogDbProperties.getDatabase());
         String url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true", binLogDbProperties.getHost(), binLogDbProperties.getPort(), binLogDbProperties.getDatabase());
         syncConfig.setDataSource(SqlUtil.getMysqlSource(url, binLogDbProperties.getUserName(), binLogDbProperties.getPassword()));
         if (CollectionUtil.isEmpty(binLogDbProperties.getBlacklist())) {
            syncConfig.setBlacklist(ListUtil.toList(new String[]{"sync_sql"}));
         } else {
            syncConfig.setBlacklist(binLogDbProperties.getBlacklist());
         }
      }

      this.binlogPortalConfig.setPositionHandler(new DefaultPositionHandler(mRedisTemplate));
      this.binlogPortalConfig.setDistributedHandler(new DefaultDistributedHandler());
   }

   public void InPut(OutputSource source, Function<Object, Object> function) {
      this.binlogPortalConfig.getSyncConfigMap().forEach((key, syncConfig) -> {
         syncConfig.addSqlHandlerList((runSqlInfo) -> {
            SourceContext.setExParm(this.parm);
            if (ObjectUtil.isNotNull(function)) {
               source.Output(function.apply(runSqlInfo));
            } else {
               source.Output(runSqlInfo);
            }

            SourceContext.clearExParm();
         });
      });
      BinlogPortalStarter binlogPortalStarter = new BinlogPortalStarter();
      binlogPortalStarter.setBinlogPortalConfig(this.binlogPortalConfig);
      ThreadUtil.execute(() -> {
         try {
            binlogPortalStarter.start();
         } catch (BinlogPortalException var2) {
            log.error(var2.getMessage(), var2);
         }

      });
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
   }
}
