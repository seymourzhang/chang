package com.chang.until.mqttclient.v5.common;

import lombok.NonNull;

public class MqttClientOptions {
   private String broker;
   private String publishTopic;
   private int publishQos;
   private String subsrcibeTopics;
   private String subsrcibeQoss;
   private @NonNull boolean autoGenerationClientID;
   private String clientID;
   private String userName;
   private String passWord;
   private int keepAlive;
   private int connectionTimeout;
   private boolean cleanStart;
   private Long sessionExpiryInterval;
   private String callBackClass;
   private String actionClass;
   private String payLoadClass;
   private boolean needSubsrcibe;
   private boolean retained;
   private Boolean sslEnable;
   private String caCrtFile;
   private String crtFile;
   private String keyFile;
   private String sslPassword;

   private static boolean $default$needSubsrcibe() {
      return false;
   }

   private static boolean $default$retained() {
      return true;
   }

   private static Boolean $default$sslEnable() {
      return false;
   }

   public static MqttClientOptionsBuilder builder() {
      return new MqttClientOptionsBuilder();
   }

   public String getBroker() {
      return this.broker;
   }

   public String getPublishTopic() {
      return this.publishTopic;
   }

   public int getPublishQos() {
      return this.publishQos;
   }

   public String getSubsrcibeTopics() {
      return this.subsrcibeTopics;
   }

   public String getSubsrcibeQoss() {
      return this.subsrcibeQoss;
   }

   public @NonNull boolean isAutoGenerationClientID() {
      return this.autoGenerationClientID;
   }

   public String getClientID() {
      return this.clientID;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassWord() {
      return this.passWord;
   }

   public int getKeepAlive() {
      return this.keepAlive;
   }

   public int getConnectionTimeout() {
      return this.connectionTimeout;
   }

   public boolean isCleanStart() {
      return this.cleanStart;
   }

   public Long getSessionExpiryInterval() {
      return this.sessionExpiryInterval;
   }

   public String getCallBackClass() {
      return this.callBackClass;
   }

   public String getActionClass() {
      return this.actionClass;
   }

   public String getPayLoadClass() {
      return this.payLoadClass;
   }

   public boolean isNeedSubsrcibe() {
      return this.needSubsrcibe;
   }

   public boolean isRetained() {
      return this.retained;
   }

   public Boolean getSslEnable() {
      return this.sslEnable;
   }

   public String getCaCrtFile() {
      return this.caCrtFile;
   }

   public String getCrtFile() {
      return this.crtFile;
   }

   public String getKeyFile() {
      return this.keyFile;
   }

   public String getSslPassword() {
      return this.sslPassword;
   }

   public void setBroker(String broker) {
      this.broker = broker;
   }

   public void setPublishTopic(String publishTopic) {
      this.publishTopic = publishTopic;
   }

   public void setPublishQos(int publishQos) {
      this.publishQos = publishQos;
   }

   public void setSubsrcibeTopics(String subsrcibeTopics) {
      this.subsrcibeTopics = subsrcibeTopics;
   }

   public void setSubsrcibeQoss(String subsrcibeQoss) {
      this.subsrcibeQoss = subsrcibeQoss;
   }

   public void setAutoGenerationClientID(@NonNull boolean autoGenerationClientID) {
      this.autoGenerationClientID = autoGenerationClientID;
   }

   public void setClientID(String clientID) {
      this.clientID = clientID;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public void setPassWord(String passWord) {
      this.passWord = passWord;
   }

   public void setKeepAlive(int keepAlive) {
      this.keepAlive = keepAlive;
   }

   public void setConnectionTimeout(int connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
   }

   public void setCleanStart(boolean cleanStart) {
      this.cleanStart = cleanStart;
   }

   public void setSessionExpiryInterval(Long sessionExpiryInterval) {
      this.sessionExpiryInterval = sessionExpiryInterval;
   }

   public void setCallBackClass(String callBackClass) {
      this.callBackClass = callBackClass;
   }

   public void setActionClass(String actionClass) {
      this.actionClass = actionClass;
   }

   public void setPayLoadClass(String payLoadClass) {
      this.payLoadClass = payLoadClass;
   }

   public void setNeedSubsrcibe(boolean needSubsrcibe) {
      this.needSubsrcibe = needSubsrcibe;
   }

   public void setRetained(boolean retained) {
      this.retained = retained;
   }

   public void setSslEnable(Boolean sslEnable) {
      this.sslEnable = sslEnable;
   }

   public void setCaCrtFile(String caCrtFile) {
      this.caCrtFile = caCrtFile;
   }

   public void setCrtFile(String crtFile) {
      this.crtFile = crtFile;
   }

   public void setKeyFile(String keyFile) {
      this.keyFile = keyFile;
   }

   public void setSslPassword(String sslPassword) {
      this.sslPassword = sslPassword;
   }

   public String toString() {
      return "MqttClientOptions(broker=" + this.getBroker() + ", publishTopic=" + this.getPublishTopic() + ", publishQos=" + this.getPublishQos() + ", subsrcibeTopics=" + this.getSubsrcibeTopics() + ", subsrcibeQoss=" + this.getSubsrcibeQoss() + ", autoGenerationClientID=" + this.isAutoGenerationClientID() + ", clientID=" + this.getClientID() + ", userName=" + this.getUserName() + ", passWord=" + this.getPassWord() + ", keepAlive=" + this.getKeepAlive() + ", connectionTimeout=" + this.getConnectionTimeout() + ", cleanStart=" + this.isCleanStart() + ", sessionExpiryInterval=" + this.getSessionExpiryInterval() + ", callBackClass=" + this.getCallBackClass() + ", actionClass=" + this.getActionClass() + ", payLoadClass=" + this.getPayLoadClass() + ", needSubsrcibe=" + this.isNeedSubsrcibe() + ", retained=" + this.isRetained() + ", sslEnable=" + this.getSslEnable() + ", caCrtFile=" + this.getCaCrtFile() + ", crtFile=" + this.getCrtFile() + ", keyFile=" + this.getKeyFile() + ", sslPassword=" + this.getSslPassword() + ")";
   }

   public MqttClientOptions(String broker, String publishTopic, int publishQos, String subsrcibeTopics, String subsrcibeQoss, @NonNull boolean autoGenerationClientID, String clientID, String userName, String passWord, int keepAlive, int connectionTimeout, boolean cleanStart, Long sessionExpiryInterval, String callBackClass, String actionClass, String payLoadClass, boolean needSubsrcibe, boolean retained, Boolean sslEnable, String caCrtFile, String crtFile, String keyFile, String sslPassword) {
      this.publishQos = QoS.EXACTLY_ONCE;
      this.cleanStart = true;
      this.sessionExpiryInterval = 0L;
      this.sslPassword = "";
      this.broker = broker;
      this.publishTopic = publishTopic;
      this.publishQos = publishQos;
      this.subsrcibeTopics = subsrcibeTopics;
      this.subsrcibeQoss = subsrcibeQoss;
      this.autoGenerationClientID = autoGenerationClientID;
      this.clientID = clientID;
      this.userName = userName;
      this.passWord = passWord;
      this.keepAlive = keepAlive;
      this.connectionTimeout = connectionTimeout;
      this.cleanStart = cleanStart;
      this.sessionExpiryInterval = sessionExpiryInterval;
      this.callBackClass = callBackClass;
      this.actionClass = actionClass;
      this.payLoadClass = payLoadClass;
      this.needSubsrcibe = needSubsrcibe;
      this.retained = retained;
      this.sslEnable = sslEnable;
      this.caCrtFile = caCrtFile;
      this.crtFile = crtFile;
      this.keyFile = keyFile;
      this.sslPassword = sslPassword;
   }

   public MqttClientOptions() {
      this.publishQos = QoS.EXACTLY_ONCE;
      this.cleanStart = true;
      this.sessionExpiryInterval = 0L;
      this.sslPassword = "";
      this.needSubsrcibe = $default$needSubsrcibe();
      this.retained = $default$retained();
      this.sslEnable = $default$sslEnable();
   }

   public static class MqttClientOptionsBuilder {
      private String broker;
      private String publishTopic;
      private int publishQos;
      private String subsrcibeTopics;
      private String subsrcibeQoss;
      private boolean autoGenerationClientID;
      private String clientID;
      private String userName;
      private String passWord;
      private int keepAlive;
      private int connectionTimeout;
      private boolean cleanStart;
      private Long sessionExpiryInterval;
      private String callBackClass;
      private String actionClass;
      private String payLoadClass;
      private boolean needSubsrcibe$set;
      private boolean needSubsrcibe$value;
      private boolean retained$set;
      private boolean retained$value;
      private boolean sslEnable$set;
      private Boolean sslEnable$value;
      private String caCrtFile;
      private String crtFile;
      private String keyFile;
      private String sslPassword;

      MqttClientOptionsBuilder() {
      }

      public MqttClientOptionsBuilder broker(String broker) {
         this.broker = broker;
         return this;
      }

      public MqttClientOptionsBuilder publishTopic(String publishTopic) {
         this.publishTopic = publishTopic;
         return this;
      }

      public MqttClientOptionsBuilder publishQos(int publishQos) {
         this.publishQos = publishQos;
         return this;
      }

      public MqttClientOptionsBuilder subsrcibeTopics(String subsrcibeTopics) {
         this.subsrcibeTopics = subsrcibeTopics;
         return this;
      }

      public MqttClientOptionsBuilder subsrcibeQoss(String subsrcibeQoss) {
         this.subsrcibeQoss = subsrcibeQoss;
         return this;
      }

      public MqttClientOptionsBuilder autoGenerationClientID(@NonNull boolean autoGenerationClientID) {
         this.autoGenerationClientID = autoGenerationClientID;
         return this;
      }

      public MqttClientOptionsBuilder clientID(String clientID) {
         this.clientID = clientID;
         return this;
      }

      public MqttClientOptionsBuilder userName(String userName) {
         this.userName = userName;
         return this;
      }

      public MqttClientOptionsBuilder passWord(String passWord) {
         this.passWord = passWord;
         return this;
      }

      public MqttClientOptionsBuilder keepAlive(int keepAlive) {
         this.keepAlive = keepAlive;
         return this;
      }

      public MqttClientOptionsBuilder connectionTimeout(int connectionTimeout) {
         this.connectionTimeout = connectionTimeout;
         return this;
      }

      public MqttClientOptionsBuilder cleanStart(boolean cleanStart) {
         this.cleanStart = cleanStart;
         return this;
      }

      public MqttClientOptionsBuilder sessionExpiryInterval(Long sessionExpiryInterval) {
         this.sessionExpiryInterval = sessionExpiryInterval;
         return this;
      }

      public MqttClientOptionsBuilder callBackClass(String callBackClass) {
         this.callBackClass = callBackClass;
         return this;
      }

      public MqttClientOptionsBuilder actionClass(String actionClass) {
         this.actionClass = actionClass;
         return this;
      }

      public MqttClientOptionsBuilder payLoadClass(String payLoadClass) {
         this.payLoadClass = payLoadClass;
         return this;
      }

      public MqttClientOptionsBuilder needSubsrcibe(boolean needSubsrcibe) {
         this.needSubsrcibe$value = needSubsrcibe;
         this.needSubsrcibe$set = true;
         return this;
      }

      public MqttClientOptionsBuilder retained(boolean retained) {
         this.retained$value = retained;
         this.retained$set = true;
         return this;
      }

      public MqttClientOptionsBuilder sslEnable(Boolean sslEnable) {
         this.sslEnable$value = sslEnable;
         this.sslEnable$set = true;
         return this;
      }

      public MqttClientOptionsBuilder caCrtFile(String caCrtFile) {
         this.caCrtFile = caCrtFile;
         return this;
      }

      public MqttClientOptionsBuilder crtFile(String crtFile) {
         this.crtFile = crtFile;
         return this;
      }

      public MqttClientOptionsBuilder keyFile(String keyFile) {
         this.keyFile = keyFile;
         return this;
      }

      public MqttClientOptionsBuilder sslPassword(String sslPassword) {
         this.sslPassword = sslPassword;
         return this;
      }

      public MqttClientOptions build() {
         boolean needSubsrcibe$value = this.needSubsrcibe$value;
         if (!this.needSubsrcibe$set) {
            needSubsrcibe$value = MqttClientOptions.$default$needSubsrcibe();
         }

         boolean retained$value = this.retained$value;
         if (!this.retained$set) {
            retained$value = MqttClientOptions.$default$retained();
         }

         Boolean sslEnable$value = this.sslEnable$value;
         if (!this.sslEnable$set) {
            sslEnable$value = MqttClientOptions.$default$sslEnable();
         }

         return new MqttClientOptions(this.broker, this.publishTopic, this.publishQos, this.subsrcibeTopics, this.subsrcibeQoss, this.autoGenerationClientID, this.clientID, this.userName, this.passWord, this.keepAlive, this.connectionTimeout, this.cleanStart, this.sessionExpiryInterval, this.callBackClass, this.actionClass, this.payLoadClass, needSubsrcibe$value, retained$value, sslEnable$value, this.caCrtFile, this.crtFile, this.keyFile, this.sslPassword);
      }

      public String toString() {
         return "MqttClientOptions.MqttClientOptionsBuilder(broker=" + this.broker + ", publishTopic=" + this.publishTopic + ", publishQos=" + this.publishQos + ", subsrcibeTopics=" + this.subsrcibeTopics + ", subsrcibeQoss=" + this.subsrcibeQoss + ", autoGenerationClientID=" + this.autoGenerationClientID + ", clientID=" + this.clientID + ", userName=" + this.userName + ", passWord=" + this.passWord + ", keepAlive=" + this.keepAlive + ", connectionTimeout=" + this.connectionTimeout + ", cleanStart=" + this.cleanStart + ", sessionExpiryInterval=" + this.sessionExpiryInterval + ", callBackClass=" + this.callBackClass + ", actionClass=" + this.actionClass + ", payLoadClass=" + this.payLoadClass + ", needSubsrcibe$value=" + this.needSubsrcibe$value + ", retained$value=" + this.retained$value + ", sslEnable$value=" + this.sslEnable$value + ", caCrtFile=" + this.caCrtFile + ", crtFile=" + this.crtFile + ", keyFile=" + this.keyFile + ", sslPassword=" + this.sslPassword + ")";
      }
   }
}
