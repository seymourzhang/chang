package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class MqttOutProperties {
   private String broker;
   private String topic;
   private String username;
   private String password;
   private Boolean isCache = false;
   private String cacheTopic;
   private String cacheName;
   private int publishQos = 0;
   private Long expiredDataSize = 0L;
   private Long batchSize = 10L;
   private Long timeInterval = 1000L;
   private Long queuePopSize = 1L;
   private Boolean sslEnable = false;
   private String caCrtFile;
   private String crtFile;
   private String keyFile;
   private String sslPassword = "";
   private String messageType = "str";
   private Map<String, Object> parm = new HashMap();
   private boolean retained = false;
   private String version = "v3";
   private boolean autoGenerationClientId = true;
   private String clientID = null;
   private String willPayLoad = null;

   public String getBroker() {
      return this.broker;
   }

   public String getTopic() {
      return this.topic;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public Boolean getIsCache() {
      return this.isCache;
   }

   public String getCacheTopic() {
      return this.cacheTopic;
   }

   public String getCacheName() {
      return this.cacheName;
   }

   public int getPublishQos() {
      return this.publishQos;
   }

   public Long getExpiredDataSize() {
      return this.expiredDataSize;
   }

   public Long getBatchSize() {
      return this.batchSize;
   }

   public Long getTimeInterval() {
      return this.timeInterval;
   }

   public Long getQueuePopSize() {
      return this.queuePopSize;
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

   public String getMessageType() {
      return this.messageType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public boolean isRetained() {
      return this.retained;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean isAutoGenerationClientId() {
      return this.autoGenerationClientId;
   }

   public String getClientID() {
      return this.clientID;
   }

   public String getWillPayLoad() {
      return this.willPayLoad;
   }

   public void setBroker(String broker) {
      this.broker = broker;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setIsCache(Boolean isCache) {
      this.isCache = isCache;
   }

   public void setCacheTopic(String cacheTopic) {
      this.cacheTopic = cacheTopic;
   }

   public void setCacheName(String cacheName) {
      this.cacheName = cacheName;
   }

   public void setPublishQos(int publishQos) {
      this.publishQos = publishQos;
   }

   public void setExpiredDataSize(Long expiredDataSize) {
      this.expiredDataSize = expiredDataSize;
   }

   public void setBatchSize(Long batchSize) {
      this.batchSize = batchSize;
   }

   public void setTimeInterval(Long timeInterval) {
      this.timeInterval = timeInterval;
   }

   public void setQueuePopSize(Long queuePopSize) {
      this.queuePopSize = queuePopSize;
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

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public void setRetained(boolean retained) {
      this.retained = retained;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public void setAutoGenerationClientId(boolean autoGenerationClientId) {
      this.autoGenerationClientId = autoGenerationClientId;
   }

   public void setClientID(String clientID) {
      this.clientID = clientID;
   }

   public void setWillPayLoad(String willPayLoad) {
      this.willPayLoad = willPayLoad;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MqttOutProperties)) {
         return false;
      } else {
         MqttOutProperties other = (MqttOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPublishQos() != other.getPublishQos()) {
            return false;
         } else if (this.isRetained() != other.isRetained()) {
            return false;
         } else if (this.isAutoGenerationClientId() != other.isAutoGenerationClientId()) {
            return false;
         } else {
            Object this$isCache = this.getIsCache();
            Object other$isCache = other.getIsCache();
            if (this$isCache == null) {
               if (other$isCache != null) {
                  return false;
               }
            } else if (!this$isCache.equals(other$isCache)) {
               return false;
            }

            label263: {
               Object this$expiredDataSize = this.getExpiredDataSize();
               Object other$expiredDataSize = other.getExpiredDataSize();
               if (this$expiredDataSize == null) {
                  if (other$expiredDataSize == null) {
                     break label263;
                  }
               } else if (this$expiredDataSize.equals(other$expiredDataSize)) {
                  break label263;
               }

               return false;
            }

            label256: {
               Object this$batchSize = this.getBatchSize();
               Object other$batchSize = other.getBatchSize();
               if (this$batchSize == null) {
                  if (other$batchSize == null) {
                     break label256;
                  }
               } else if (this$batchSize.equals(other$batchSize)) {
                  break label256;
               }

               return false;
            }

            Object this$timeInterval = this.getTimeInterval();
            Object other$timeInterval = other.getTimeInterval();
            if (this$timeInterval == null) {
               if (other$timeInterval != null) {
                  return false;
               }
            } else if (!this$timeInterval.equals(other$timeInterval)) {
               return false;
            }

            Object this$queuePopSize = this.getQueuePopSize();
            Object other$queuePopSize = other.getQueuePopSize();
            if (this$queuePopSize == null) {
               if (other$queuePopSize != null) {
                  return false;
               }
            } else if (!this$queuePopSize.equals(other$queuePopSize)) {
               return false;
            }

            label235: {
               Object this$sslEnable = this.getSslEnable();
               Object other$sslEnable = other.getSslEnable();
               if (this$sslEnable == null) {
                  if (other$sslEnable == null) {
                     break label235;
                  }
               } else if (this$sslEnable.equals(other$sslEnable)) {
                  break label235;
               }

               return false;
            }

            label228: {
               Object this$broker = this.getBroker();
               Object other$broker = other.getBroker();
               if (this$broker == null) {
                  if (other$broker == null) {
                     break label228;
                  }
               } else if (this$broker.equals(other$broker)) {
                  break label228;
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

            label214: {
               Object this$username = this.getUsername();
               Object other$username = other.getUsername();
               if (this$username == null) {
                  if (other$username == null) {
                     break label214;
                  }
               } else if (this$username.equals(other$username)) {
                  break label214;
               }

               return false;
            }

            label207: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label207;
                  }
               } else if (this$password.equals(other$password)) {
                  break label207;
               }

               return false;
            }

            label200: {
               Object this$cacheTopic = this.getCacheTopic();
               Object other$cacheTopic = other.getCacheTopic();
               if (this$cacheTopic == null) {
                  if (other$cacheTopic == null) {
                     break label200;
                  }
               } else if (this$cacheTopic.equals(other$cacheTopic)) {
                  break label200;
               }

               return false;
            }

            Object this$cacheName = this.getCacheName();
            Object other$cacheName = other.getCacheName();
            if (this$cacheName == null) {
               if (other$cacheName != null) {
                  return false;
               }
            } else if (!this$cacheName.equals(other$cacheName)) {
               return false;
            }

            label186: {
               Object this$caCrtFile = this.getCaCrtFile();
               Object other$caCrtFile = other.getCaCrtFile();
               if (this$caCrtFile == null) {
                  if (other$caCrtFile == null) {
                     break label186;
                  }
               } else if (this$caCrtFile.equals(other$caCrtFile)) {
                  break label186;
               }

               return false;
            }

            Object this$crtFile = this.getCrtFile();
            Object other$crtFile = other.getCrtFile();
            if (this$crtFile == null) {
               if (other$crtFile != null) {
                  return false;
               }
            } else if (!this$crtFile.equals(other$crtFile)) {
               return false;
            }

            label172: {
               Object this$keyFile = this.getKeyFile();
               Object other$keyFile = other.getKeyFile();
               if (this$keyFile == null) {
                  if (other$keyFile == null) {
                     break label172;
                  }
               } else if (this$keyFile.equals(other$keyFile)) {
                  break label172;
               }

               return false;
            }

            Object this$sslPassword = this.getSslPassword();
            Object other$sslPassword = other.getSslPassword();
            if (this$sslPassword == null) {
               if (other$sslPassword != null) {
                  return false;
               }
            } else if (!this$sslPassword.equals(other$sslPassword)) {
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

            label151: {
               Object this$parm = this.getParm();
               Object other$parm = other.getParm();
               if (this$parm == null) {
                  if (other$parm == null) {
                     break label151;
                  }
               } else if (this$parm.equals(other$parm)) {
                  break label151;
               }

               return false;
            }

            label144: {
               Object this$version = this.getVersion();
               Object other$version = other.getVersion();
               if (this$version == null) {
                  if (other$version == null) {
                     break label144;
                  }
               } else if (this$version.equals(other$version)) {
                  break label144;
               }

               return false;
            }

            Object this$clientID = this.getClientID();
            Object other$clientID = other.getClientID();
            if (this$clientID == null) {
               if (other$clientID != null) {
                  return false;
               }
            } else if (!this$clientID.equals(other$clientID)) {
               return false;
            }

            Object this$willPayLoad = this.getWillPayLoad();
            Object other$willPayLoad = other.getWillPayLoad();
            if (this$willPayLoad == null) {
               if (other$willPayLoad != null) {
                  return false;
               }
            } else if (!this$willPayLoad.equals(other$willPayLoad)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MqttOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getPublishQos();
      result = result * 59 + (this.isRetained() ? 79 : 97);
      result = result * 59 + (this.isAutoGenerationClientId() ? 79 : 97);
      Object $isCache = this.getIsCache();
      result = result * 59 + ($isCache == null ? 43 : $isCache.hashCode());
      Object $expiredDataSize = this.getExpiredDataSize();
      result = result * 59 + ($expiredDataSize == null ? 43 : $expiredDataSize.hashCode());
      Object $batchSize = this.getBatchSize();
      result = result * 59 + ($batchSize == null ? 43 : $batchSize.hashCode());
      Object $timeInterval = this.getTimeInterval();
      result = result * 59 + ($timeInterval == null ? 43 : $timeInterval.hashCode());
      Object $queuePopSize = this.getQueuePopSize();
      result = result * 59 + ($queuePopSize == null ? 43 : $queuePopSize.hashCode());
      Object $sslEnable = this.getSslEnable();
      result = result * 59 + ($sslEnable == null ? 43 : $sslEnable.hashCode());
      Object $broker = this.getBroker();
      result = result * 59 + ($broker == null ? 43 : $broker.hashCode());
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      Object $username = this.getUsername();
      result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $cacheTopic = this.getCacheTopic();
      result = result * 59 + ($cacheTopic == null ? 43 : $cacheTopic.hashCode());
      Object $cacheName = this.getCacheName();
      result = result * 59 + ($cacheName == null ? 43 : $cacheName.hashCode());
      Object $caCrtFile = this.getCaCrtFile();
      result = result * 59 + ($caCrtFile == null ? 43 : $caCrtFile.hashCode());
      Object $crtFile = this.getCrtFile();
      result = result * 59 + ($crtFile == null ? 43 : $crtFile.hashCode());
      Object $keyFile = this.getKeyFile();
      result = result * 59 + ($keyFile == null ? 43 : $keyFile.hashCode());
      Object $sslPassword = this.getSslPassword();
      result = result * 59 + ($sslPassword == null ? 43 : $sslPassword.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      Object $version = this.getVersion();
      result = result * 59 + ($version == null ? 43 : $version.hashCode());
      Object $clientID = this.getClientID();
      result = result * 59 + ($clientID == null ? 43 : $clientID.hashCode());
      Object $willPayLoad = this.getWillPayLoad();
      result = result * 59 + ($willPayLoad == null ? 43 : $willPayLoad.hashCode());
      return result;
   }

   public String toString() {
      return "MqttOutProperties(broker=" + this.getBroker() + ", topic=" + this.getTopic() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", isCache=" + this.getIsCache() + ", cacheTopic=" + this.getCacheTopic() + ", cacheName=" + this.getCacheName() + ", publishQos=" + this.getPublishQos() + ", expiredDataSize=" + this.getExpiredDataSize() + ", batchSize=" + this.getBatchSize() + ", timeInterval=" + this.getTimeInterval() + ", queuePopSize=" + this.getQueuePopSize() + ", sslEnable=" + this.getSslEnable() + ", caCrtFile=" + this.getCaCrtFile() + ", crtFile=" + this.getCrtFile() + ", keyFile=" + this.getKeyFile() + ", sslPassword=" + this.getSslPassword() + ", messageType=" + this.getMessageType() + ", parm=" + this.getParm() + ", retained=" + this.isRetained() + ", version=" + this.getVersion() + ", autoGenerationClientId=" + this.isAutoGenerationClientId() + ", clientID=" + this.getClientID() + ", willPayLoad=" + this.getWillPayLoad() + ")";
   }
}
