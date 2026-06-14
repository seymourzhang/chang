package com.chang.binlog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.chang.binlog.common.DbType;
import com.chang.binlog.event.handler.IEventHandler;
import com.chang.binlog.event.handler.ISqlHandler;
import java.util.ArrayList;
import java.util.List;

public class SyncConfig {
   private String dbKey;
   private String host;
   private Integer port;
   private String database;
   private String userName;
   private String password;
   private DbType syncDbType;
   private DruidDataSource dataSource;
   private List<String> blacklist;
   private List<String> lookTableList;
   private List<IEventHandler> eventHandlerList = new ArrayList();
   private List<ISqlHandler> sqlHandlersList = new ArrayList();

   public SyncConfig() {
   }

   public SyncConfig(String host, Integer port, String userName, String password) {
      this.host = host;
      this.port = port;
      this.userName = userName;
      this.password = password;
   }

   public void addEventHandlerList(IEventHandler eventHandler) {
      this.eventHandlerList.add(eventHandler);
   }

   public void addSqlHandlerList(ISqlHandler sqlHandler) {
      this.sqlHandlersList.add(sqlHandler);
   }

   public String getDbKey() {
      return this.dbKey;
   }

   public String getHost() {
      return this.host;
   }

   public Integer getPort() {
      return this.port;
   }

   public String getDatabase() {
      return this.database;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassword() {
      return this.password;
   }

   public DbType getSyncDbType() {
      return this.syncDbType;
   }

   public DruidDataSource getDataSource() {
      return this.dataSource;
   }

   public List<String> getBlacklist() {
      return this.blacklist;
   }

   public List<String> getLookTableList() {
      return this.lookTableList;
   }

   public List<IEventHandler> getEventHandlerList() {
      return this.eventHandlerList;
   }

   public List<ISqlHandler> getSqlHandlersList() {
      return this.sqlHandlersList;
   }

   public void setDbKey(String dbKey) {
      this.dbKey = dbKey;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public void setPort(Integer port) {
      this.port = port;
   }

   public void setDatabase(String database) {
      this.database = database;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setSyncDbType(DbType syncDbType) {
      this.syncDbType = syncDbType;
   }

   public void setDataSource(DruidDataSource dataSource) {
      this.dataSource = dataSource;
   }

   public void setBlacklist(List<String> blacklist) {
      this.blacklist = blacklist;
   }

   public void setLookTableList(List<String> lookTableList) {
      this.lookTableList = lookTableList;
   }

   public void setEventHandlerList(List<IEventHandler> eventHandlerList) {
      this.eventHandlerList = eventHandlerList;
   }

   public void setSqlHandlersList(List<ISqlHandler> sqlHandlersList) {
      this.sqlHandlersList = sqlHandlersList;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SyncConfig)) {
         return false;
      } else {
         SyncConfig other = (SyncConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label155: {
               Object this$port = this.getPort();
               Object other$port = other.getPort();
               if (this$port == null) {
                  if (other$port == null) {
                     break label155;
                  }
               } else if (this$port.equals(other$port)) {
                  break label155;
               }

               return false;
            }

            Object this$dbKey = this.getDbKey();
            Object other$dbKey = other.getDbKey();
            if (this$dbKey == null) {
               if (other$dbKey != null) {
                  return false;
               }
            } else if (!this$dbKey.equals(other$dbKey)) {
               return false;
            }

            Object this$host = this.getHost();
            Object other$host = other.getHost();
            if (this$host == null) {
               if (other$host != null) {
                  return false;
               }
            } else if (!this$host.equals(other$host)) {
               return false;
            }

            label134: {
               Object this$database = this.getDatabase();
               Object other$database = other.getDatabase();
               if (this$database == null) {
                  if (other$database == null) {
                     break label134;
                  }
               } else if (this$database.equals(other$database)) {
                  break label134;
               }

               return false;
            }

            label127: {
               Object this$userName = this.getUserName();
               Object other$userName = other.getUserName();
               if (this$userName == null) {
                  if (other$userName == null) {
                     break label127;
                  }
               } else if (this$userName.equals(other$userName)) {
                  break label127;
               }

               return false;
            }

            label120: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label120;
                  }
               } else if (this$password.equals(other$password)) {
                  break label120;
               }

               return false;
            }

            Object this$syncDbType = this.getSyncDbType();
            Object other$syncDbType = other.getSyncDbType();
            if (this$syncDbType == null) {
               if (other$syncDbType != null) {
                  return false;
               }
            } else if (!this$syncDbType.equals(other$syncDbType)) {
               return false;
            }

            label106: {
               Object this$dataSource = this.getDataSource();
               Object other$dataSource = other.getDataSource();
               if (this$dataSource == null) {
                  if (other$dataSource == null) {
                     break label106;
                  }
               } else if (this$dataSource.equals(other$dataSource)) {
                  break label106;
               }

               return false;
            }

            Object this$blacklist = this.getBlacklist();
            Object other$blacklist = other.getBlacklist();
            if (this$blacklist == null) {
               if (other$blacklist != null) {
                  return false;
               }
            } else if (!this$blacklist.equals(other$blacklist)) {
               return false;
            }

            label92: {
               Object this$lookTableList = this.getLookTableList();
               Object other$lookTableList = other.getLookTableList();
               if (this$lookTableList == null) {
                  if (other$lookTableList == null) {
                     break label92;
                  }
               } else if (this$lookTableList.equals(other$lookTableList)) {
                  break label92;
               }

               return false;
            }

            Object this$eventHandlerList = this.getEventHandlerList();
            Object other$eventHandlerList = other.getEventHandlerList();
            if (this$eventHandlerList == null) {
               if (other$eventHandlerList != null) {
                  return false;
               }
            } else if (!this$eventHandlerList.equals(other$eventHandlerList)) {
               return false;
            }

            Object this$sqlHandlersList = this.getSqlHandlersList();
            Object other$sqlHandlersList = other.getSqlHandlersList();
            if (this$sqlHandlersList == null) {
               if (other$sqlHandlersList != null) {
                  return false;
               }
            } else if (!this$sqlHandlersList.equals(other$sqlHandlersList)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SyncConfig;
   }

   public int hashCode() {
      int result = 1;
      Object $port = this.getPort();
      result = result * 59 + ($port == null ? 43 : $port.hashCode());
      Object $dbKey = this.getDbKey();
      result = result * 59 + ($dbKey == null ? 43 : $dbKey.hashCode());
      Object $host = this.getHost();
      result = result * 59 + ($host == null ? 43 : $host.hashCode());
      Object $database = this.getDatabase();
      result = result * 59 + ($database == null ? 43 : $database.hashCode());
      Object $userName = this.getUserName();
      result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $syncDbType = this.getSyncDbType();
      result = result * 59 + ($syncDbType == null ? 43 : $syncDbType.hashCode());
      Object $dataSource = this.getDataSource();
      result = result * 59 + ($dataSource == null ? 43 : $dataSource.hashCode());
      Object $blacklist = this.getBlacklist();
      result = result * 59 + ($blacklist == null ? 43 : $blacklist.hashCode());
      Object $lookTableList = this.getLookTableList();
      result = result * 59 + ($lookTableList == null ? 43 : $lookTableList.hashCode());
      Object $eventHandlerList = this.getEventHandlerList();
      result = result * 59 + ($eventHandlerList == null ? 43 : $eventHandlerList.hashCode());
      Object $sqlHandlersList = this.getSqlHandlersList();
      result = result * 59 + ($sqlHandlersList == null ? 43 : $sqlHandlersList.hashCode());
      return result;
   }

   public String toString() {
      return "SyncConfig(dbKey=" + this.getDbKey() + ", host=" + this.getHost() + ", port=" + this.getPort() + ", database=" + this.getDatabase() + ", userName=" + this.getUserName() + ", password=" + this.getPassword() + ", syncDbType=" + this.getSyncDbType() + ", dataSource=" + this.getDataSource() + ", blacklist=" + this.getBlacklist() + ", lookTableList=" + this.getLookTableList() + ", eventHandlerList=" + this.getEventHandlerList() + ", sqlHandlersList=" + this.getSqlHandlersList() + ")";
   }
}
