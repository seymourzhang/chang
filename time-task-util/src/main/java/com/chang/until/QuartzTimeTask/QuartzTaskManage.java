package com.chang.until.QuartzTimeTask;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTaskManage {
   private static final ConcurrentHashMap<String, QuartzConfig> scheduleConfigMap = new ConcurrentHashMap();
   private static final Scheduler scheduler;

   private static JobDetail getJobDetail(String name, Class<? extends Job> jobClass) {
      JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(JobKey.jobKey(name)).build();
      return jobDetail;
   }

   private static JobDetail getJobDetail(String name, Class<? extends Job> jobClass, JobDataMap jobDataMap) {
      JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(JobKey.jobKey(name)).usingJobData(jobDataMap).build();
      return jobDetail;
   }

   private static CronTrigger getCronTrigger(String name, String cronExpression) {
      CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey(name)).startNow().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
      return trigger;
   }

   private static Scheduler getScheduler(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
      scheduler.scheduleJob(jobDetail, trigger);
      return scheduler;
   }

   public static void addStartTask(String name, QuartzConfig quartzConfig) throws SchedulerException {
      buildScheduler(name, quartzConfig);
      scheduleConfigMap.put(name, quartzConfig);
   }

   public static void addTaskAndStop(String name, QuartzConfig quartzConfig) throws SchedulerException {
      buildScheduler(name, quartzConfig);
      scheduler.pauseJob(JobKey.jobKey(name));
      scheduleConfigMap.put(name, quartzConfig);
   }

   private static void buildScheduler(String name, QuartzConfig quartzConfig) throws SchedulerException {
      if (scheduleConfigMap.containsKey(name)) {
         throw new RuntimeException("任务已经存在");
      } else {
         JobDetail jobDetail = null;
         if (ObjectUtil.isNotNull(quartzConfig.getJobDataMap())) {
            jobDetail = getJobDetail(name, quartzConfig.getJobClass(), quartzConfig.getJobDataMap());
         } else {
            jobDetail = getJobDetail(name, quartzConfig.getJobClass());
         }

         CronTrigger cronTrigger = getCronTrigger(name, quartzConfig.getCronExpression());
         scheduler.scheduleJob(jobDetail, cronTrigger);
      }
   }

   public static void stopTask(String name) throws SchedulerException {
      if (!scheduleConfigMap.containsKey(name)) {
         throw new RuntimeException("任务已经不存在");
      } else {
         scheduler.pauseJob(JobKey.jobKey(name));
      }
   }

   public static void removeTask(String name) throws SchedulerException {
      if (!scheduleConfigMap.containsKey(name)) {
         throw new RuntimeException("任务已经不存在");
      } else {
         scheduler.deleteJob(JobKey.jobKey(name));
         scheduleConfigMap.remove(name);
      }
   }

   public static void removeAllTask() throws SchedulerException {
      Iterator var0 = scheduleConfigMap.keySet().iterator();

      while(var0.hasNext()) {
         String name = (String)var0.next();
         scheduler.deleteJob(JobKey.jobKey(name));
      }

      scheduleConfigMap.clear();
   }

   public static void reStartTask(String name) throws SchedulerException {
      if (!scheduleConfigMap.containsKey(name)) {
         throw new RuntimeException("任务已经不存在");
      } else {
         scheduler.resumeJob(JobKey.jobKey(name));
      }
   }

   public static void modTaskCron(String name, String cron) throws SchedulerException {
      QuartzConfig quartzConfig = (QuartzConfig)scheduleConfigMap.get(name);
      scheduler.deleteJob(JobKey.jobKey(name));
      quartzConfig.setCronExpression(cron);
      scheduleConfigMap.remove(name);
      buildScheduler(name, quartzConfig);
   }

   public static void modTaskParm(String name, JobDataMap parm) throws SchedulerException {
      QuartzConfig quartzConfig = (QuartzConfig)scheduleConfigMap.get(name);
      scheduler.deleteJob(JobKey.jobKey(name));
      quartzConfig.setJobDataMap(parm);
      scheduleConfigMap.remove(name);
      buildScheduler(name, quartzConfig);
   }

   public static JSONObject getAllConfig() {
      JSONObject jsonObject = new JSONObject();
      jsonObject.putAll(scheduleConfigMap);
      return jsonObject;
   }

   public static boolean isRun(String name) throws SchedulerException {
      List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
      Iterator var2 = currentlyExecutingJobs.iterator();

      String jobName;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         JobExecutionContext executionContext = (JobExecutionContext)var2.next();
         JobDetail jobDetail = executionContext.getJobDetail();
         jobName = jobDetail.getKey().getName();
      } while(!jobName.equals(name));

      return true;
   }

   public static boolean has(String name) {
      return scheduleConfigMap.containsKey(name);
   }

   public static void pauseAll() throws SchedulerException {
      scheduler.pauseAll();
   }

   public static void resumeAll() throws SchedulerException {
      scheduler.resumeAll();
   }

   static {
      try {
         scheduler = (new StdSchedulerFactory()).getScheduler();
         scheduler.start();
      } catch (SchedulerException var1) {
         throw new RuntimeException(var1);
      }
   }
}
