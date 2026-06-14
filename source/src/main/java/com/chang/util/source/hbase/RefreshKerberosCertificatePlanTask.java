/*
package com.chang.util.source.hbase;

import com.chang.hbaselib.HbaseUtil;
import java.io.IOException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshKerberosCertificatePlanTask implements Job {
   private static final Logger log = LoggerFactory.getLogger(RefreshKerberosCertificatePlanTask.class);

   public void execute(JobExecutionContext context) throws JobExecutionException {
      HbaseUtil send = (HbaseUtil)context.getJobDetail().getJobDataMap().get("hbase");

      try {
         log.info("RefreshKerberosCertificate... ...");
         send.RefreshKerberosCertificate();
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }
}
*/
