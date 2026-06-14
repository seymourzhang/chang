package com.chang.util.source.spring.start.config;

public class SqliteProperties {
   private String url;

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SqliteProperties)) {
         return false;
      } else {
         SqliteProperties other = (SqliteProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$url = this.getUrl();
            Object other$url = other.getUrl();
            if (this$url == null) {
               if (other$url != null) {
                  return false;
               }
            } else if (!this$url.equals(other$url)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SqliteProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      return result;
   }

   public String toString() {
      return "SqliteProperties(url=" + this.getUrl() + ")";
   }
}
