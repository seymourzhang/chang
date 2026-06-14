package com.chang.util.rabbitmqApi;

public class RabbitMQOptions {
   private String host;
   private int port;
   private String username;
   private String password;
   private String queue;
   private String topic;
   private String keyName;
   private boolean durable;

   public static RabbitMQOptionsBuilder builder() {
      return new RabbitMQOptionsBuilder();
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public String getQueue() {
      return this.queue;
   }

   public String getTopic() {
      return this.topic;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public boolean getDurable() {
      return durable;
   }

   public void setDurable(boolean durable) {
      this.durable = durable;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setQueue(String queue) {
      this.queue = queue;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public String toString() {
      return "RabbitMQOptions(host=" + this.getHost() + ", port=" + this.getPort() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", queue=" + this.getQueue() + ", topic=" + this.getTopic() + ", keyName=" + this.getKeyName() + ", durable=" + this.getDurable() + ")";
   }

   public RabbitMQOptions(String host, int port, String username, String password, String queue, String topic, String keyName, boolean durable) {
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
      this.queue = queue;
      this.topic = topic;
      this.keyName = keyName;
      this.durable = durable;
   }

   public RabbitMQOptions() {
   }

   public static class RabbitMQOptionsBuilder {
      private String host;
      private int port;
      private String username;
      private String password;
      private String queue;
      private String topic;
      private String keyName;
      private boolean durable;

      RabbitMQOptionsBuilder() {
      }

      public RabbitMQOptionsBuilder host(String host) {
         this.host = host;
         return this;
      }

      public RabbitMQOptionsBuilder port(int port) {
         this.port = port;
         return this;
      }

      public RabbitMQOptionsBuilder username(String username) {
         this.username = username;
         return this;
      }

      public RabbitMQOptionsBuilder password(String password) {
         this.password = password;
         return this;
      }

      public RabbitMQOptionsBuilder queue(String queue) {
         this.queue = queue;
         return this;
      }

      public RabbitMQOptionsBuilder topic(String topic) {
         this.topic = topic;
         return this;
      }

      public RabbitMQOptionsBuilder keyName(String keyName) {
         this.keyName = keyName;
         return this;
      }

      public RabbitMQOptionsBuilder durable(boolean durable) {
         this.durable = durable;
         return this;
      }

      public RabbitMQOptions build() {
         return new RabbitMQOptions(this.host, this.port, this.username, this.password, this.queue, this.topic, this.keyName, this.durable);
      }

      public String toString() {
         return "RabbitMQOptions.RabbitMQOptionsBuilder(host=" + this.host + ", port=" + this.port + ", username=" + this.username + ", password=" + this.password + ", queue=" + this.queue + ", topic=" + this.topic + ", keyName=" + this.keyName + ", durable=" + this.durable + ")";
      }
   }
}
