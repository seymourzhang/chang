package com.chang.common.function.exception;

import com.chang.common.exception.IErrorCode;

@FunctionalInterface
public interface ThrowExceptionFunction {
   void throwMessage(IErrorCode var1);
}
