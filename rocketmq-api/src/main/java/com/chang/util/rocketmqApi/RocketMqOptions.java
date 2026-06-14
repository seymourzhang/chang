package com.chang.util.rocketmqApi;

public class RocketMqOptions {
   private String group;
   private String nameSrvAddr;
   private String topic;
   private String messageModel;
   private String subExpression;
   private String accessKey;
   private String secretKey;
   private String namespace;
   private Integer sendMsgTimeout;
   private Integer messageBatchMaxSize;
   private Integer consumeThread;

   public static RocketMqOptionsBuilder builder() {
      return new RocketMqOptionsBuilder();
   }

   public String getGroup() {
      return this.group;
   }

   public String getNameSrvAddr() {
      return this.nameSrvAddr;
   }

   public String getTopic() {
      return this.topic;
   }

   public String getMessageModel() {
      return this.messageModel;
   }

   public String getSubExpression() {
      return this.subExpression;
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

   public Integer getSendMsgTimeout() {
      return this.sendMsgTimeout;
   }

   public Integer getMessageBatchMaxSize() {
      return this.messageBatchMaxSize;
   }

   public Integer getConsumeThread() {
      return this.consumeThread;
   }

   public void setGroup(String group) {
      this.group = group;
   }

   public void setNameSrvAddr(String nameSrvAddr) {
      this.nameSrvAddr = nameSrvAddr;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setMessageModel(String messageModel) {
      this.messageModel = messageModel;
   }

   public void setSubExpression(String subExpression) {
      this.subExpression = subExpression;
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

   public void setSendMsgTimeout(Integer sendMsgTimeout) {
      this.sendMsgTimeout = sendMsgTimeout;
   }

   public void setMessageBatchMaxSize(Integer messageBatchMaxSize) {
      this.messageBatchMaxSize = messageBatchMaxSize;
   }

   public void setConsumeThread(Integer consumeThread) {
      this.consumeThread = consumeThread;
   }

   public String toString() {
      return "RocketMqOptions(group=" + this.getGroup() + ", nameSrvAddr=" + this.getNameSrvAddr() + ", topic=" + this.getTopic() + ", messageModel=" + this.getMessageModel() + ", subExpression=" + this.getSubExpression() + ", accessKey=" + this.getAccessKey() + ", secretKey=" + this.getSecretKey() + ", namespace=" + this.getNamespace() + ", sendMsgTimeout=" + this.getSendMsgTimeout() + ", messageBatchMaxSize=" + this.getMessageBatchMaxSize() + ", consumeThread=" + this.getConsumeThread() + ")";
   }

   public RocketMqOptions(String group, String nameSrvAddr, String topic, String messageModel, String subExpression, String accessKey, String secretKey, String namespace, Integer sendMsgTimeout, Integer messageBatchMaxSize, Integer consumeThread) {
      this.group = group;
      this.nameSrvAddr = nameSrvAddr;
      this.topic = topic;
      this.messageModel = messageModel;
      this.subExpression = subExpression;
      this.accessKey = accessKey;
      this.secretKey = secretKey;
      this.namespace = namespace;
      this.sendMsgTimeout = sendMsgTimeout;
      this.messageBatchMaxSize = messageBatchMaxSize;
      this.consumeThread = consumeThread;
   }

   public RocketMqOptions() {
   }

   public static class RocketMqOptionsBuilder {
      private String group;
      private String nameSrvAddr;
      private String topic;
      private String messageModel;
      private String subExpression;
      private String accessKey;
      private String secretKey;
      private String namespace;
      private Integer sendMsgTimeout;
      private Integer messageBatchMaxSize;
      private Integer consumeThread;

      RocketMqOptionsBuilder() {
      }

      public RocketMqOptionsBuilder group(String group) {
         this.group = group;
         return this;
      }

      public RocketMqOptionsBuilder nameSrvAddr(String nameSrvAddr) {
         this.nameSrvAddr = nameSrvAddr;
         return this;
      }

      public RocketMqOptionsBuilder topic(String topic) {
         this.topic = topic;
         return this;
      }

      public RocketMqOptionsBuilder messageModel(String messageModel) {
         this.messageModel = messageModel;
         return this;
      }

      public RocketMqOptionsBuilder subExpression(String subExpression) {
         this.subExpression = subExpression;
         return this;
      }

      public RocketMqOptionsBuilder accessKey(String accessKey) {
         this.accessKey = accessKey;
         return this;
      }

      public RocketMqOptionsBuilder secretKey(String secretKey) {
         this.secretKey = secretKey;
         return this;
      }

      public RocketMqOptionsBuilder namespace(String namespace) {
         this.namespace = namespace;
         return this;
      }

      public RocketMqOptionsBuilder sendMsgTimeout(Integer sendMsgTimeout) {
         this.sendMsgTimeout = sendMsgTimeout;
         return this;
      }

      public RocketMqOptionsBuilder messageBatchMaxSize(Integer messageBatchMaxSize) {
         this.messageBatchMaxSize = messageBatchMaxSize;
         return this;
      }

      public RocketMqOptionsBuilder consumeThread(Integer consumeThread) {
         this.consumeThread = consumeThread;
         return this;
      }

      public RocketMqOptions build() {
         return new RocketMqOptions(this.group, this.nameSrvAddr, this.topic, this.messageModel, this.subExpression, this.accessKey, this.secretKey, this.namespace, this.sendMsgTimeout, this.messageBatchMaxSize, this.consumeThread);
      }

      public String toString() {
         return "RocketMqOptions.RocketMqOptionsBuilder(group=" + this.group + ", nameSrvAddr=" + this.nameSrvAddr + ", topic=" + this.topic + ", messageModel=" + this.messageModel + ", subExpression=" + this.subExpression + ", accessKey=" + this.accessKey + ", secretKey=" + this.secretKey + ", namespace=" + this.namespace + ", sendMsgTimeout=" + this.sendMsgTimeout + ", messageBatchMaxSize=" + this.messageBatchMaxSize + ", consumeThread=" + this.consumeThread + ")";
      }
   }
}
