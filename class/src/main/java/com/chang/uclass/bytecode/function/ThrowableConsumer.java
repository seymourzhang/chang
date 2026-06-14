package com.chang.uclass.bytecode.function;

@FunctionalInterface
public interface ThrowableConsumer<T> {
   void accept(T var1) throws Throwable;

   default void execute(T t) throws RuntimeException {
      try {
         this.accept(t);
      } catch (Throwable var3) {
         throw new RuntimeException(var3.getMessage(), var3.getCause());
      }
   }

   static <T> void execute(T t, ThrowableConsumer<T> consumer) {
      consumer.execute(t);
   }
}
