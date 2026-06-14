package com.chang.uclass.bytecode.function;

@FunctionalInterface
public interface ThrowableAction {
   void execute() throws Throwable;

   static void execute(ThrowableAction action) throws RuntimeException {
      try {
         action.execute();
      } catch (Throwable var2) {
         throw new RuntimeException(var2);
      }
   }
}
