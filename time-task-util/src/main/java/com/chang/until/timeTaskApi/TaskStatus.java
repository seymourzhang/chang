package com.chang.until.timeTaskApi;

public enum TaskStatus {
   ALIVE("alive"),
   CANCEL("cancel"),
   Done("done");

   private String type;

   private TaskStatus(String type) {
      this.type = type;
   }

   public String getValue() {
      return this.type;
   }
}
