package com.chang.until.license.InitializationWork;

import com.chang.until.QuartzTimeTask.QuartzUtil;
import com.chang.until.license.license.LicenseJob;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CheckStartupRunner implements CommandLineRunner {
   private static final Logger log = LoggerFactory.getLogger(CheckStartupRunner.class);

   public void run(String... strings) {
      try {
         QuartzUtil.setEveryMinuteToRun("licenseJob", LicenseJob.class, 1, (JobDataMap)null);
      } catch (Exception var3) {
         System.exit(0);
      }

   }
}
