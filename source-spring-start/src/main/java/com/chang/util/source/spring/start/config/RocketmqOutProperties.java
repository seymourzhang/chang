package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class RocketmqOutProperties {
   private String producerGroup;
   private String topic;
   private String nameSrvAddr;
   private Integer sendMsgTimeout;
   private String accessKey;
   private String secretKey;
   private String namespace;
   private String messageType = "str";
   private Map<String, Object> parm = new HashMap();

   public String getProducerGroup() {
      return this.producerGroup;
   }

   public String getTopic() {
      return this.topic;
   }

   public String getNameSrvAddr() {
      return this.nameSrvAddr;
   }

   public Integer getSendMsgTimeout() {
      return this.sendMsgTimeout;
   }

   public String getAccessKey() {
      return this.accessKey;
   }

   public String getSecretKey() {
      return this.secretKey;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setProducerGroup(String producerGroup) {
      this.producerGroup = producerGroup;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setNameSrvAddr(String nameSrvAddr) {
      this.nameSrvAddr = nameSrvAddr;
   }

   public void setSendMsgTimeout(Integer sendMsgTimeout) {
      this.sendMsgTimeout = sendMsgTimeout;
   }

   public void setAccessKey(String accessKey) {
      this.accessKey = accessKey;
   }

   public void setSecretKey(String secretKey) {
      this.secretKey = secretKey;
   }

   public void setNamespace(String namespace) {
      this.namespace = namespace;
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
      } else if (!(o instanceof RocketmqOutProperties)) {
         return false;
      } else {
         RocketmqOutProperties other = (RocketmqOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label119: {
               Object this$sendMsgTimeout = this.getSendMsgTimeout();
               Object other$sendMsgTimeout = other.getSendMsgTimeout();
               if (this$sendMsgTimeout == null) {
                  if (other$sendMsgTimeout == null) {
                     break label119;
                  }
               } else if (this$sendMsgTimeout.equals(other$sendMsgTimeout)) {
                  break label119;
               }

               return false;
            }

            Object this$producerGroup = this.getProducerGroup();
            Object other$producerGroup = other.getProducerGroup();
            if (this$producerGroup == null) {
               if (other$producerGroup != null) {
                  return false;
               }
            } else if (!this$producerGroup.equals(other$producerGroup)) {
               return false;
            }

            label105: {
               Object this$topic = this.getTopic();
               Object other$topic = other.getTopic();
               if (this$topic == null) {
                  if (other$topic == null) {
                     break label105;
                  }
               } else if (this$topic.equals(other$topic)) {
                  break label105;
               }

               return false;
            }

            Object this$nameSrvAddr = this.getNameSrvAddr();
            Object other$nameSrvAddr = other.getNameSrvAddr();
            if (this$nameSrvAddr == null) {
               if (other$nameSrvAddr != null) {
                  return false;
               }
            } else if (!this$nameSrvAddr.equals(other$nameSrvAddr)) {
               return false;
            }

            label91: {
               Object this$accessKey = this.getAccessKey();
               Object other$accessKey = other.getAccessKey();
               if (this$accessKey == null) {
                  if (other$accessKey == null) {
                     break label91;
                  }
               } else if (this$accessKey.equals(other$accessKey)) {
                  break label91;
               }

               return false;
            }

            Object this$secretKey = this.getSecretKey();
            Object other$secretKey = other.getSecretKey();
            if (this$secretKey == null) {
               if (other$secretKey != null) {
                  return false;
               }
            } else if (!this$secretKey.equals(other$secretKey)) {
               return false;
            }

            label77: {
               Object this$namespace = this.getNamespace();
               Object other$namespace = other.getNamespace();
               if (this$namespace == null) {
                  if (other$namespace == null) {
                     break label77;
                  }
               } else if (this$namespace.equals(other$namespace)) {
                  break label77;
               }

               return false;
            }

            label70: {
               Object this$messageType = this.getMessageType();
               Object other$messageType = other.getMessageType();
               if (this$messageType == null) {
                  if (other$messageType == null) {
                     break label70;
                  }
               } else if (this$messageType.equals(other$messageType)) {
                  break label70;
               }

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
      return other instanceof RocketmqOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $sendMsgTimeout = this.getSendMsgTimeout();
      result = result * 59 + ($sendMsgTimeout == null ? 43 : $sendMsgTimeout.hashCode());
      Object $producerGroup = this.getProducerGroup();
      result = result * 59 + ($producerGroup == null ? 43 : $producerGroup.hashCode());
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      Object $nameSrvAddr = this.getNameSrvAddr();
      result = result * 59 + ($nameSrvAddr == null ? 43 : $nameSrvAddr.hashCode());
      Object $accessKey = this.getAccessKey();
      result = result * 59 + ($accessKey == null ? 43 : $accessKey.hashCode());
      Object $secretKey = this.getSecretKey();
      result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
      Object $namespace = this.getNamespace();
      result = result * 59 + ($namespace == null ? 43 : $namespace.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "RocketmqOutProperties(producerGroup=" + this.getProducerGroup() + ", topic=" + this.getTopic() + ", nameSrvAddr=" + this.getNameSrvAddr() + ", sendMsgTimeout=" + this.getSendMsgTimeout() + ", accessKey=" + this.getAccessKey() + ", secretKey=" + this.getSecretKey() + ", namespace=" + this.getNamespace() + ", messageType=" + this.getMessageType() + ", parm=" + this.getParm() + ")";
   }
}
