package com.chang.uclass.bytecode;

import com.chang.uclass.bytecode.FeatureCodec;
import com.chang.uclass.bytecode.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Configure {
   private String ip;
   private Integer telnetPort;
   private Integer httpPort;
   private Long javaPid;
   private String core;
   private String agent;
   private String tunnelServer;
   private String agentId;
   private String username;
   private String password;
   private String outputPath;
   private String enhanceLoaders;
   private String appName;
   private String statUrl;
   private Long sessionTimeout;
   private String disabledCommands;
   private Boolean localConnectionNonAuth;

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public Integer getTelnetPort() {
      return this.telnetPort;
   }

   public void setTelnetPort(int telnetPort) {
      this.telnetPort = telnetPort;
   }

   public void setHttpPort(int httpPort) {
      this.httpPort = httpPort;
   }

   public Integer getHttpPort() {
      return this.httpPort;
   }

   public long getJavaPid() {
      return this.javaPid;
   }

   public void setJavaPid(long javaPid) {
      this.javaPid = javaPid;
   }

   public String getAgent() {
      return this.agent;
   }

   public void setAgent(String agent) {
      this.agent = agent;
   }

   public String getCore() {
      return this.core;
   }

   public void setCore(String core) {
      this.core = core;
   }

   public Long getSessionTimeout() {
      return this.sessionTimeout;
   }

   public void setSessionTimeout(long sessionTimeout) {
      this.sessionTimeout = sessionTimeout;
   }

   public String getTunnelServer() {
      return this.tunnelServer;
   }

   public void setTunnelServer(String tunnelServer) {
      this.tunnelServer = tunnelServer;
   }

   public String getAgentId() {
      return this.agentId;
   }

   public void setAgentId(String agentId) {
      this.agentId = agentId;
   }

   public String getStatUrl() {
      return this.statUrl;
   }

   public void setStatUrl(String statUrl) {
      this.statUrl = statUrl;
   }

   public String getAppName() {
      return this.appName;
   }

   public void setAppName(String appName) {
      this.appName = appName;
   }

   public String getEnhanceLoaders() {
      return this.enhanceLoaders;
   }

   public void setEnhanceLoaders(String enhanceLoaders) {
      this.enhanceLoaders = enhanceLoaders;
   }

   public String getOutputPath() {
      return this.outputPath;
   }

   public void setOutputPath(String outputPath) {
      this.outputPath = outputPath;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getDisabledCommands() {
      return this.disabledCommands;
   }

   public void setDisabledCommands(String disabledCommands) {
      this.disabledCommands = disabledCommands;
   }

   public boolean isLocalConnectionNonAuth() {
      return this.localConnectionNonAuth != null && this.localConnectionNonAuth;
   }

   public void setLocalConnectionNonAuth(boolean localConnectionNonAuth) {
      this.localConnectionNonAuth = localConnectionNonAuth;
   }

   public String toString() {
      Map<String, String> map = new HashMap();
      Iterator var2 = ReflectUtils.getFields(Configure.class).iterator();

      while(var2.hasNext()) {
         Field field = (Field)var2.next();
         if (!Modifier.isStatic(field.getModifiers())) {
            try {
               Object fieldValue = ReflectUtils.getFieldValueByField(this, field);
               if (fieldValue != null) {
                  map.put(field.getName(), String.valueOf(fieldValue));
               }
            } catch (Throwable var5) {
            }
         }
      }

      return FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toString(map);
   }

   public static Configure toConfigure(String toString) throws IllegalAccessException {
      Configure configure = new Configure();
      Map<String, String> map = FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toMap(toString);
      Iterator var3 = map.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var3.next();
         Field field = ReflectUtils.getField(Configure.class, (String)entry.getKey());
         if (null != field && !Modifier.isStatic(field.getModifiers())) {
            ReflectUtils.set(field, ReflectUtils.valueOf(field.getType(), (String)entry.getValue()), configure);
         }
      }

      return configure;
   }
}
