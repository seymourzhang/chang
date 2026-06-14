package com.chang.common.function.exception;

import cn.hutool.core.util.ObjectUtil;
import com.chang.common.exception.CustomException;

public class ExceptionUtils {
   public static ThrowExceptionFunction isTure(boolean b) {
      return (errCode) -> {
         if (b) {
            throw new CustomException(errCode);
         }
      };
   }

   public static ThrowExceptionFunction isFalse(boolean b) {
      return (errCode) -> {
         if (!b) {
            throw new CustomException(errCode);
         }
      };
   }

   public static ThrowExceptionFunction isNull(Object o) {
      return (errCode) -> {
         if (ObjectUtil.isNull(o)) {
            throw new CustomException(errCode);
         }
      };
   }
}
