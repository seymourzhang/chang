package com.chang.uclass.bytecode.function;

@FunctionalInterface
public interface ThrowableFunction<T, R> {
   R apply(T var1) throws Throwable;

   default R execute(T t) throws RuntimeException {
      R result = null;

      try {
         result = this.apply(t);
         return result;
      } catch (Throwable var4) {
         throw new RuntimeException(var4.getCause());
      }
   }

   static <T, R> R execute(T t, ThrowableFunction<T, R> function) {
      return function.execute(t);
   }
}
