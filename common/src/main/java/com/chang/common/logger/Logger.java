package com.chang.common.logger;

public interface Logger {
   void debug(String var1);

   void debug(String var1, Object... var2);

   void info(String var1);

   void info(String var1, Object... var2);

   void warn(String var1);

   void warn(String var1, Object... var2);

   void error(String var1);

   void error(String var1, Object... var2);

   void error(String var1, Throwable var2);
}
