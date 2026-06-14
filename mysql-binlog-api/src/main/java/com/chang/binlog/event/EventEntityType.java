package com.chang.binlog.event;

public enum EventEntityType {
   UPDATE("update"),
   INSERT("insert"),
   DELETE("delete");

   String desc;

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   private EventEntityType(String desc) {
      this.desc = desc;
   }
}
