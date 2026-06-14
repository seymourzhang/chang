package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class MongodbOutProperties {
   private String url;
   private String databaseName;
   private String collectionName;
   private Map<String, Object> parm = new HashMap();

   public String getUrl() {
      return this.url;
   }

   public String getDatabaseName() {
      return this.databaseName;
   }

   public String getCollectionName() {
      return this.collectionName;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setDatabaseName(String databaseName) {
      this.databaseName = databaseName;
   }

   public void setCollectionName(String collectionName) {
      this.collectionName = collectionName;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MongodbOutProperties)) {
         return false;
      } else {
         MongodbOutProperties other = (MongodbOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$url = this.getUrl();
               Object other$url = other.getUrl();
               if (this$url == null) {
                  if (other$url == null) {
                     break label59;
                  }
               } else if (this$url.equals(other$url)) {
                  break label59;
               }

               return false;
            }

            Object this$databaseName = this.getDatabaseName();
            Object other$databaseName = other.getDatabaseName();
            if (this$databaseName == null) {
               if (other$databaseName != null) {
                  return false;
               }
            } else if (!this$databaseName.equals(other$databaseName)) {
               return false;
            }

            Object this$collectionName = this.getCollectionName();
            Object other$collectionName = other.getCollectionName();
            if (this$collectionName == null) {
               if (other$collectionName != null) {
                  return false;
               }
            } else if (!this$collectionName.equals(other$collectionName)) {
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
      return other instanceof MongodbOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      Object $databaseName = this.getDatabaseName();
      result = result * 59 + ($databaseName == null ? 43 : $databaseName.hashCode());
      Object $collectionName = this.getCollectionName();
      result = result * 59 + ($collectionName == null ? 43 : $collectionName.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "MongodbOutProperties(url=" + this.getUrl() + ", databaseName=" + this.getDatabaseName() + ", collectionName=" + this.getCollectionName() + ", parm=" + this.getParm() + ")";
   }
}
