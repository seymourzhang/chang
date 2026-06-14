package com.chang.until.timeTaskApi.lib;

import cn.hutool.core.lang.Assert;
import java.lang.reflect.UndeclaredThrowableException;

public class DelegatingErrorHandlingRunnable implements Runnable {
   private final Runnable delegate;
   private final ErrorHandler errorHandler;

   public DelegatingErrorHandlingRunnable(Runnable delegate, ErrorHandler errorHandler) {
      Assert.notNull(delegate, "Delegate must not be null", new Object[0]);
      Assert.notNull(errorHandler, "ErrorHandler must not be null", new Object[0]);
      this.delegate = delegate;
      this.errorHandler = errorHandler;
   }

   public void run() {
      try {
         this.delegate.run();
      } catch (UndeclaredThrowableException var2) {
         this.errorHandler.handleError(var2.getUndeclaredThrowable());
      } catch (Throwable var3) {
         this.errorHandler.handleError(var3);
      }

   }

   public String toString() {
      return "DelegatingErrorHandlingRunnable for " + this.delegate;
   }
}
