package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class HttpInProperties {
   private String baseUrl;
   private String url;
   private String type;
   private Long time;
   private Map<String, Object> parm;
   private Map<String, Object> exParm = new HashMap();

   public String getBaseUrl() {
      return this.baseUrl;
   }

   public String getUrl() {
      return this.url;
   }

   public String getType() {
      return this.type;
   }

   public Long getTime() {
      return this.time;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public Map<String, Object> getExParm() {
      return this.exParm;
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

   public void setTime(Long time) {
      this.time = time;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public void setExParm(Map<String, Object> exParm) {
      this.exParm = exParm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HttpInProperties)) {
         return false;
      } else {
         HttpInProperties other = (HttpInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$time = this.getTime();
            Object other$time = other.getTime();
            if (this$time == null) {
               if (other$time != null) {
                  return false;
               }
            } else if (!this$time.equals(other$time)) {
               return false;
            }

            Object this$baseUrl = this.getBaseUrl();
            Object other$baseUrl = other.getBaseUrl();
            if (this$baseUrl == null) {
               if (other$baseUrl != null) {
                  return false;
               }
            } else if (!this$baseUrl.equals(other$baseUrl)) {
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

            label62: {
               Object this$type = this.getType();
               Object other$type = other.getType();
               if (this$type == null) {
                  if (other$type == null) {
                     break label62;
                  }
               } else if (this$type.equals(other$type)) {
                  break label62;
               }

               return false;
            }

            label55: {
               Object this$parm = this.getParm();
               Object other$parm = other.getParm();
               if (this$parm == null) {
                  if (other$parm == null) {
                     break label55;
                  }
               } else if (this$parm.equals(other$parm)) {
                  break label55;
               }

               return false;
            }

            Object this$exParm = this.getExParm();
            Object other$exParm = other.getExParm();
            if (this$exParm == null) {
               if (other$exParm != null) {
                  return false;
               }
            } else if (!this$exParm.equals(other$exParm)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HttpInProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      Object $baseUrl = this.getBaseUrl();
      result = result * 59 + ($baseUrl == null ? 43 : $baseUrl.hashCode());
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      Object $exParm = this.getExParm();
      result = result * 59 + ($exParm == null ? 43 : $exParm.hashCode());
      return result;
   }

   public String toString() {
      return "HttpInProperties(baseUrl=" + this.getBaseUrl() + ", url=" + this.getUrl() + ", type=" + this.getType() + ", time=" + this.getTime() + ", parm=" + this.getParm() + ", exParm=" + this.getExParm() + ")";
   }
}
