package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServerProperties {
   private int port;
   private String messageType = "str";
   private String socketApiBeanName;
   private Map<String, Object> parm = new HashMap();

   public int getPort() {
      return this.port;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public String getSocketApiBeanName() {
      return this.socketApiBeanName;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setSocketApiBeanName(String socketApiBeanName) {
      this.socketApiBeanName = socketApiBeanName;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WebSocketServerProperties)) {
         return false;
      } else {
         WebSocketServerProperties other = (WebSocketServerProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPort() != other.getPort()) {
            return false;
         } else {
            label49: {
               Object this$messageType = this.getMessageType();
               Object other$messageType = other.getMessageType();
               if (this$messageType == null) {
                  if (other$messageType == null) {
                     break label49;
                  }
               } else if (this$messageType.equals(other$messageType)) {
                  break label49;
               }

               return false;
            }

            Object this$socketApiBeanName = this.getSocketApiBeanName();
            Object other$socketApiBeanName = other.getSocketApiBeanName();
            if (this$socketApiBeanName == null) {
               if (other$socketApiBeanName != null) {
                  return false;
               }
            } else if (!this$socketApiBeanName.equals(other$socketApiBeanName)) {
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
      return other instanceof WebSocketServerProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getPort();
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $socketApiBeanName = this.getSocketApiBeanName();
      result = result * 59 + ($socketApiBeanName == null ? 43 : $socketApiBeanName.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "WebSocketServerProperties(port=" + this.getPort() + ", messageType=" + this.getMessageType() + ", socketApiBeanName=" + this.getSocketApiBeanName() + ", parm=" + this.getParm() + ")";
   }
}
