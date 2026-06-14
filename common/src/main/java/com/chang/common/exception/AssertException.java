package com.chang.common.exception;

public class AssertException extends BaseException {
   private static final long serialVersionUID = -3662513307871932162L;

   public AssertException(IErrorCode errCode, String errMessage) {
      super(errMessage);
      this.setErrCode(errCode);
   }

   public AssertException(IErrorCode errCode, String errMessage, Throwable e) {
      super(errMessage, e);
      this.setErrCode(errCode);
   }
}
