package com.chang.util.source.spring.start.config;


import com.chang.util.source.rocketmq.ConsumerMessageType;

import java.util.HashMap;
import java.util.Map;

public class RocketmqInProperties {
   private String consumerGroup;
   private String nameSrvAddr;
   private String topic;
   private String subExpression;
   private String messageModel;
   private String accessKey;
   private String secretKey;
   private String namespace;
   private Integer messageBatchMaxSize = 3;
   private Integer consumeThread = 10;
   private String messageType = "str";
   private ConsumerMessageType consumerMessageType;
   private String redisKey;
   private Boolean isSingle;
   private Map<String, Object> parm;

   public RocketmqInProperties() {
      this.consumerMessageType = ConsumerMessageType.CONCURRENTLY;
      this.isSingle = false;
      this.parm = new HashMap();
   }

   public String getConsumerGroup() {
      return this.consumerGroup;
   }

   public String getNameSrvAddr() {
      return this.nameSrvAddr;
   }

   public String getTopic() {
      return this.topic;
   }

   public String getSubExpression() {
      return this.subExpression;
   }

   public String getMessageModel() {
      return this.messageModel;
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

   public Integer getMessageBatchMaxSize() {
      return this.messageBatchMaxSize;
   }

   public Integer getConsumeThread() {
      return this.consumeThread;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public ConsumerMessageType getConsumerMessageType() {
      return this.consumerMessageType;
   }

   public String getRedisKey() {
      return this.redisKey;
   }

   public Boolean getIsSingle() {
      return this.isSingle;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setConsumerGroup(String consumerGroup) {
      this.consumerGroup = consumerGroup;
   }

   public void setNameSrvAddr(String nameSrvAddr) {
      this.nameSrvAddr = nameSrvAddr;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setSubExpression(String subExpression) {
      this.subExpression = subExpression;
   }

   public void setMessageModel(String messageModel) {
      this.messageModel = messageModel;
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

   public void setMessageBatchMaxSize(Integer messageBatchMaxSize) {
      this.messageBatchMaxSize = messageBatchMaxSize;
   }

   public void setConsumeThread(Integer consumeThread) {
      this.consumeThread = consumeThread;
   }

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setConsumerMessageType(ConsumerMessageType consumerMessageType) {
      this.consumerMessageType = consumerMessageType;
   }

   public void setRedisKey(String redisKey) {
      this.redisKey = redisKey;
   }

   public void setIsSingle(Boolean isSingle) {
      this.isSingle = isSingle;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RocketmqInProperties)) {
         return false;
      } else {
         RocketmqInProperties other = (RocketmqInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label191: {
               Object this$messageBatchMaxSize = this.getMessageBatchMaxSize();
               Object other$messageBatchMaxSize = other.getMessageBatchMaxSize();
               if (this$messageBatchMaxSize == null) {
                  if (other$messageBatchMaxSize == null) {
                     break label191;
                  }
               } else if (this$messageBatchMaxSize.equals(other$messageBatchMaxSize)) {
                  break label191;
               }

               return false;
            }

            Object this$consumeThread = this.getConsumeThread();
            Object other$consumeThread = other.getConsumeThread();
            if (this$consumeThread == null) {
               if (other$consumeThread != null) {
                  return false;
               }
            } else if (!this$consumeThread.equals(other$consumeThread)) {
               return false;
            }

            Object this$isSingle = this.getIsSingle();
            Object other$isSingle = other.getIsSingle();
            if (this$isSingle == null) {
               if (other$isSingle != null) {
                  return false;
               }
            } else if (!this$isSingle.equals(other$isSingle)) {
               return false;
            }

            label170: {
               Object this$consumerGroup = this.getConsumerGroup();
               Object other$consumerGroup = other.getConsumerGroup();
               if (this$consumerGroup == null) {
                  if (other$consumerGroup == null) {
                     break label170;
                  }
               } else if (this$consumerGroup.equals(other$consumerGroup)) {
                  break label170;
               }

               return false;
            }

            label163: {
               Object this$nameSrvAddr = this.getNameSrvAddr();
               Object other$nameSrvAddr = other.getNameSrvAddr();
               if (this$nameSrvAddr == null) {
                  if (other$nameSrvAddr == null) {
                     break label163;
                  }
               } else if (this$nameSrvAddr.equals(other$nameSrvAddr)) {
                  break label163;
               }

               return false;
            }

            Object this$topic = this.getTopic();
            Object other$topic = other.getTopic();
            if (this$topic == null) {
               if (other$topic != null) {
                  return false;
               }
            } else if (!this$topic.equals(other$topic)) {
               return false;
            }

            Object this$subExpression = this.getSubExpression();
            Object other$subExpression = other.getSubExpression();
            if (this$subExpression == null) {
               if (other$subExpression != null) {
                  return false;
               }
            } else if (!this$subExpression.equals(other$subExpression)) {
               return false;
            }

            label142: {
               Object this$messageModel = this.getMessageModel();
               Object other$messageModel = other.getMessageModel();
               if (this$messageModel == null) {
                  if (other$messageModel == null) {
                     break label142;
                  }
               } else if (this$messageModel.equals(other$messageModel)) {
                  break label142;
               }

               return false;
            }

            label135: {
               Object this$accessKey = this.getAccessKey();
               Object other$accessKey = other.getAccessKey();
               if (this$accessKey == null) {
                  if (other$accessKey == null) {
                     break label135;
                  }
               } else if (this$accessKey.equals(other$accessKey)) {
                  break label135;
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

            label121: {
               Object this$namespace = this.getNamespace();
               Object other$namespace = other.getNamespace();
               if (this$namespace == null) {
                  if (other$namespace == null) {
                     break label121;
                  }
               } else if (this$namespace.equals(other$namespace)) {
                  break label121;
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

            label107: {
               Object this$consumerMessageType = this.getConsumerMessageType();
               Object other$consumerMessageType = other.getConsumerMessageType();
               if (this$consumerMessageType == null) {
                  if (other$consumerMessageType == null) {
                     break label107;
                  }
               } else if (this$consumerMessageType.equals(other$consumerMessageType)) {
                  break label107;
               }

               return false;
            }

            Object this$redisKey = this.getRedisKey();
            Object other$redisKey = other.getRedisKey();
            if (this$redisKey == null) {
               if (other$redisKey != null) {
                  return false;
               }
            } else if (!this$redisKey.equals(other$redisKey)) {
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
      return other instanceof RocketmqInProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $messageBatchMaxSize = this.getMessageBatchMaxSize();
      result = result * 59 + ($messageBatchMaxSize == null ? 43 : $messageBatchMaxSize.hashCode());
      Object $consumeThread = this.getConsumeThread();
      result = result * 59 + ($consumeThread == null ? 43 : $consumeThread.hashCode());
      Object $isSingle = this.getIsSingle();
      result = result * 59 + ($isSingle == null ? 43 : $isSingle.hashCode());
      Object $consumerGroup = this.getConsumerGroup();
      result = result * 59 + ($consumerGroup == null ? 43 : $consumerGroup.hashCode());
      Object $nameSrvAddr = this.getNameSrvAddr();
      result = result * 59 + ($nameSrvAddr == null ? 43 : $nameSrvAddr.hashCode());
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      Object $subExpression = this.getSubExpression();
      result = result * 59 + ($subExpression == null ? 43 : $subExpression.hashCode());
      Object $messageModel = this.getMessageModel();
      result = result * 59 + ($messageModel == null ? 43 : $messageModel.hashCode());
      Object $accessKey = this.getAccessKey();
      result = result * 59 + ($accessKey == null ? 43 : $accessKey.hashCode());
      Object $secretKey = this.getSecretKey();
      result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
      Object $namespace = this.getNamespace();
      result = result * 59 + ($namespace == null ? 43 : $namespace.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $consumerMessageType = this.getConsumerMessageType();
      result = result * 59 + ($consumerMessageType == null ? 43 : $consumerMessageType.hashCode());
      Object $redisKey = this.getRedisKey();
      result = result * 59 + ($redisKey == null ? 43 : $redisKey.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "RocketmqInProperties(consumerGroup=" + this.getConsumerGroup() + ", nameSrvAddr=" + this.getNameSrvAddr() + ", topic=" + this.getTopic() + ", subExpression=" + this.getSubExpression() + ", messageModel=" + this.getMessageModel() + ", accessKey=" + this.getAccessKey() + ", secretKey=" + this.getSecretKey() + ", namespace=" + this.getNamespace() + ", messageBatchMaxSize=" + this.getMessageBatchMaxSize() + ", consumeThread=" + this.getConsumeThread() + ", messageType=" + this.getMessageType() + ", consumerMessageType=" + this.getConsumerMessageType() + ", redisKey=" + this.getRedisKey() + ", isSingle=" + this.getIsSingle() + ", parm=" + this.getParm() + ")";
   }
}
