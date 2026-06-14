package com.chang.util.source.spring.start.config;


import com.chang.util.source.common.TaskModeType;
import com.chang.util.source.mongodb.Condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongodbInProperties {
   private String url;
   private String time;
   private TaskModeType taskModeType;
   private String databaseName;
   private String collectionName;
   private List<Condition> conditions;
   private Map<String, Object> parm = new HashMap();

   public String getUrl() {
      return this.url;
   }

   public String getTime() {
      return this.time;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public String getDatabaseName() {
      return this.databaseName;
   }

   public String getCollectionName() {
      return this.collectionName;
   }

   public List<Condition> getConditions() {
      return this.conditions;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public void setTaskModeType(TaskModeType taskModeType) {
      this.taskModeType = taskModeType;
   }

   public void setDatabaseName(String databaseName) {
      this.databaseName = databaseName;
   }

   public void setCollectionName(String collectionName) {
      this.collectionName = collectionName;
   }

   public void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MongodbInProperties)) {
         return false;
      } else {
         MongodbInProperties other = (MongodbInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label95: {
               Object this$url = this.getUrl();
               Object other$url = other.getUrl();
               if (this$url == null) {
                  if (other$url == null) {
                     break label95;
                  }
               } else if (this$url.equals(other$url)) {
                  break label95;
               }

               return false;
            }

            Object this$time = this.getTime();
            Object other$time = other.getTime();
            if (this$time == null) {
               if (other$time != null) {
                  return false;
               }
            } else if (!this$time.equals(other$time)) {
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

            label74: {
               Object this$databaseName = this.getDatabaseName();
               Object other$databaseName = other.getDatabaseName();
               if (this$databaseName == null) {
                  if (other$databaseName == null) {
                     break label74;
                  }
               } else if (this$databaseName.equals(other$databaseName)) {
                  break label74;
               }

               return false;
            }

            label67: {
               Object this$collectionName = this.getCollectionName();
               Object other$collectionName = other.getCollectionName();
               if (this$collectionName == null) {
                  if (other$collectionName == null) {
                     break label67;
                  }
               } else if (this$collectionName.equals(other$collectionName)) {
                  break label67;
               }

               return false;
            }

            Object this$conditions = this.getConditions();
            Object other$conditions = other.getConditions();
            if (this$conditions == null) {
               if (other$conditions != null) {
                  return false;
               }
            } else if (!this$conditions.equals(other$conditions)) {
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
      return other instanceof MongodbInProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      Object $taskModeType = this.getTaskModeType();
      result = result * 59 + ($taskModeType == null ? 43 : $taskModeType.hashCode());
      Object $databaseName = this.getDatabaseName();
      result = result * 59 + ($databaseName == null ? 43 : $databaseName.hashCode());
      Object $collectionName = this.getCollectionName();
      result = result * 59 + ($collectionName == null ? 43 : $collectionName.hashCode());
      Object $conditions = this.getConditions();
      result = result * 59 + ($conditions == null ? 43 : $conditions.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "MongodbInProperties(url=" + this.getUrl() + ", time=" + this.getTime() + ", taskModeType=" + this.getTaskModeType() + ", databaseName=" + this.getDatabaseName() + ", collectionName=" + this.getCollectionName() + ", conditions=" + this.getConditions() + ", parm=" + this.getParm() + ")";
   }
}
