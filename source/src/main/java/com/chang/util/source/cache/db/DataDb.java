package com.chang.util.source.cache.db;

import java.util.Arrays;

public class DataDb {
   private Long id;
   private String topic;
   private Long timestamp;
   private byte[] date;

   public Long getId() {
      return this.id;
   }

   public String getTopic() {
      return this.topic;
   }

   public Long getTimestamp() {
      return this.timestamp;
   }

   public byte[] getDate() {
      return this.date;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
   }

   public void setDate(byte[] date) {
      this.date = date;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DataDb)) {
         return false;
      } else {
         DataDb other = (DataDb)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$id = this.getId();
            Object other$id = other.getId();
            if (this$id == null) {
               if (other$id != null) {
                  return false;
               }
            } else if (!this$id.equals(other$id)) {
               return false;
            }

            Object this$timestamp = this.getTimestamp();
            Object other$timestamp = other.getTimestamp();
            if (this$timestamp == null) {
               if (other$timestamp != null) {
                  return false;
               }
            } else if (!this$timestamp.equals(other$timestamp)) {
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

            if (!Arrays.equals(this.getDate(), other.getDate())) {
               return false;
            } else {
               return true;
            }
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof DataDb;
   }

   public int hashCode() {
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $timestamp = this.getTimestamp();
      result = result * 59 + ($timestamp == null ? 43 : $timestamp.hashCode());
      Object $topic = this.getTopic();
      result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
      result = result * 59 + Arrays.hashCode(this.getDate());
      return result;
   }

   public String toString() {
      return "DataDb(id=" + this.getId() + ", topic=" + this.getTopic() + ", timestamp=" + this.getTimestamp() + ", date=" + Arrays.toString(this.getDate()) + ")";
   }
}
