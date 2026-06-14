package com.chang.until.timeTaskApi.lib;

import java.lang.reflect.UndeclaredThrowableException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class TaskUtils {
   public static final ErrorHandler LOG_AND_SUPPRESS_ERROR_HANDLER = new LoggingErrorHandler();
   public static final ErrorHandler LOG_AND_PROPAGATE_ERROR_HANDLER = new PropagatingErrorHandler();

   public static DelegatingErrorHandlingRunnable decorateTaskWithErrorHandler(Runnable task, ErrorHandler errorHandler, boolean isRepeatingTask) {
      if (task instanceof DelegatingErrorHandlingRunnable) {
         return (DelegatingErrorHandlingRunnable)task;
      } else {
         ErrorHandler eh = errorHandler != null ? errorHandler : getDefaultErrorHandler(isRepeatingTask);
         return new DelegatingErrorHandlingRunnable(task, eh);
      }
   }

   public static ErrorHandler getDefaultErrorHandler(boolean isRepeatingTask) {
      return isRepeatingTask ? LOG_AND_SUPPRESS_ERROR_HANDLER : LOG_AND_PROPAGATE_ERROR_HANDLER;
   }

   public static void rethrowRuntimeException(Throwable ex) {
      if (ex instanceof RuntimeException) {
         throw (RuntimeException)ex;
      } else if (ex instanceof Error) {
         throw (Error)ex;
      } else {
         throw new UndeclaredThrowableException(ex);
      }
   }

   private static class PropagatingErrorHandler extends LoggingErrorHandler {
      private PropagatingErrorHandler() {
         super(null);
      }

      public void handleError(Throwable t) {
         super.handleError(t);
         TaskUtils.rethrowRuntimeException(t);
      }

      // $FF: synthetic method
      PropagatingErrorHandler(Object x0) {
         this();
      }
   }

   private static class LoggingErrorHandler implements ErrorHandler {
      private final Log logger;

      private LoggingErrorHandler() {
         this.logger = LogFactory.getLog(LoggingErrorHandler.class);
      }

      public void handleError(Throwable t) {
         this.logger.error("Unexpected error occurred in scheduled task", t);
      }

      // $FF: synthetic method
      LoggingErrorHandler(Object x0) {
         this();
      }
   }
}
