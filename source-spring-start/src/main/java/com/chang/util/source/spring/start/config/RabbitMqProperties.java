package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class RabbitMqProperties {
   private String host;
   private int port;
   private String username;
   private String password;
   private String queue;
   private String topic;
   private String messageType = "str";
   private Boolean isAck = false;
   private Integer basicQos = 100;
   private Integer consumerNum = 4;
   private Integer nThreads = 4;
   private Integer maximumQueueSize = 100;
   private Boolean isDurable = false;
   private Map<String, Object> parm = new HashMap();

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

   public String getMessageType() {
      return this.messageType;
   }

   public Boolean getIsAck() {
      return this.isAck;
   }

   public Integer getBasicQos() {
      return this.basicQos;
   }

   public Integer getConsumerNum() {
      return this.consumerNum;
   }

   public Integer getNThreads() {
      return this.nThreads;
   }

   public Integer getMaximumQueueSize() {
      return this.maximumQueueSize;
   }

   public Boolean getIsDurable() {
      return this.isDurable;
   }

   public Map<String, Object> getParm() {
      return this.parm;
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

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setIsAck(Boolean isAck) {
      this.isAck = isAck;
   }

   public void setBasicQos(Integer basicQos) {
      this.basicQos = basicQos;
   }

   public void setConsumerNum(Integer consumerNum) {
      this.consumerNum = consumerNum;
   }

   public void setNThreads(Integer nThreads) {
      this.nThreads = nThreads;
   }

   public void setMaximumQueueSize(Integer maximumQueueSize) {
      this.maximumQueueSize = maximumQueueSize;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

    public void setDurable(Boolean durable) {
        this.isDurable = durable;
    }

    public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RabbitMqProperties)) {
         return false;
      } else {
         RabbitMqProperties other = (RabbitMqProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPort() != other.getPort()) {
            return false;
         } else {
            label157: {
               Object this$isAck = this.getIsAck();
               Object other$isAck = other.getIsAck();
               if (this$isAck == null) {
                  if (other$isAck == null) {
                     break label157;
                  }
               } else if (this$isAck.equals(other$isAck)) {
                  break label157;
               }

               return false;
            }

            label150: {
               Object this$basicQos = this.getBasicQos();
               Object other$basicQos = other.getBasicQos();
               if (this$basicQos == null) {
                  if (other$basicQos == null) {
                     break label150;
                  }
               } else if (this$basicQos.equals(other$basicQos)) {
                  break label150;
               }

               return false;
            }

            Object this$consumerNum = this.getConsumerNum();
            Object other$consumerNum = other.getConsumerNum();
            if (this$consumerNum == null) {
               if (other$consumerNum != null) {
                  return false;
               }
            } else if (!this$consumerNum.equals(other$consumerNum)) {
               return false;
            }

            label136: {
               Object this$nThreads = this.getNThreads();
               Object other$nThreads = other.getNThreads();
               if (this$nThreads == null) {
                  if (other$nThreads == null) {
                     break label136;
                  }
               } else if (this$nThreads.equals(other$nThreads)) {
                  break label136;
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

            label122: {
               Object this$host = this.getHost();
               Object other$host = other.getHost();
               if (this$host == null) {
                  if (other$host == null) {
                     break label122;
                  }
               } else if (this$host.equals(other$host)) {
                  break label122;
               }

               return false;
            }

            Object this$username = this.getUsername();
            Object other$username = other.getUsername();
            if (this$username == null) {
               if (other$username != null) {
                  return false;
               }
            } else if (!this$username.equals(other$username)) {
               return false;
            }

            Object this$password = this.getPassword();
            Object other$password = other.getPassword();
            if (this$password == null) {
               if (other$password != null) {
                  return false;
               }
            } else if (!this$password.equals(other$password)) {
               return false;
            }

            Object this$queue = this.getQueue();
            Object other$queue = other.getQueue();
            if (this$queue == null) {
               if (other$queue != null) {
                  return false;
               }
            } else if (!this$queue.equals(other$queue)) {
               return false;
            }

            label94: {
               Object this$topic = this.getTopic();
               Object other$topic = other.getTopic();
               if (this$topic == null) {
                  if (other$topic == null) {
                     break label94;
                  }
               } else if (this$topic.equals(other$topic)) {
                  break label94;
               }

               return false;
            }

            label87: {
               Object this$messageType = this.getMessageType();
               Object other$messageType = other.getMessageType();
               if (this$messageType == null) {
                  if (other$messageType == null) {
                     break label87;
                  }
               } else if (this$messageType.equals(other$messageType)) {
                  break label87;
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
      return other instanceof RabbitMqProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getPort();
      Object $isAck = this.getIsAck();
      result = result * 59 + ($isAck == null ? 43 : $isAck.hashCode());
      Object $basicQos = this.getBasicQos();
      result = result * 59 + ($basicQos == null ? 43 : $basicQos.hashCode());
      Object $consumerNum = this.getConsumerNum();
      result = result * 59 + ($consumerNum == null ? 43 : $consumerNum.hashCode());
      Object $nThreads = this.getNThreads();
      result = result * 59 + ($nThreads == null ? 43 : $nThreads.hashCode());
      Object $maximumQueueSize = this.getMaximumQueueSize();
      result = result * 59 + ($maximumQueueSize == null ? 43 : $maximumQueueSize.hashCode());
      Object $host = this.getHost();
      result = result * 59 + ($host == null ? 43 : $host.hashCode());
      Object $username = this.getUsername();
      result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $queue = this.getQueue();
      result = result * 59 + ($queue == null ? 43 : $queue.hashCode());
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "RabbitMqProperties(host=" + this.getHost() + ", port=" + this.getPort() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", queue=" + this.getQueue() + ", topic=" + this.getTopic() + ", messageType=" + this.getMessageType() + ", isAck=" + this.getIsAck() + ", basicQos=" + this.getBasicQos() + ", consumerNum=" + this.getConsumerNum() + ", nThreads=" + this.getNThreads() + ", maximumQueueSize=" + this.getMaximumQueueSize() + ", parm=" + this.getParm() + ", isDurable=" + this.getIsDurable() + ")";
   }
}
