package com.chang.common.exception;

public class DefaultBaseException extends BaseException {
   private static final long serialVersionUID = 6416966378895834357L;

   public DefaultBaseException(IErrorCode errCode, String errMessage) {
      super(errMessage);
      this.setErrCode(errCode);
   }

   public DefaultBaseException(IErrorCode errCode, String errMessage, Throwable e) {
      super(errMessage, e);
      this.setErrCode(errCode);
   }
}
