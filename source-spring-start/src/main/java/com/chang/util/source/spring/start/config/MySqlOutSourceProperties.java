package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class MySqlOutSourceProperties {
   private String tableName;
   private String excludeFiled;
   private String dbConfigName;
   private Map<String, Object> parm = new HashMap();

   public String getTableName() {
      return this.tableName;
   }

   public String getExcludeFiled() {
      return this.excludeFiled;
   }

   public String getDbConfigName() {
      return this.dbConfigName;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public void setExcludeFiled(String excludeFiled) {
      this.excludeFiled = excludeFiled;
   }

   public void setDbConfigName(String dbConfigName) {
      this.dbConfigName = dbConfigName;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MySqlOutSourceProperties)) {
         return false;
      } else {
         MySqlOutSourceProperties other = (MySqlOutSourceProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$tableName = this.getTableName();
               Object other$tableName = other.getTableName();
               if (this$tableName == null) {
                  if (other$tableName == null) {
                     break label59;
                  }
               } else if (this$tableName.equals(other$tableName)) {
                  break label59;
               }

               return false;
            }

            Object this$excludeFiled = this.getExcludeFiled();
            Object other$excludeFiled = other.getExcludeFiled();
            if (this$excludeFiled == null) {
               if (other$excludeFiled != null) {
                  return false;
               }
            } else if (!this$excludeFiled.equals(other$excludeFiled)) {
               return false;
            }

            Object this$dbConfigName = this.getDbConfigName();
            Object other$dbConfigName = other.getDbConfigName();
            if (this$dbConfigName == null) {
               if (other$dbConfigName != null) {
                  return false;
               }
            } else if (!this$dbConfigName.equals(other$dbConfigName)) {
               return false;
            }

            Object this$parm = this.getParm();
            Object other$parm = other.getParm();
            if (this$parm == null) {
               if (other$parm != null) {
                  return false;
               }
            } else if (!this$parm.equals(other$parm)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MySqlOutSourceProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $tableName = this.getTableName();
      result = result * 59 + ($tableName == null ? 43 : $tableName.hashCode());
      Object $excludeFiled = this.getExcludeFiled();
      result = result * 59 + ($excludeFiled == null ? 43 : $excludeFiled.hashCode());
      Object $dbConfigName = this.getDbConfigName();
      result = result * 59 + ($dbConfigName == null ? 43 : $dbConfigName.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "MySqlOutSourceProperties(tableName=" + this.getTableName() + ", excludeFiled=" + this.getExcludeFiled() + ", dbConfigName=" + this.getDbConfigName() + ", parm=" + this.getParm() + ")";
   }
}
