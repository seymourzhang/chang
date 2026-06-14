package com.chang.binlog.event.parser;

public class RunSqlInfo {
   private String dbKey;
   private String sqlType;
   private String sql;
   private String tableName;
   private Long timeStamp;

   public static RunSqlInfoBuilder builder() {
      return new RunSqlInfoBuilder();
   }

   public String getDbKey() {
      return this.dbKey;
   }

   public String getSqlType() {
      return this.sqlType;
   }

   public String getSql() {
      return this.sql;
   }

   public String getTableName() {
      return this.tableName;
   }

   public Long getTimeStamp() {
      return this.timeStamp;
   }

   public void setDbKey(String dbKey) {
      this.dbKey = dbKey;
   }

   public void setSqlType(String sqlType) {
      this.sqlType = sqlType;
   }

   public void setSql(String sql) {
      this.sql = sql;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public void setTimeStamp(Long timeStamp) {
      this.timeStamp = timeStamp;
   }

   public String toString() {
      return "RunSqlInfo(dbKey=" + this.getDbKey() + ", sqlType=" + this.getSqlType() + ", sql=" + this.getSql() + ", tableName=" + this.getTableName() + ", timeStamp=" + this.getTimeStamp() + ")";
   }

   public RunSqlInfo(String dbKey, String sqlType, String sql, String tableName, Long timeStamp) {
      this.dbKey = dbKey;
      this.sqlType = sqlType;
      this.sql = sql;
      this.tableName = tableName;
      this.timeStamp = timeStamp;
   }

   public RunSqlInfo() {
   }

   public static class RunSqlInfoBuilder {
      private String dbKey;
      private String sqlType;
      private String sql;
      private String tableName;
      private Long timeStamp;

      RunSqlInfoBuilder() {
      }

      public RunSqlInfoBuilder dbKey(String dbKey) {
         this.dbKey = dbKey;
         return this;
      }

      public RunSqlInfoBuilder sqlType(String sqlType) {
         this.sqlType = sqlType;
         return this;
      }

      public RunSqlInfoBuilder sql(String sql) {
         this.sql = sql;
         return this;
      }

      public RunSqlInfoBuilder tableName(String tableName) {
         this.tableName = tableName;
         return this;
      }

      public RunSqlInfoBuilder timeStamp(Long timeStamp) {
         this.timeStamp = timeStamp;
         return this;
      }

      public RunSqlInfo build() {
         return new RunSqlInfo(this.dbKey, this.sqlType, this.sql, this.tableName, this.timeStamp);
      }

      public String toString() {
         return "RunSqlInfo.RunSqlInfoBuilder(dbKey=" + this.dbKey + ", sqlType=" + this.sqlType + ", sql=" + this.sql + ", tableName=" + this.tableName + ", timeStamp=" + this.timeStamp + ")";
      }
   }
}
