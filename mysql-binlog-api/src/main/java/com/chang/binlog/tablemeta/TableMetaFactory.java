package com.chang.binlog.tablemeta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.config.SyncConfig;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableMetaFactory {
   private static final Logger log = LoggerFactory.getLogger(TableMetaFactory.class);
   private SyncConfig syncConfig;
   private Map<Long, TableMetaEntity> tableMetaEntityIdMap = new ConcurrentHashMap();

   public TableMetaFactory(SyncConfig syncConfig) {
      this.syncConfig = syncConfig;
   }

   public TableMetaEntity getTableMetaEntity(Long tableId, String dbName, String tableName) throws BinlogPortalException {
      if (!ObjectUtil.equals(this.syncConfig.getDatabase(), dbName)) {
         return null;
      } else if (this.syncConfig.getBlacklist().contains(tableName)) {
         return null;
      } else if (CollectionUtil.isNotEmpty(this.syncConfig.getLookTableList()) && !this.syncConfig.getLookTableList().contains(tableName)) {
         return null;
      } else {
         ResultSet rs = null;
         DruidPooledConnection connection = null;

         try {
            TableMetaEntity tableMeta = (TableMetaEntity)this.tableMetaEntityIdMap.get(tableId);
            if (ObjectUtil.isNotNull(tableMeta)) {
               TableMetaEntity var27 = tableMeta;
               return var27;
            } else {
               DruidDataSource dataSource = this.syncConfig.getDataSource();
               connection = dataSource.getConnection();
               DatabaseMetaData bmd = connection.getMetaData();
               rs = bmd.getColumns(connection.getCatalog(), connection.getSchema(), tableName, (String)null);
               TableMetaEntity tableMetaEntity = new TableMetaEntity();
               tableMetaEntity.setTableId(tableId);
               tableMetaEntity.setDbName(dbName);
               tableMetaEntity.setTableName(tableName);

               while(rs.next()) {
                  TableMetaEntity.ColumnMetaData columnMetaData = new TableMetaEntity.ColumnMetaData();
                  String colName = rs.getString("COLUMN_NAME");
                  columnMetaData.setName(colName);
                  String dbType = rs.getString("TYPE_NAME");
                  columnMetaData.setType(dbType);
                  tableMetaEntity.getColumnMetaDataList().add(columnMetaData);
               }

               this.tableMetaEntityIdMap.put(tableId, tableMetaEntity);
               TableMetaEntity var28 = tableMetaEntity;
               return var28;
            }
         } catch (Throwable var25) {
            throw new BinlogPortalException(var25.getCause());
         } finally {
            if (ObjectUtil.isNotNull(rs)) {
               try {
                  rs.close();
               } catch (Exception var24) {
                  log.error("close rs error", var24);
               }
            }

            if (ObjectUtil.isNotNull(connection)) {
               try {
                  connection.close();
               } catch (Exception var23) {
                  log.error("close rs error", var23);
               }
            }

         }
      }
   }

   public TableMetaEntity getTableMetaEntity(Long tableId) {
      return (TableMetaEntity)this.tableMetaEntityIdMap.get(tableId);
   }

   public SyncConfig getSyncConfig() {
      return this.syncConfig;
   }
}
