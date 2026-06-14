package com.chang.uclass.bytecode;

public class NoSuchMethodException extends RuntimeException {
   private static final long serialVersionUID = -2725364246023268766L;

   public NoSuchMethodException() {
   }

   public NoSuchMethodException(String msg) {
      super(msg);
   }
}
