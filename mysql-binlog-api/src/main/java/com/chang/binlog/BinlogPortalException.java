package com.chang.binlog;

public class BinlogPortalException extends Exception {
   public BinlogPortalException() {
   }

   public BinlogPortalException(String message) {
      super(message);
   }

   public BinlogPortalException(String message, Throwable cause) {
      super(message, cause);
   }

   public BinlogPortalException(Throwable cause) {
      super(cause);
   }

   protected BinlogPortalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
