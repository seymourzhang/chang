package com.chang.util.mybatis.base;

import com.chang.until.license.LicenseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class CheckPackage implements ApplicationListener<ContextRefreshedEvent> {
   private static final Logger log = LoggerFactory.getLogger(CheckPackage.class);
   @Autowired
   private LicenseClient licenseClient;

   public void onApplicationEvent(ContextRefreshedEvent event) {
      try {
         this.licenseClient.checkHealth();
      } catch (Exception var3) {
         log.error("chang import error,please check");
         System.exit(0);
      }

   }
}
