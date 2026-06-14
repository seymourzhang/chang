package com.chang.common;

public enum States {
   VALID("N", "valid"),
   INVALID("D", "invalid"),
   ONLINE("1", "online"),
   OFFLINE("0", "offline"),
   READY("0", "ready"),
   DONE("1", "done");

   private String stateCode;
   private String stateDes;

   private States(String stateCode, String stateDes) {
      this.stateCode = stateCode;
      this.stateDes = stateDes;
   }

   public String getStateCode() {
      return this.stateCode;
   }

   public String getStateDes() {
      return this.stateDes;
   }
}
