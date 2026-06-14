package com.chang.servlet.kafka.configuration.api;

public class KafkaOptions {
   private String servers;
   private String groupId;
   private String keyName;
   private String topic;

   public static KafkaOptionsBuilder builder() {
      return new KafkaOptionsBuilder();
   }

   public String getServers() {
      return this.servers;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public String getTopic() {
      return this.topic;
   }

   public void setServers(String servers) {
      this.servers = servers;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public String toString() {
      return "KafkaOptions(servers=" + this.getServers() + ", groupId=" + this.getGroupId() + ", keyName=" + this.getKeyName() + ", topic=" + this.getTopic() + ")";
   }

   public KafkaOptions(String servers, String groupId, String keyName, String topic) {
      this.servers = servers;
      this.groupId = groupId;
      this.keyName = keyName;
      this.topic = topic;
   }

   public KafkaOptions() {
   }

   public static class KafkaOptionsBuilder {
      private String servers;
      private String groupId;
      private String keyName;
      private String topic;

      KafkaOptionsBuilder() {
      }

      public KafkaOptionsBuilder servers(String servers) {
         this.servers = servers;
         return this;
      }

      public KafkaOptionsBuilder groupId(String groupId) {
         this.groupId = groupId;
         return this;
      }

      public KafkaOptionsBuilder keyName(String keyName) {
         this.keyName = keyName;
         return this;
      }

      public KafkaOptionsBuilder topic(String topic) {
         this.topic = topic;
         return this;
      }

      public KafkaOptions build() {
         return new KafkaOptions(this.servers, this.groupId, this.keyName, this.topic);
      }

      public String toString() {
         return "KafkaOptions.KafkaOptionsBuilder(servers=" + this.servers + ", groupId=" + this.groupId + ", keyName=" + this.keyName + ", topic=" + this.topic + ")";
      }
   }
}
