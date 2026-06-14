/*
package com.chang.util.source.hbase;

import cn.hutool.core.util.ObjectUtil;
import com.chang.common.CommUtils;
import com.chang.hbaselib.HbaseUtil;
import com.chang.until.QuartzTimeTask.QuartzConfig;
import com.chang.until.QuartzTimeTask.QuartzTaskManage;
import com.chang.util.source.OutputSource;
import java.util.Map;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(HbaseOutput.class);
   private final HbaseUtil hbase;
   private final String tableName;
   private final String familyName;
   private final String keyName;
   private Map<String, Object> parm;

   public HbaseOutput(String master, String zookeeper, String tableName, String familyName, Boolean isAuth, String user, String krbConfPath, String keytabPath, String keyName, Map<String, Object> parm) {
      this.tableName = tableName;
      this.familyName = familyName;
      this.keyName = keyName;
      if (isAuth) {
         this.hbase = new HbaseUtil(master, zookeeper, user, krbConfPath, keytabPath);
         String taskId = CommUtils.getSnowflakeIdStr(2L);
         JobDataMap jobDataMap = new JobDataMap();
         jobDataMap.put("hbase", this.hbase);
         QuartzConfig quartzConfig = new QuartzConfig();
         quartzConfig.setJobClass(RefreshKerberosCertificatePlanTask.class);
         quartzConfig.setCronExpression("0 0 0/8 * * ?");
         quartzConfig.setJobDataMap(jobDataMap);

         try {
            QuartzTaskManage.addStartTask(taskId, quartzConfig);
         } catch (Exception var16) {
            throw new RuntimeException(var16);
         }
      } else {
         this.hbase = new HbaseUtil(master, zookeeper);
      }

      try {
         this.hbase.creatTable(tableName, new String[]{familyName});
      } catch (Exception var15) {
         log.error("HbaseOutput creatTable Err {}", keyName, var15);
         throw new RuntimeException(var15);
      }
   }

   public void Output(Object o) {
      if (o instanceof HbaseData) {
         HbaseData hbaseData = (HbaseData)o;

         try {
            this.hbase.addByteData(hbaseData.getRowKey(), this.tableName, this.familyName, hbaseData.getKeyAndData());
         } catch (Exception var4) {
            log.error("Output Err {}", this.keyName, var4);
            throw new RuntimeException(var4);
         }
      } else {
         throw new RuntimeException("save data must HbaseData class");
      }
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      if (ObjectUtil.isNotNull(this.hbase) && !this.hbase.isClose()) {
         try {
            this.hbase.CloseHbaseClient();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
*/
