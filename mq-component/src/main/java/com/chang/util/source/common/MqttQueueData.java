package com.chang.util.source.common;

public class MqttQueueData {
   private String topic;
   private Object object;

   public String getTopic() {
      return this.topic;
   }

   public Object getObject() {
      return this.object;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setObject(Object object) {
      this.object = object;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MqttQueueData)) {
         return false;
      } else {
         MqttQueueData other = (MqttQueueData)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$topic = this.getTopic();
            Object other$topic = other.getTopic();
            if (this$topic == null) {
               if (other$topic != null) {
                  return false;
               }
            } else if (!this$topic.equals(other$topic)) {
               return false;
            }

            Object this$object = this.getObject();
            Object other$object = other.getObject();
            if (this$object == null) {
               if (other$object != null) {
                  return false;
               }
            } else if (!this$object.equals(other$object)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MqttQueueData;
   }

   public int hashCode() {
      int result = 1;
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      Object $object = this.getObject();
      result = result * 59 + ($object == null ? 43 : $object.hashCode());
      return result;
   }

   public String toString() {
      return "MqttQueueData(topic=" + this.getTopic() + ", object=" + this.getObject() + ")";
   }
}
