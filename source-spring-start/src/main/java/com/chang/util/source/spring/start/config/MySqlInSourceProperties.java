package com.chang.util.source.spring.start.config;


import com.chang.util.source.common.TaskModeType;

public class MySqlInSourceProperties {
   private String sql;
   private TaskModeType taskModeType;
   private String time;
   private String excludeFiled;
   private String dbConfigName;

   public String getSql() {
      return this.sql;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public String getTime() {
      return this.time;
   }

   public String getExcludeFiled() {
      return this.excludeFiled;
   }

   public String getDbConfigName() {
      return this.dbConfigName;
   }

   public void setSql(String sql) {
      this.sql = sql;
   }

   public void setTaskModeType(TaskModeType taskModeType) {
      this.taskModeType = taskModeType;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public void setExcludeFiled(String excludeFiled) {
      this.excludeFiled = excludeFiled;
   }

   public void setDbConfigName(String dbConfigName) {
      this.dbConfigName = dbConfigName;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MySqlInSourceProperties)) {
         return false;
      } else {
         MySqlInSourceProperties other = (MySqlInSourceProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label71: {
               Object this$sql = this.getSql();
               Object other$sql = other.getSql();
               if (this$sql == null) {
                  if (other$sql == null) {
                     break label71;
                  }
               } else if (this$sql.equals(other$sql)) {
                  break label71;
               }

               return false;
            }

            Object this$taskModeType = this.getTaskModeType();
            Object other$taskModeType = other.getTaskModeType();
            if (this$taskModeType == null) {
               if (other$taskModeType != null) {
                  return false;
               }
            } else if (!this$taskModeType.equals(other$taskModeType)) {
               return false;
            }

            label57: {
               Object this$time = this.getTime();
               Object other$time = other.getTime();
               if (this$time == null) {
                  if (other$time == null) {
                     break label57;
                  }
               } else if (this$time.equals(other$time)) {
                  break label57;
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
               if (other$dbConfigName == null) {
                  return true;
               }
            } else if (this$dbConfigName.equals(other$dbConfigName)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MySqlInSourceProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $sql = this.getSql();
      result = result * 59 + ($sql == null ? 43 : $sql.hashCode());
      Object $taskModeType = this.getTaskModeType();
      result = result * 59 + ($taskModeType == null ? 43 : $taskModeType.hashCode());
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      Object $excludeFiled = this.getExcludeFiled();
      result = result * 59 + ($excludeFiled == null ? 43 : $excludeFiled.hashCode());
      Object $dbConfigName = this.getDbConfigName();
      result = result * 59 + ($dbConfigName == null ? 43 : $dbConfigName.hashCode());
      return result;
   }

   public String toString() {
      return "MySqlInSourceProperties(sql=" + this.getSql() + ", taskModeType=" + this.getTaskModeType() + ", time=" + this.getTime() + ", excludeFiled=" + this.getExcludeFiled() + ", dbConfigName=" + this.getDbConfigName() + ")";
   }
}
