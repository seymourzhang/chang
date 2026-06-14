package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class HttpOutProperties {
   private String baseUrl;
   private String url;
   private String type;
   private Map<String, Object> parm = new HashMap();

   public String getBaseUrl() {
      return this.baseUrl;
   }

   public String getUrl() {
      return this.url;
   }

   public String getType() {
      return this.type;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HttpOutProperties)) {
         return false;
      } else {
         HttpOutProperties other = (HttpOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$baseUrl = this.getBaseUrl();
               Object other$baseUrl = other.getBaseUrl();
               if (this$baseUrl == null) {
                  if (other$baseUrl == null) {
                     break label59;
                  }
               } else if (this$baseUrl.equals(other$baseUrl)) {
                  break label59;
               }

               return false;
            }

            Object this$url = this.getUrl();
            Object other$url = other.getUrl();
            if (this$url == null) {
               if (other$url != null) {
                  return false;
               }
            } else if (!this$url.equals(other$url)) {
               return false;
            }

            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type != null) {
                  return false;
               }
            } else if (!this$type.equals(other$type)) {
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
      return other instanceof HttpOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $baseUrl = this.getBaseUrl();
      result = result * 59 + ($baseUrl == null ? 43 : $baseUrl.hashCode());
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "HttpOutProperties(baseUrl=" + this.getBaseUrl() + ", url=" + this.getUrl() + ", type=" + this.getType() + ", parm=" + this.getParm() + ")";
   }
}
