package com.chang.common.function.booleanHandle.A;

public class LfElseFunctionA {
   public static BranchHandleA isTureOrFalse(boolean b) {
      return (trueHandleA, falseHandleA) -> {
         if (b) {
            trueHandleA.doWith();
         } else {
            falseHandleA.doWith();
         }

      };
   }
}
