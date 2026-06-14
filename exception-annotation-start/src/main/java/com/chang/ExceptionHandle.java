package com.chang;

import com.chang.common.LogUtil;
import com.chang.common.exception.BaseException;
import com.chang.common.exception.DefaultBaseException;
import com.chang.common.exception.DefaultExceptionHandler;
import com.chang.common.exception.Response;
import com.chang.common.exception.SYSCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Aspect
@Configuration
public class ExceptionHandle {
   private static final Logger log = LoggerFactory.getLogger(ExceptionHandle.class);
   private static final LogUtil LOG = LogUtil.getLogger(ExceptionHandle.class);

   @Pointcut("@annotation(com.chang.GetException)")
   public void GetExceptionDo() {
   }

   @Around("GetExceptionDo()")
   public Object ExceptionCatch(ProceedingJoinPoint joinPoint) throws BaseException {
      Object result = null;

      try {
         result = joinPoint.proceed();
         return result;
      } catch (Throwable var7) {
         log.error(var7.getMessage(), var7);
         Response response = new Response();
         DefaultExceptionHandler.singleton.handleException(response, var7);
         if (var7 instanceof BaseException) {
            BaseException baseException = (BaseException)var7;
            baseException.setResponse(response);
            throw baseException;
         } else {
            String message = null;
            if (StringUtils.hasText(var7.getMessage())) {
               message = var7.getClass().getName() + "-:-" + var7.getMessage();
            } else {
               message = var7.getClass().getName();
            }

            DefaultBaseException defaultBaseException = new DefaultBaseException(SYSCode.SYS_ERROR, message);
            defaultBaseException.setResponse(response);
            throw defaultBaseException;
         }
      }
   }
}
