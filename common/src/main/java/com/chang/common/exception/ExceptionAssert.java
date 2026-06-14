package com.chang.common.exception;

public class ExceptionAssert {
   public static void isTrue(boolean expression, String message) {
      if (!expression) {
         throw new AssertException(AssertErrorCode.ASSERT_ISTRUE_ERROR, message);
      }
   }

   public static void isTrue(boolean expression, IErrorCode errCode, String message) {
      if (!expression) {
         throw new AssertException(AssertErrorCode.ASSERT_ISTRUE_ERROR, message);
      }
   }

   public static void isNull(Object o, String message) {
      if (o == null) {
         throw new AssertException(AssertErrorCode.ASSERT_ISNULL_ERROR, message);
      }
   }

   public static void isNull(Object o, IErrorCode errCode, String message) {
      if (o == null) {
         throw new AssertException(AssertErrorCode.ASSERT_ISNULL_ERROR, message);
      }
   }
}
