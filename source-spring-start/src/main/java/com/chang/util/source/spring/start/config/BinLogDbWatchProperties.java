package com.chang.util.source.spring.start.config;

import com.chang.binlog.common.DbType;

import java.util.List;

public class BinLogDbWatchProperties {
   private String host;
   private Integer port;
   private String userName;
   private String password;
   private String database;
   private DbType syncDbType;
   private List<String> lookTableList;
   private List<String> blacklist;

   public String getHost() {
      return this.host;
   }

   public Integer getPort() {
      return this.port;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassword() {
      return this.password;
   }

   public String getDatabase() {
      return this.database;
   }

   public DbType getSyncDbType() {
      return this.syncDbType;
   }

   public List<String> getLookTableList() {
      return this.lookTableList;
   }

   public List<String> getBlacklist() {
      return this.blacklist;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public void setPort(Integer port) {
      this.port = port;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setDatabase(String database) {
      this.database = database;
   }

   public void setSyncDbType(DbType syncDbType) {
      this.syncDbType = syncDbType;
   }

   public void setLookTableList(List<String> lookTableList) {
      this.lookTableList = lookTableList;
   }

   public void setBlacklist(List<String> blacklist) {
      this.blacklist = blacklist;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BinLogDbWatchProperties)) {
         return false;
      } else {
         BinLogDbWatchProperties other = (BinLogDbWatchProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label107: {
               Object this$port = this.getPort();
               Object other$port = other.getPort();
               if (this$port == null) {
                  if (other$port == null) {
                     break label107;
                  }
               } else if (this$port.equals(other$port)) {
                  break label107;
               }

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

            Object this$userName = this.getUserName();
            Object other$userName = other.getUserName();
            if (this$userName == null) {
               if (other$userName != null) {
                  return false;
               }
            } else if (!this$userName.equals(other$userName)) {
               return false;
            }

            label86: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label86;
                  }
               } else if (this$password.equals(other$password)) {
                  break label86;
               }

               return false;
            }

            label79: {
               Object this$database = this.getDatabase();
               Object other$database = other.getDatabase();
               if (this$database == null) {
                  if (other$database == null) {
                     break label79;
                  }
               } else if (this$database.equals(other$database)) {
                  break label79;
               }

               return false;
            }

            label72: {
               Object this$syncDbType = this.getSyncDbType();
               Object other$syncDbType = other.getSyncDbType();
               if (this$syncDbType == null) {
                  if (other$syncDbType == null) {
                     break label72;
                  }
               } else if (this$syncDbType.equals(other$syncDbType)) {
                  break label72;
               }

               return false;
            }

            Object this$lookTableList = this.getLookTableList();
            Object other$lookTableList = other.getLookTableList();
            if (this$lookTableList == null) {
               if (other$lookTableList != null) {
                  return false;
               }
            } else if (!this$lookTableList.equals(other$lookTableList)) {
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

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof BinLogDbWatchProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $port = this.getPort();
      result = result * 59 + ($port == null ? 43 : $port.hashCode());
      Object $host = this.getHost();
      result = result * 59 + ($host == null ? 43 : $host.hashCode());
      Object $userName = this.getUserName();
      result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $database = this.getDatabase();
      result = result * 59 + ($database == null ? 43 : $database.hashCode());
      Object $syncDbType = this.getSyncDbType();
      result = result * 59 + ($syncDbType == null ? 43 : $syncDbType.hashCode());
      Object $lookTableList = this.getLookTableList();
      result = result * 59 + ($lookTableList == null ? 43 : $lookTableList.hashCode());
      Object $blacklist = this.getBlacklist();
      result = result * 59 + ($blacklist == null ? 43 : $blacklist.hashCode());
      return result;
   }

   public String toString() {
      return "BinLogDbWatchProperties(host=" + this.getHost() + ", port=" + this.getPort() + ", userName=" + this.getUserName() + ", password=" + this.getPassword() + ", database=" + this.getDatabase() + ", syncDbType=" + this.getSyncDbType() + ", lookTableList=" + this.getLookTableList() + ", blacklist=" + this.getBlacklist() + ")";
   }
}
