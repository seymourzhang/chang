package com.chang.binlog.common;

public enum DbType {
   MYSQL("mysql"),
   DM("dm");

   private final String dbName;

   public String getDbName() {
      return this.dbName;
   }

   private DbType(String dbName) {
      this.dbName = dbName;
   }
}
