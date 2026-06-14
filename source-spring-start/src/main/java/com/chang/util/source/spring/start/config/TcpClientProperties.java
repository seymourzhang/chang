package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class TcpClientProperties {
   private String hostname;
   private int port;
   private String groupName;
   private String messageType = "str";
   private Map<String, Object> parm = new HashMap();
   private Integer nThreads = 8;
   private Integer maximumQueueSize = 1024;

   public String getHostname() {
      return this.hostname;
   }

   public int getPort() {
      return this.port;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public Integer getNThreads() {
      return this.nThreads;
   }

   public Integer getMaximumQueueSize() {
      return this.maximumQueueSize;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public void setNThreads(Integer nThreads) {
      this.nThreads = nThreads;
   }

   public void setMaximumQueueSize(Integer maximumQueueSize) {
      this.maximumQueueSize = maximumQueueSize;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TcpClientProperties)) {
         return false;
      } else {
         TcpClientProperties other = (TcpClientProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPort() != other.getPort()) {
            return false;
         } else {
            Object this$nThreads = this.getNThreads();
            Object other$nThreads = other.getNThreads();
            if (this$nThreads == null) {
               if (other$nThreads != null) {
                  return false;
               }
            } else if (!this$nThreads.equals(other$nThreads)) {
               return false;
            }

            Object this$maximumQueueSize = this.getMaximumQueueSize();
            Object other$maximumQueueSize = other.getMaximumQueueSize();
            if (this$maximumQueueSize == null) {
               if (other$maximumQueueSize != null) {
                  return false;
               }
            } else if (!this$maximumQueueSize.equals(other$maximumQueueSize)) {
               return false;
            }

            label71: {
               Object this$hostname = this.getHostname();
               Object other$hostname = other.getHostname();
               if (this$hostname == null) {
                  if (other$hostname == null) {
                     break label71;
                  }
               } else if (this$hostname.equals(other$hostname)) {
                  break label71;
               }

               return false;
            }

            label64: {
               Object this$groupName = this.getGroupName();
               Object other$groupName = other.getGroupName();
               if (this$groupName == null) {
                  if (other$groupName == null) {
                     break label64;
                  }
               } else if (this$groupName.equals(other$groupName)) {
                  break label64;
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
      return other instanceof TcpClientProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getPort();
      Object $nThreads = this.getNThreads();
      result = result * 59 + ($nThreads == null ? 43 : $nThreads.hashCode());
      Object $maximumQueueSize = this.getMaximumQueueSize();
      result = result * 59 + ($maximumQueueSize == null ? 43 : $maximumQueueSize.hashCode());
      Object $hostname = this.getHostname();
      result = result * 59 + ($hostname == null ? 43 : $hostname.hashCode());
      Object $groupName = this.getGroupName();
      result = result * 59 + ($groupName == null ? 43 : $groupName.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "TcpClientProperties(hostname=" + this.getHostname() + ", port=" + this.getPort() + ", groupName=" + this.getGroupName() + ", messageType=" + this.getMessageType() + ", parm=" + this.getParm() + ", nThreads=" + this.getNThreads() + ", maximumQueueSize=" + this.getMaximumQueueSize() + ")";
   }
}
