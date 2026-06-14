package com.chang.util.source.rocketmq;

public enum ConsumerMessageType {
   CONCURRENTLY("Concurrently"),
   ORDERLY("Orderly");

   private final String messageType;

   public String getMessageType() {
      return this.messageType;
   }

   private ConsumerMessageType(String messageType) {
      this.messageType = messageType;
   }
}
