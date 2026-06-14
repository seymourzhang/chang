package com.chang.hbaselib;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import org.apache.hadoop.security.authentication.util.KerberosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaasConfiguration extends Configuration {
   private static final Logger log = LoggerFactory.getLogger(JaasConfiguration.class);
   private static final Map<String, String> JAAS_BASIC_OPTIONS = new HashMap();
   private static final boolean IS_IBM_JDK = System.getProperty("java.vendor").contains("IBM");
   private static final Map<String, String> KEYTAB_KERBEROS_OPTIONS;
   private static final AppConfigurationEntry KEYTAB_KERBEROS_LOGIN;
   private static final AppConfigurationEntry[] KEYTAB_KERBEROS_CONF;
   private Configuration loginConfig;
   private final String loginContextName;
   private final String keytabFile;
   private final String thePrincipal;
   private final boolean useTicketCache;

   public JaasConfiguration(String loginContextName, String thePrincipal, String keytabFile) throws IOException {
      this(loginContextName, thePrincipal, keytabFile, keytabFile == null || keytabFile.length() == 0);
   }

   private JaasConfiguration(String loginContextName, String thePrincipal, String keytabFile, boolean useTicketCache) throws IOException {
      try {
         this.loginConfig = Configuration.getConfiguration();
      } catch (SecurityException var6) {
         this.loginConfig = null;
      }

      this.loginContextName = loginContextName;
      this.useTicketCache = useTicketCache;
      this.keytabFile = keytabFile;
      this.thePrincipal = thePrincipal;
      this.initKerberosOption();
      log.info("JaasConfiguration loginContextName={} thePrincipal={} useTicketCache={} keytabFile={}", new Object[]{loginContextName, thePrincipal, useTicketCache, keytabFile});
   }

   private void initKerberosOption() throws IOException {
      if (!this.useTicketCache) {
         if (IS_IBM_JDK) {
            KEYTAB_KERBEROS_OPTIONS.put("useKeytab", this.keytabFile);
         } else {
            KEYTAB_KERBEROS_OPTIONS.put("keyTab", this.keytabFile);
            KEYTAB_KERBEROS_OPTIONS.put("useKeyTab", "true");
            KEYTAB_KERBEROS_OPTIONS.put("useTicketCache", this.useTicketCache ? "true" : "false");
         }
      }

      KEYTAB_KERBEROS_OPTIONS.put("principal", this.thePrincipal);
   }

   @Nullable
   public AppConfigurationEntry[] getAppConfigurationEntry(String appName) {
      if (this.loginContextName.equals(appName)) {
         return KEYTAB_KERBEROS_CONF;
      } else {
         return this.loginConfig != null ? this.loginConfig.getAppConfigurationEntry(appName) : null;
      }
   }

   public void setJaasConf() throws IOException {
      File userKeytabFile = new File(this.keytabFile);
      Configuration.setConfiguration(new JaasConfiguration(this.loginContextName, this.thePrincipal, userKeytabFile.getCanonicalPath()));
      Configuration conf = Configuration.getConfiguration();
      if (!(conf instanceof JaasConfiguration)) {
         log.error("javax.security.auth.login.Configuration is not JaasConfiguration.");
         throw new IOException("javax.security.auth.login.Configuration is not JaasConfiguration.");
      } else {
         AppConfigurationEntry[] entrys = conf.getAppConfigurationEntry(this.loginContextName);
         if (entrys == null) {
            log.error("javax.security.auth.login.Configuration has no AppConfigurationEntry named {}.", this.loginContextName);
            throw new IOException("javax.security.auth.login.Configuration has no AppConfigurationEntry named " + this.loginContextName + ".");
         } else {
            boolean checkPrincipal = false;
            boolean checkKeytab = false;

            for(int i = 0; i < entrys.length; ++i) {
               if (entrys[i].getOptions().get("principal").equals(this.thePrincipal)) {
                  checkPrincipal = true;
               }

               if (IS_IBM_JDK) {
                  if (entrys[i].getOptions().get("useKeytab").equals(userKeytabFile.getCanonicalPath())) {
                     checkKeytab = true;
                  }
               } else if (entrys[i].getOptions().get("keyTab").equals(userKeytabFile.getCanonicalPath())) {
                  checkKeytab = true;
               }
            }

            if (!checkPrincipal) {
               log.error("AppConfigurationEntry named {} does not have principal value of {}.", this.loginContextName, this.thePrincipal);
               throw new IOException("AppConfigurationEntry named " + this.loginContextName + " does not have principal value of " + this.thePrincipal + ".");
            } else if (!checkKeytab) {
               log.error("AppConfigurationEntry named {} does not have keyTab value of {}.", this.loginContextName, this.keytabFile);
               throw new IOException("AppConfigurationEntry named " + this.loginContextName + " does not have keyTab value of " + this.keytabFile + ".");
            }
         }
      }
   }

   static {
      String jaasEnvVar = System.getenv("HBASE_JAAS_DEBUG");
      if ("true".equalsIgnoreCase(jaasEnvVar)) {
         JAAS_BASIC_OPTIONS.put("debug", "true");
      }

      KEYTAB_KERBEROS_OPTIONS = new HashMap();
      if (IS_IBM_JDK) {
         KEYTAB_KERBEROS_OPTIONS.put("credsType", "both");
      } else {
         KEYTAB_KERBEROS_OPTIONS.put("useKeyTab", "true");
         KEYTAB_KERBEROS_OPTIONS.put("useTicketCache", "false");
         KEYTAB_KERBEROS_OPTIONS.put("doNotPrompt", "true");
         KEYTAB_KERBEROS_OPTIONS.put("storeKey", "true");
      }

      KEYTAB_KERBEROS_OPTIONS.putAll(JAAS_BASIC_OPTIONS);
      KEYTAB_KERBEROS_LOGIN = new AppConfigurationEntry(KerberosUtil.getKrb5LoginModuleName(), LoginModuleControlFlag.REQUIRED, KEYTAB_KERBEROS_OPTIONS);
      KEYTAB_KERBEROS_CONF = new AppConfigurationEntry[]{KEYTAB_KERBEROS_LOGIN};
   }
}
