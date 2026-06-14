package com.chang.netty.until.common;

import java.util.concurrent.TimeUnit;

public class InitializerConfig {
   private long readerIdleTime;
   private long writerIdleTime;
   private long allIdleTime;
   private TimeUnit unit;
   private DataType dataType;

   InitializerConfig(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit, DataType dataType) {
      this.readerIdleTime = readerIdleTime;
      this.writerIdleTime = writerIdleTime;
      this.allIdleTime = allIdleTime;
      this.unit = unit;
      this.dataType = dataType;
   }

   public static InitializerConfigBuilder builder() {
      return new InitializerConfigBuilder();
   }

   public void setReaderIdleTime(long readerIdleTime) {
      this.readerIdleTime = readerIdleTime;
   }

   public void setWriterIdleTime(long writerIdleTime) {
      this.writerIdleTime = writerIdleTime;
   }

   public void setAllIdleTime(long allIdleTime) {
      this.allIdleTime = allIdleTime;
   }

   public void setUnit(TimeUnit unit) {
      this.unit = unit;
   }

   public void setDataType(DataType dataType) {
      this.dataType = dataType;
   }

   public long getReaderIdleTime() {
      return this.readerIdleTime;
   }

   public long getWriterIdleTime() {
      return this.writerIdleTime;
   }

   public long getAllIdleTime() {
      return this.allIdleTime;
   }

   public TimeUnit getUnit() {
      return this.unit;
   }

   public DataType getDataType() {
      return this.dataType;
   }

   public static class InitializerConfigBuilder {
      private long readerIdleTime;
      private long writerIdleTime;
      private long allIdleTime;
      private TimeUnit unit;
      private DataType dataType;

      InitializerConfigBuilder() {
      }

      public InitializerConfigBuilder readerIdleTime(long readerIdleTime) {
         this.readerIdleTime = readerIdleTime;
         return this;
      }

      public InitializerConfigBuilder writerIdleTime(long writerIdleTime) {
         this.writerIdleTime = writerIdleTime;
         return this;
      }

      public InitializerConfigBuilder allIdleTime(long allIdleTime) {
         this.allIdleTime = allIdleTime;
         return this;
      }

      public InitializerConfigBuilder unit(TimeUnit unit) {
         this.unit = unit;
         return this;
      }

      public InitializerConfigBuilder dataType(DataType dataType) {
         this.dataType = dataType;
         return this;
      }

      public InitializerConfig build() {
         return new InitializerConfig(this.readerIdleTime, this.writerIdleTime, this.allIdleTime, this.unit, this.dataType);
      }

      public String toString() {
         return "InitializerConfig.InitializerConfigBuilder(readerIdleTime=" + this.readerIdleTime + ", writerIdleTime=" + this.writerIdleTime + ", allIdleTime=" + this.allIdleTime + ", unit=" + this.unit + ", dataType=" + this.dataType + ")";
      }
   }
}
