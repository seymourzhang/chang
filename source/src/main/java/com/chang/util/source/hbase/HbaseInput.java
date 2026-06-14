/*
package com.chang.util.source.hbase;

import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.TaskModeType;
import com.chang.common.CommUtils;
import com.chang.hbaselib.HbaseUtil;
import com.chang.until.QuartzTimeTask.QuartzConfig;
import com.chang.until.QuartzTimeTask.QuartzTaskManage;
import com.chang.until.QuartzTimeTask.QuartzUtil;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseInput implements InputSource<Object, Object>, Job {
   private static final Logger log = LoggerFactory.getLogger(HbaseInput.class);
   private HbaseUtil hbase;
   private Scan scan;
   private String tableName;
   private String keyName;
   private String time;
   private volatile Boolean first = false;
   private String familyName;
   private Map<String, Object> parm;
   private TaskModeType taskModeType;
   private OutputSource source;
   private Function<Object, Object> function;
   private Map<String, Object> content = new HashMap();
   private Class<?> aClass;
   private Long count = 0L;

   public HbaseInput(String master, String zookeeper, String tableName, String familyName, Class<?> aClass, String startRow, String endRow, String keyName, String time, TaskModeType taskModeType, Boolean isAuth, String user, String krbConfPath, String keytabPath, Map<String, Object> parm) throws Exception {
      this.tableName = tableName;
      this.keyName = keyName;
      this.time = time;
      this.familyName = familyName;
      this.parm = parm;
      this.taskModeType = taskModeType;
      this.aClass = aClass;
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
         } catch (Exception var20) {
            throw new RuntimeException(var20);
         }
      } else {
         this.hbase = new HbaseUtil(master, zookeeper);
      }

      if (!StringUtils.isBlank(startRow) && !StringUtils.isBlank(endRow)) {
         this.scan = this.hbase.getRowRangScan(startRow, endRow);
         this.scan.addFamily(Bytes.toBytes(familyName));
         this.count = this.hbase.countTableAndFamilyNum(tableName, familyName, startRow, endRow);
      } else {
         throw new RuntimeException("startRow or endRow is null");
      }
   }

   public HbaseInput(String master, String zookeeper, String tableName, String familyName, Class<?> aClass, String rowKeyPrefix, String keyName, String time, TaskModeType taskModeType, Boolean isAuth, String user, String krbConfPath, String keytabPath, Map<String, Object> parm) throws Exception {
      this.tableName = tableName;
      this.keyName = keyName;
      this.time = time;
      this.familyName = familyName;
      this.parm = parm;
      this.taskModeType = taskModeType;
      this.aClass = aClass;
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
         } catch (Exception var19) {
            throw new RuntimeException(var19);
         }
      } else {
         this.hbase = new HbaseUtil(master, zookeeper);
      }

      if (StringUtils.isBlank(rowKeyPrefix)) {
         throw new RuntimeException("rowKeyPrefix is null");
      } else {
         this.scan = this.hbase.getNewScan();
         PrefixFilter prefixFilter = this.hbase.getPrefixFilter(rowKeyPrefix);
         this.scan.setFilter(prefixFilter);
         this.scan.addFamily(Bytes.toBytes(familyName));
         this.count = this.hbase.countTableAndFamilyNum(tableName, familyName, rowKeyPrefix);
      }
   }

   public void InPut(OutputSource source, Function<Object, Object> function) {
      try {
         this.source = source;
         this.function = function;
         if (!this.first) {
            if (!StringUtils.isBlank(this.time) && !ObjectUtil.isNull(this.taskModeType)) {
               JobDataMap jobDataMap = new JobDataMap();
               jobDataMap.put("this", this);
               if (ObjectUtil.equals(TaskModeType.ONCE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setStartTimeToRun(this.keyName, HbaseInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.LOOP_DAY.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryDayToRun(this.keyName, HbaseInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.LOOP_MONTH.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryMonthToRun(this.keyName, HbaseInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_HOUR.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryHourToRun(this.keyName, HbaseInput.class, Integer.parseInt(this.time), jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_MINUTE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryMinuteToRun(this.keyName, HbaseInput.class, Integer.parseInt(this.time), jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_SECOND.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEverySecondToRun(this.keyName, HbaseInput.class, Integer.parseInt(this.time), jobDataMap);
               }
            } else {
               try {
                  if (ObjectUtil.isNull(this.parm)) {
                     this.parm = new HashMap();
                  }

                  this.parm.put("hbase_input_total_key", this.count);
                  SourceContext.setExParm(this.parm);
                  ResultScanner scanner = this.hbase.getResult(this.tableName, this.scan);
                  this.hbase.getQueryStream(this.content, scanner, (contents, key, object) -> {
                     if (ObjectUtil.isNotNull(function)) {
                        source.Output(function.apply(object));
                     } else {
                        source.Output(object);
                     }

                  }, this.aClass);
                  SourceContext.clearExParm();
               } catch (Exception var4) {
                  log.error("InPut", var4);
               }
            }

            this.first = true;
         }

      } catch (Exception var5) {
         log.error("InPut Err", var5);
         throw new RuntimeException(var5);
      }
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void execute(JobExecutionContext context) throws JobExecutionException {
      HbaseInput hbaseInput = (HbaseInput)context.getJobDetail().getJobDataMap().get("this");
      this.content = hbaseInput.getContent();
      this.parm = hbaseInput.getParm();

      try {
         if (ObjectUtil.isNull(this.parm)) {
            this.parm = new HashMap();
         }

         this.parm.put("hbase_input_total_key", this.count);
         SourceContext.setExParm(this.parm);
         ResultScanner scanner = hbaseInput.getHbase().getResultByScanPrefixFilter(hbaseInput.getTableName(), hbaseInput.getFamilyName(), hbaseInput.getScan());
         hbaseInput.getHbase().getQueryStream(this.content, scanner, (contents, key, object) -> {
            if (ObjectUtil.isNotNull(this.function)) {
               this.source.Output(this.function.apply(object));
            } else {
               this.source.Output(object);
            }

         }, hbaseInput.getAClass());
         SourceContext.clearExParm();
      } catch (Exception var4) {
         log.error("InPut", var4);
      }

   }

   public void close() {
      try {
         QuartzUtil.close(this.keyName);
      } catch (Exception var3) {
         log.error("InPut", var3);
      }

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

   public HbaseUtil getHbase() {
      return this.hbase;
   }

   public Scan getScan() {
      return this.scan;
   }

   public String getTableName() {
      return this.tableName;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public String getTime() {
      return this.time;
   }

   public Boolean getFirst() {
      return this.first;
   }

   public String getFamilyName() {
      return this.familyName;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public OutputSource getSource() {
      return this.source;
   }

   public Function<Object, Object> getFunction() {
      return this.function;
   }

   public Map<String, Object> getContent() {
      return this.content;
   }

   public Class<?> getAClass() {
      return this.aClass;
   }

   public Long getCount() {
      return this.count;
   }

   public HbaseInput() {
   }
}
*/
