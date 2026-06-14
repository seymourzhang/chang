package com.chang;

import com.alibaba.fastjson.JSONObject;
import com.chang.GetLogForFun;
import com.chang.common.CommUtils;
import com.chang.common.LogUtil;
import com.chang.until.reflect.ReflectionUtils;
import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Configuration
public class LogHandle {
   private static final Long SIZE = 1024L;

   private boolean hasIndex(int num, int[] nums) {
      int[] var3 = nums;
      int var4 = nums.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int o = var3[var5];
         if (num == o) {
            return true;
         }
      }

      return false;
   }

   @Pointcut("@annotation(com.chang.GetLogForFun)")
   public void GetLogForFunDo() {
   }

   @Around("GetLogForFunDo()")
   public Object LogCatch(ProceedingJoinPoint joinPoint) throws Throwable {
      LogUtil LOG = LogUtil.getLogger(joinPoint.getTarget().getClass());
      LOG.setNeedCheck(false);
      Object result = null;
      if (!LOG.getEndAble()) {
         result = joinPoint.proceed();
         return result;
      } else {
         String className = joinPoint.getTarget().getClass().getName();
         Signature s = joinPoint.getSignature();
         MethodSignature ms = (MethodSignature)s;
         Method method = ms.getMethod();
         String methodName = method.getName();
         GetLogForFun logForFun = (GetLogForFun)method.getDeclaredAnnotation(GetLogForFun.class);
         Class clazz = logForFun.classType();
         LogLevel logLevel = logForFun.loglevel();
         String[] name = logForFun.excludeFieldName();
         Object[] objs = joinPoint.getArgs();
         List<Object> paramsList = new ArrayList();
         JSONObject params = new JSONObject();
         JSONObject results = new JSONObject();

         label126:
         for(int i = 0; i < objs.length; ++i) {
            Object obj = objs[i];
            if (null != obj && !(obj instanceof ServletRequest) && !(obj instanceof ServletResponse) && !(obj instanceof File) && !(obj instanceof Closeable) && !(obj instanceof MultipartFile)) {
               if (!ReflectionUtils.isBaseType(obj)) {
                  Map<String, Class> fieldsMap = ReflectionUtils.getFieldsMap(obj.getClass());
                  boolean illegalBean = false;
                  Iterator var21 = fieldsMap.entrySet().iterator();

                  label119:
                  while(true) {
                     Class fieldClass;
                     do {
                        if (!var21.hasNext()) {
                           if (illegalBean) {
                              continue label126;
                           }
                           break label119;
                        }

                        Map.Entry<String, Class> entry = (Map.Entry)var21.next();
                        fieldClass = (Class)entry.getValue();
                     } while(!fieldClass.equals(ServletRequest.class) && !fieldClass.equals(ServletResponse.class) && !fieldClass.equals(Closeable.class) && !fieldClass.equals(File.class) && !fieldClass.equals(MultipartFile.class));

                     illegalBean = true;
                  }
               }

               if (!ReflectionUtils.isBaseType(clazz) && !clazz.equals(Object.class)) {
                  if (obj.getClass().equals(clazz) && name.length > 0) {
                     obj = CommUtils.getJSONStringFromObject(obj, name);
                  }

                  if (obj.getClass().equals(clazz) && name.length == 0) {
                     continue;
                  }
               }

               paramsList.add(obj);
            }
         }

         if (paramsList.size() > 0) {
            Long paramsDataSize = CommUtils.getDataSize(paramsList);
            if (paramsDataSize > SIZE) {
               params.put("params", "[!WARNING!] parameter big size: " + (double)paramsDataSize / 1024.0 / 1024.0 + "M");
            } else {
               params.put("params", paramsList);
            }
         } else {
            params.put("params", "null");
         }

         if (logLevel == LogLevel.DEBUG) {
            LOG.debug(className + "." + methodName, params, LogUtil.START_METHOD);
         } else if (logLevel == LogLevel.INFO) {
            LOG.info(className + "." + methodName, params, LogUtil.START_METHOD);
         } else if (logLevel == LogLevel.WARN) {
            LOG.warn(className + "." + methodName, params, LogUtil.START_METHOD);
         } else if (logLevel == LogLevel.ERROR) {
            LOG.error(className + "." + methodName, params, LogUtil.START_METHOD);
         }

         long startTime = CommUtils.getNowMilli();
         result = joinPoint.proceed();
         long endTime = CommUtils.getNowMilli();
         Long resultDataSize = CommUtils.getDataSize(result);
         if (resultDataSize > SIZE) {
            results.put("result", "[!WARNING!] result big size: " + (double)resultDataSize / 1024.0 / 1024.0 + "M");
         } else {
            results.put("result", result);
         }

         results.put("callTime", endTime - startTime);
         if (logLevel == LogLevel.DEBUG) {
            LOG.debug(className + "." + methodName, results, LogUtil.END_METHOD);
         } else if (logLevel == LogLevel.INFO) {
            LOG.info(className + "." + methodName, results, LogUtil.END_METHOD);
         } else if (logLevel == LogLevel.WARN) {
            LOG.warn(className + "." + methodName, results, LogUtil.END_METHOD);
         } else if (logLevel == LogLevel.ERROR) {
            LOG.error(className + "." + methodName, results, LogUtil.END_METHOD);
         }

         return result;
      }
   }
}
