package com.chang.common.exception;

import java.io.Serializable;

public abstract class BaseException extends RuntimeException {
   private static final long serialVersionUID = 75689773186069527L;
   private IErrorCode errCode;
   private Response response;

   public BaseException(String errMessage) {
      super(errMessage);
   }

   public BaseException(String errMessage, Throwable e) {
      super(errMessage, e);
   }

   public BaseException(IErrorCode errCode) {
      super(errCode.getErrDesc());
      this.errCode = errCode;
      this.response = Response.getInstance().buildFailure(errCode);
   }

   public <T extends Serializable> BaseException(IErrorCode errCode, T data) {
      this.errCode = errCode;
      this.response = Response.getInstance().buildFailure((IErrorCode)errCode, (Object)data);
   }

   public void setErrCode(IErrorCode errCode) {
      this.errCode = errCode;
   }

   public IErrorCode getErrCode() {
      return this.errCode;
   }

   public void setResponse(Response response) {
      this.response = response;
   }

   public Response getResponse() {
      return this.response;
   }
}
