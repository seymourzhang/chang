package com.chang.common.exception;

public enum SYSCode implements IErrorCode {
   SYS_ERROR("SYS_ERROR", "system error"),
   BUS_ERROR("BUS_ERROR", "business error");

   private String errCode;
   private String errDesc;

   private SYSCode(String errCode, String errDesc) {
      this.errCode = errCode;
      this.errDesc = errDesc;
   }

   public String getErrCode() {
      return this.errCode;
   }

   public String getErrDesc() {
      return this.errDesc;
   }
}
