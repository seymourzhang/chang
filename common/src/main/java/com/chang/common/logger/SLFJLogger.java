package com.chang.common.logger;

public class SLFJLogger implements Logger {
   private org.slf4j.Logger slfjLogger;

   public SLFJLogger(org.slf4j.Logger slfjLogger) {
      this.slfjLogger = slfjLogger;
   }

   public void debug(String msg) {
      this.slfjLogger.debug(msg);
   }

   public void debug(String msg, Object... args) {
      this.slfjLogger.debug(msg, args);
   }

   public void info(String msg) {
      this.slfjLogger.info(msg);
   }

   public void info(String msg, Object... args) {
      this.slfjLogger.info(msg, args);
   }

   public void warn(String msg) {
      this.slfjLogger.warn(msg);
   }

   public void warn(String msg, Object... args) {
      this.slfjLogger.warn(msg, args);
   }

   public void error(String msg) {
      this.slfjLogger.error(msg);
   }

   public void error(String msg, Object... args) {
      this.slfjLogger.error(msg, args);
   }

   public void error(String msg, Throwable t) {
      this.slfjLogger.error(msg, t);
   }
}
