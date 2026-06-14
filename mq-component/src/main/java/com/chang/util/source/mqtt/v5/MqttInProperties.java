package com.chang.util.source.mqtt.v5;

import java.util.HashMap;
import java.util.Map;

public class MqttInProperties {
   private String broker;
   private String topic;
   private String username;
   private String password;
   private Boolean sslEnable = false;
   private String caCrtFile;
   private String crtFile;
   private String keyFile;
   private String sslPassword = "";
   private String messageType = "str";
   private Map<String, Object> parm = new HashMap();
   private boolean retained = false;
   private String subscribersQos = "0";
   private String version = "v5";
   private boolean autoGenerationClientId = true;
   private String clientID = null;
   private Integer nThreads = 8;
   private Integer maximumQueueSize = 1024;

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

   public String getSubscribersQos() {
      return this.subscribersQos;
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

   public Integer getNThreads() {
      return this.nThreads;
   }

   public Integer getMaximumQueueSize() {
      return this.maximumQueueSize;
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

   public void setSubscribersQos(String subscribersQos) {
      this.subscribersQos = subscribersQos;
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

   public void setNThreads(Integer nThreads) {
      this.nThreads = nThreads;
   }

   public void setMaximumQueueSize(Integer maximumQueueSize) {
      this.maximumQueueSize = maximumQueueSize;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MqttInProperties)) {
         return false;
      } else {
         MqttInProperties other = (MqttInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isRetained() != other.isRetained()) {
            return false;
         } else if (this.isAutoGenerationClientId() != other.isAutoGenerationClientId()) {
            return false;
         } else {
            label208: {
               Object this$sslEnable = this.getSslEnable();
               Object other$sslEnable = other.getSslEnable();
               if (this$sslEnable == null) {
                  if (other$sslEnable == null) {
                     break label208;
                  }
               } else if (this$sslEnable.equals(other$sslEnable)) {
                  break label208;
               }

               return false;
            }

            label201: {
               Object this$nThreads = this.getNThreads();
               Object other$nThreads = other.getNThreads();
               if (this$nThreads == null) {
                  if (other$nThreads == null) {
                     break label201;
                  }
               } else if (this$nThreads.equals(other$nThreads)) {
                  break label201;
               }

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

            label187: {
               Object this$broker = this.getBroker();
               Object other$broker = other.getBroker();
               if (this$broker == null) {
                  if (other$broker == null) {
                     break label187;
                  }
               } else if (this$broker.equals(other$broker)) {
                  break label187;
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

            label173: {
               Object this$username = this.getUsername();
               Object other$username = other.getUsername();
               if (this$username == null) {
                  if (other$username == null) {
                     break label173;
                  }
               } else if (this$username.equals(other$username)) {
                  break label173;
               }

               return false;
            }

            label166: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label166;
                  }
               } else if (this$password.equals(other$password)) {
                  break label166;
               }

               return false;
            }

            Object this$caCrtFile = this.getCaCrtFile();
            Object other$caCrtFile = other.getCaCrtFile();
            if (this$caCrtFile == null) {
               if (other$caCrtFile != null) {
                  return false;
               }
            } else if (!this$caCrtFile.equals(other$caCrtFile)) {
               return false;
            }

            label152: {
               Object this$crtFile = this.getCrtFile();
               Object other$crtFile = other.getCrtFile();
               if (this$crtFile == null) {
                  if (other$crtFile == null) {
                     break label152;
                  }
               } else if (this$crtFile.equals(other$crtFile)) {
                  break label152;
               }

               return false;
            }

            label145: {
               Object this$keyFile = this.getKeyFile();
               Object other$keyFile = other.getKeyFile();
               if (this$keyFile == null) {
                  if (other$keyFile == null) {
                     break label145;
                  }
               } else if (this$keyFile.equals(other$keyFile)) {
                  break label145;
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

            label124: {
               Object this$parm = this.getParm();
               Object other$parm = other.getParm();
               if (this$parm == null) {
                  if (other$parm == null) {
                     break label124;
                  }
               } else if (this$parm.equals(other$parm)) {
                  break label124;
               }

               return false;
            }

            Object this$subscribersQos = this.getSubscribersQos();
            Object other$subscribersQos = other.getSubscribersQos();
            if (this$subscribersQos == null) {
               if (other$subscribersQos != null) {
                  return false;
               }
            } else if (!this$subscribersQos.equals(other$subscribersQos)) {
               return false;
            }

            Object this$version = this.getVersion();
            Object other$version = other.getVersion();
            if (this$version == null) {
               if (other$version != null) {
                  return false;
               }
            } else if (!this$version.equals(other$version)) {
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

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MqttInProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + (this.isRetained() ? 79 : 97);
      result = result * 59 + (this.isAutoGenerationClientId() ? 79 : 97);
      Object $sslEnable = this.getSslEnable();
      result = result * 59 + ($sslEnable == null ? 43 : $sslEnable.hashCode());
      Object $nThreads = this.getNThreads();
      result = result * 59 + ($nThreads == null ? 43 : $nThreads.hashCode());
      Object $maximumQueueSize = this.getMaximumQueueSize();
      result = result * 59 + ($maximumQueueSize == null ? 43 : $maximumQueueSize.hashCode());
      Object $broker = this.getBroker();
      result = result * 59 + ($broker == null ? 43 : $broker.hashCode());
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      Object $username = this.getUsername();
      result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
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
      Object $subscribersQos = this.getSubscribersQos();
      result = result * 59 + ($subscribersQos == null ? 43 : $subscribersQos.hashCode());
      Object $version = this.getVersion();
      result = result * 59 + ($version == null ? 43 : $version.hashCode());
      Object $clientID = this.getClientID();
      result = result * 59 + ($clientID == null ? 43 : $clientID.hashCode());
      return result;
   }

   public String toString() {
      return "MqttInProperties(broker=" + this.getBroker() + ", topic=" + this.getTopic() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", sslEnable=" + this.getSslEnable() + ", caCrtFile=" + this.getCaCrtFile() + ", crtFile=" + this.getCrtFile() + ", keyFile=" + this.getKeyFile() + ", sslPassword=" + this.getSslPassword() + ", messageType=" + this.getMessageType() + ", parm=" + this.getParm() + ", retained=" + this.isRetained() + ", subscribersQos=" + this.getSubscribersQos() + ", version=" + this.getVersion() + ", autoGenerationClientId=" + this.isAutoGenerationClientId() + ", clientID=" + this.getClientID() + ", nThreads=" + this.getNThreads() + ", maximumQueueSize=" + this.getMaximumQueueSize() + ")";
   }
}
