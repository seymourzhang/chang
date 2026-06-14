package com.chang.util.source.spring.start.config;

public enum RedisModeType {
   SINGLE("single"),
   SENTINEL("sentinel"),
   CLUSTER("cluster"),
   MASTER_SLAVE("master-slave");

   private final String redisModeType;

   public String getRedisModeType() {
      return this.redisModeType;
   }

   private RedisModeType(String redisModeType) {
      this.redisModeType = redisModeType;
   }
}
