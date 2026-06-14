package com.chang.util.source.spring.start.config;

import com.chang.util.source.tcpServer.SendMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TcpServerProperties {
   private String hostname;
   private int port;
   private List<SendMode> groupNamesMode;
   private String messageType = "str";
   private Map<String, Object> parm = new HashMap();

   public String getHostname() {
      return this.hostname;
   }

   public int getPort() {
      return this.port;
   }

   public List<SendMode> getGroupNamesMode() {
      return this.groupNamesMode;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setGroupNamesMode(List<SendMode> groupNamesMode) {
      this.groupNamesMode = groupNamesMode;
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
      } else if (!(o instanceof TcpServerProperties)) {
         return false;
      } else {
         TcpServerProperties other = (TcpServerProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPort() != other.getPort()) {
            return false;
         } else {
            label61: {
               Object this$hostname = this.getHostname();
               Object other$hostname = other.getHostname();
               if (this$hostname == null) {
                  if (other$hostname == null) {
                     break label61;
                  }
               } else if (this$hostname.equals(other$hostname)) {
                  break label61;
               }

               return false;
            }

            label54: {
               Object this$groupNamesMode = this.getGroupNamesMode();
               Object other$groupNamesMode = other.getGroupNamesMode();
               if (this$groupNamesMode == null) {
                  if (other$groupNamesMode == null) {
                     break label54;
                  }
               } else if (this$groupNamesMode.equals(other$groupNamesMode)) {
                  break label54;
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
      return other instanceof TcpServerProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getPort();
      Object $hostname = this.getHostname();
      result = result * 59 + ($hostname == null ? 43 : $hostname.hashCode());
      Object $groupNamesMode = this.getGroupNamesMode();
      result = result * 59 + ($groupNamesMode == null ? 43 : $groupNamesMode.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "TcpServerProperties(hostname=" + this.getHostname() + ", port=" + this.getPort() + ", groupNamesMode=" + this.getGroupNamesMode() + ", messageType=" + this.getMessageType() + ", parm=" + this.getParm() + ")";
   }
}
