package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class WebSocketClientProperties {
   private String url;
   private String messageType = "str";
   private Map<String, Object> parm = new HashMap();

   public String getUrl() {
      return this.url;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WebSocketClientProperties)) {
         return false;
      } else {
         WebSocketClientProperties other = (WebSocketClientProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$url = this.getUrl();
               Object other$url = other.getUrl();
               if (this$url == null) {
                  if (other$url == null) {
                     break label47;
                  }
               } else if (this$url.equals(other$url)) {
                  break label47;
               }

               return false;
            }

            Object this$messageType = this.getMessageType();
            Object other$messageType = other.getMessageType();
            if (this$messageType == null) {
               if (other$messageType != null) {
                  return false;
               }
            } else if (!this$messageType.equals(other$messageType)) {
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
      return other instanceof WebSocketClientProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "WebSocketClientProperties(url=" + this.getUrl() + ", messageType=" + this.getMessageType() + ", parm=" + this.getParm() + ")";
   }
}
