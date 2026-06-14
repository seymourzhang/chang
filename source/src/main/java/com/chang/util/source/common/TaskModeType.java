package com.chang.util.source.common;

public enum TaskModeType {
   ONCE("ONCE"),
   LOOP_DAY("LOOP_DAY"),
   LOOP_MONTH("LOOP_MONTH"),
   EVERY_SECOND("EVERY_SECOND"),
   EVERY_MINUTE("EVERY_MINUTE"),
   EVERY_HOUR("EVERY_HOUR");

   private final String taskModeType;

   public String getTaskModeType() {
      return this.taskModeType;
   }

   private TaskModeType(String taskModeType) {
      this.taskModeType = taskModeType;
   }
}
