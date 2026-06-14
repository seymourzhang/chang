package com.chang.binlog.tablemeta;

import java.util.ArrayList;
import java.util.List;

public class TableMetaEntity {
   private Long tableId;
   private String dbName;
   private String tableName;
   private List<ColumnMetaData> columnMetaDataList = new ArrayList();
   private String createSql;

   public Long getTableId() {
      return this.tableId;
   }

   public void setTableId(Long tableId) {
      this.tableId = tableId;
   }

   public String getDbName() {
      return this.dbName;
   }

   public void setDbName(String dbName) {
      this.dbName = dbName;
   }

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public List<ColumnMetaData> getColumnMetaDataList() {
      return this.columnMetaDataList;
   }

   public void setColumnMetaDataList(List<ColumnMetaData> columnMetaDataList) {
      this.columnMetaDataList = columnMetaDataList;
   }

   public String getCreateSql() {
      return this.createSql;
   }

   public void setCreateSql(String createSql) {
      this.createSql = createSql;
   }

   public static class ColumnMetaData {
      String name;
      String type;

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getType() {
         return this.type;
      }

      public void setType(String type) {
         this.type = type;
      }
   }
}
