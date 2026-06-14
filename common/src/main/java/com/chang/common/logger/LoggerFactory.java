package com.chang.common.logger;

public class LoggerFactory {
   private static boolean useSysLogger = false;

   public static Logger getLogger(Class<?> clazz) {
      if (useSysLogger) {
         return SysLogger.getLogger(clazz);
      } else {
         org.slf4j.Logger slfjLogger = org.slf4j.LoggerFactory.getLogger(clazz);
         return new SLFJLogger(slfjLogger);
      }
   }

   public static Logger getLogger(String loggerName) {
      if (useSysLogger) {
         return SysLogger.getLogger(loggerName);
      } else {
         org.slf4j.Logger slfjLogger = org.slf4j.LoggerFactory.getLogger(loggerName);
         return new SLFJLogger(slfjLogger);
      }
   }

   public static void activateSysLogger() {
      useSysLogger = true;
   }

   public static void deactivateSysLogger() {
      useSysLogger = false;
   }
}
