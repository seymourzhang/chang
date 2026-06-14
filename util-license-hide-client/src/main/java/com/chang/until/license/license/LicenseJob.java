package com.chang.until.license.license;

import cn.hutool.core.util.ObjectUtil;
import com.chang.until.license.LicenseClient;
import com.chang.until.spring.context.SpringContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenseJob implements Job {
   private static final Logger log = LoggerFactory.getLogger(LicenseJob.class);
   private LicenseClient licenseClient = null;

   public void execute(JobExecutionContext context) throws JobExecutionException {
      try {
         if (ObjectUtil.isNull(this.licenseClient)) {
            this.licenseClient = (LicenseClient)SpringContext.getBean(LicenseClient.class);
         }

         this.licenseClient.verify();
      } catch (Exception var3) {
         System.exit(0);
      }

   }
}
