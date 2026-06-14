package com.chang.common.exception;

import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExceptionHandler implements IExceptionHandler {
   private Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
   public static final DefaultExceptionHandler singleton = new DefaultExceptionHandler();

   private DefaultExceptionHandler() {
   }

   public void handleException(Response response, Throwable exception) {
      this.buildResponse(response, exception);
      this.printLog(response, exception);
   }

   private void printLog(Response response, Throwable exception) {
      this.logger.error(this.buildErrorMsg(response), exception);
   }

   private String buildErrorMsg(Response response) {
      return "errorCode: " + response.getErrCode() + " errorMsg:" + response.getErrMessage();
   }

   private void buildResponse(Response response, Throwable exception) {
      if (exception instanceof BaseException) {
         IErrorCode errCode = ((BaseException)exception).getErrCode();
         if (ObjectUtil.isNull(errCode)) {
            response.setErrCode(SYSCode.BUS_ERROR.getErrCode());
            response.setErrMessage(exception.getMessage());
         } else {
            response.setErrCode(errCode.getErrCode());
            response.setErrMessage(errCode.getErrDesc());
         }
      } else {
         response.setErrCode(SYSCode.SYS_ERROR.getErrCode());
         response.setErrMessage(exception.getClass().getName() + "-:-" + exception.getMessage());
      }

      response.setSuccess(false);
   }
}
