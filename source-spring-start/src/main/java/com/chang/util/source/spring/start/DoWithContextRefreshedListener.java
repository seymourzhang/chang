package com.chang.util.source.spring.start;

import cn.hutool.core.util.ObjectUtil;
import com.chang.until.license.LicenseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class DoWithContextRefreshedListener implements SmartLifecycle {
   private static final Logger log = LoggerFactory.getLogger(DoWithContextRefreshedListener.class);
   private static final AtomicBoolean firstRun = new AtomicBoolean(false);
   private final InitSource initSource;
   private final LicenseClient licenseClient;

   public DoWithContextRefreshedListener(InitSource initSource, LicenseClient licenseClient) {
      this.initSource = initSource;
      this.licenseClient = licenseClient;
   }

   public void start() {
      try {
         if (ObjectUtil.equal(firstRun.get(), false)) {
            try {
               this.licenseClient.checkHealth();
            } catch (Exception var2) {
               System.exit(0);
            }

            this.initSource.init();
            firstRun.set(true);
         }

      } catch (Exception var3) {
         log.error("DoWithContext Err", var3);
         throw new RuntimeException(var3);
      }
   }

   public void stop() {
   }

   public boolean isRunning() {
      return false;
   }
}
