package com.chang.common.function.booleanHandle;

public class LfElseFunction {
   public static <T> BranchHandle<T> isTureOrFalse(boolean b, Class<T> tClass) {
      return (trueHandle, falseHandle) -> {
         return b ? trueHandle.doWith() : falseHandle.doWith();
      };
   }
}
