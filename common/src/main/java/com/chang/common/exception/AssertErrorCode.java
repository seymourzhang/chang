package com.chang.common.exception;

public enum AssertErrorCode implements IErrorCode {
   ASSERT_ISTRUE_ERROR("ASSERT_ISTRUE_ERROR", "The expectation is not TRUE"),
   ASSERT_ISNULL_ERROR("ASSERT_ISNULL_ERROR", "Object is null");

   private String errCode;
   private String errDesc;

   private AssertErrorCode(String errCode, String errDesc) {
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
