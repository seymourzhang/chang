package com.chang.common;

import com.alibaba.fastjson.JSONObject;
import com.chang.common.logger.Logger;
import com.chang.common.logger.LoggerFactory;
import com.chang.common.Config.ServiceInfo;
import com.chang.until.spring.context.SpringContext;

public class LogUtil {
   private static final Long SIZE = 102400L;
   private static final ThreadLocal<JSONObject> jsonHolder = new ThreadLocal();
   public static String START_METHOD = "START_METHOD";
   public static String END_METHOD = "END_METHOD";
   private Logger logger = null;
   private Boolean needCheck = true;

   public void setNeedCheck(Boolean needCheck) {
      this.needCheck = needCheck;
   }

   private LogUtil(Class clazz) {
      this.logger = LoggerFactory.getLogger(clazz);
   }

   public static LogUtil getLogger(Class clazz) {
      return new LogUtil(clazz);
   }

   private ServiceInfo getServiceInfo() {
      return (ServiceInfo)SpringContext.getBean(ServiceInfo.class);
   }

   private void setHostAndIP(JSONObject text) {
      ServiceInfo serviceInfo = this.getServiceInfo();
      if (null != serviceInfo) {
         text.put("host", serviceInfo.getHostIp());
         text.put("port", serviceInfo.getPort());
         text.put("appName", serviceInfo.getAppName());
         text.put("projectName", serviceInfo.getProjectName());
      }

   }

   public Boolean getEndAble() {
      ServiceInfo serviceInfo = this.getServiceInfo();
      return null != serviceInfo ? serviceInfo.getEndable() : true;
   }

   private boolean getDebugOn() {
      ServiceInfo serviceInfo = this.getServiceInfo();
      return null != serviceInfo ? serviceInfo.isDebugOn() : true;
   }

   private boolean getInfoOn() {
      ServiceInfo serviceInfo = this.getServiceInfo();
      return null != serviceInfo ? serviceInfo.isInfoOn() : true;
   }

   private boolean getWarnOn() {
      ServiceInfo serviceInfo = this.getServiceInfo();
      return null != serviceInfo ? serviceInfo.isWarnOn() : true;
   }

   private boolean getErrorOn() {
      ServiceInfo serviceInfo = this.getServiceInfo();
      return null != serviceInfo ? serviceInfo.isErrorOn() : true;
   }

   private JSONObject getJsonObject() {
      JSONObject jsonObject = (JSONObject)jsonHolder.get();
      if (jsonObject == null) {
         jsonObject = new JSONObject(true);
         jsonHolder.set(jsonObject);
      }

      jsonObject.clear();
      return jsonObject;
   }

   private Object checkSize(Object data) {
      if (this.needCheck) {
         long size = CommUtils.getDataSize(data);
         return size > SIZE ? "[!WARNING!] find data big size: " + (double)size / 1024.0 / 1024.0 + "M" : data;
      } else {
         return data;
      }
   }

   public void debug(String message) {
      if (this.getDebugOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.debug(text.toJSONString());
         text.clear();
      }
   }

   public void debug(String message, Object... arguments) {
      if (this.getDebugOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.debug(text.toJSONString(), arguments);
         text.clear();
      }
   }

   public void debug(String message, Throwable t) {
      if (this.getDebugOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("throwable", t.getMessage());
         this.setHostAndIP(text);
         this.logger.debug(text.toJSONString(), t);
         text.clear();
      }
   }

   public void debug(String method, JSONObject params, String message) {
      if (this.getDebugOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("params", this.checkSize(params));
         text.put("method", method);
         this.setHostAndIP(text);
         this.logger.debug(text.toJSONString());
         text.clear();
      }
   }

   public void startCallDebug(String method, String callMethod, long time, JSONObject params) {
      if (this.getDebugOn()) {
         JSONObject text = this.getJsonObject();
         text.put("method", method);
         text.put("callMethod", callMethod);
         text.put("params", this.checkSize(params));
         this.setHostAndIP(text);
         this.logger.debug(text.toJSONString());
         text.clear();
      }
   }

   public void endCallDebug(String method, String callMethod, long time, long calltime, String result, JSONObject params) {
      if (this.getDebugOn()) {
         JSONObject text = this.getJsonObject();
         text.put("params", this.checkSize(params));
         text.put("result", this.checkSize(result));
         text.put("method", method);
         text.put("callMethod", callMethod);
         text.put("callEndTime", time);
         text.put("callTime", calltime);
         this.setHostAndIP(text);
         this.logger.debug(text.toJSONString());
         text.clear();
      }
   }

   public void info(String message) {
      if (this.getInfoOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.info(text.toJSONString());
         text.clear();
      }
   }

   public void info(String message, Object... arguments) {
      if (this.getInfoOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.info(text.toJSONString(), arguments);
         text.clear();
      }
   }

   public void info(String message, Throwable t) {
      if (this.getInfoOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("throwable", t.getMessage());
         this.setHostAndIP(text);
         this.logger.info(text.toJSONString(), t);
         text.clear();
      }
   }

   public void info(String method, JSONObject params, String message) {
      if (this.getInfoOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("params", this.checkSize(params));
         text.put("method", method);
         this.setHostAndIP(text);
         this.logger.info(text.toJSONString());
         text.clear();
      }
   }

   public void startCallInfo(String method, String callMethod, long time, JSONObject params) {
      if (this.getInfoOn()) {
         JSONObject text = this.getJsonObject();
         text.put("params", this.checkSize(params));
         text.put("method", method);
         text.put("callMethod", callMethod);
         text.put("callStartTime", time);
         this.setHostAndIP(text);
         this.logger.info(text.toJSONString());
         text.clear();
      }
   }

   public void endCallInfo(String method, String callMethod, long time, long calltime, String result, JSONObject params) {
      if (this.getInfoOn()) {
         JSONObject text = this.getJsonObject();
         text.put("params", this.checkSize(params));
         text.put("result", this.checkSize(result));
         text.put("method", method);
         text.put("callMethod", callMethod);
         text.put("callEndTime", time);
         text.put("callTime", calltime);
         this.setHostAndIP(text);
         this.logger.info(text.toJSONString());
         text.clear();
      }
   }

   public void warn(String message) {
      if (this.getWarnOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.warn(text.toJSONString());
         text.clear();
      }
   }

   public void warn(String message, Object... arguments) {
      if (this.getWarnOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.warn(text.toJSONString(), arguments);
         text.clear();
      }
   }

   public void warn(String message, Throwable t) {
      if (this.getWarnOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("throwable", t.getMessage());
         this.setHostAndIP(text);
         this.logger.warn(text.toJSONString(), t);
         text.clear();
      }
   }

   public void warn(String method, JSONObject params, String message) {
      if (this.getWarnOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("params", this.checkSize(params));
         text.put("method", method);
         this.setHostAndIP(text);
         this.logger.warn(text.toJSONString());
         text.clear();
      }
   }

   public void startCallWarn(String method, String callMethod, long time, JSONObject params) {
      if (this.getWarnOn()) {
         JSONObject text = this.getJsonObject();
         text.put("params", this.checkSize(params));
         text.put("method", method);
         text.put("callMethod", callMethod);
         text.put("callStartTime", time);
         this.setHostAndIP(text);
         this.logger.warn(text.toJSONString());
         text.clear();
      }
   }

   public void endCallWarn(String method, String callMethod, long time, long calltime, String result, JSONObject params) {
      if (this.getWarnOn()) {
         JSONObject text = this.getJsonObject();
         text.put("params", this.checkSize(params));
         text.put("method", method);
         text.put("callMethod", callMethod);
         text.put("callEndTime", time);
         text.put("callTime", calltime);
         text.put("result", this.checkSize(result));
         this.setHostAndIP(text);
         this.logger.warn(text.toJSONString());
         text.clear();
      }
   }

   public void error(String message) {
      if (this.getErrorOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.error(text.toJSONString());
         text.clear();
      }
   }

   public void error(String message, Object... arguments) {
      if (this.getErrorOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         this.setHostAndIP(text);
         this.logger.error(text.toJSONString(), arguments);
         text.clear();
      }
   }

   public void error(String message, Throwable t) {
      if (this.getErrorOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("throwable", t.getMessage());
         this.setHostAndIP(text);
         this.logger.error(text.toJSONString(), t);
         text.clear();
      }
   }

   public void error(String method, JSONObject params, String message) {
      if (this.getErrorOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("params", this.checkSize(params));
         text.put("method", method);
         this.setHostAndIP(text);
         this.logger.error(text.toJSONString());
         text.clear();
      }
   }

   public void error(String method, JSONObject params, String message, Throwable t) {
      if (this.getErrorOn()) {
         JSONObject text = this.getJsonObject();
         text.put("message", this.checkSize(message));
         text.put("params", this.checkSize(params));
         text.put("method", method);
         text.put("throwable", t.getMessage());
         this.setHostAndIP(text);
         this.logger.error(text.toJSONString(), t);
         text.clear();
      }
   }
}
