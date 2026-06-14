package com.chang.common.exception;

public class CustomException extends BaseException {
   public CustomException(IErrorCode errCode) {
      super((IErrorCode) errCode);
   }

   public CustomException(String errCode) {
      super(errCode);
   }
}
