package com.chang.until.QuartzTimeTask;

import cn.hutool.core.util.ObjectUtil;
import com.chang.common.CommUtils;
import com.chang.common.cron.CronUtil;
import com.chang.common.exception.CustomException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

public class QuartzUtil {
   public static void quartzAnyBase(String keyName, Class<? extends Job> jobClass, String cronExpression, JobDataMap jobDataMap) throws Exception {
      QuartzConfig quartzConfig = new QuartzConfig();
      quartzConfig.setJobClass(jobClass);
      quartzConfig.setCronExpression(cronExpression);
      if (ObjectUtil.isNotNull(jobDataMap) && !jobDataMap.isEmpty()) {
         quartzConfig.setJobDataMap(jobDataMap);
      }

      QuartzTaskManage.addStartTask(keyName, quartzConfig);
   }

   public static void setStartTimeToRun(String keyName, Class<? extends Job> jobClass, String time, JobDataMap jobDataMap) throws Exception {
      if (CommUtils.getTimeStamp(time) < System.currentTimeMillis()) {
         throw new CustomException("设置的计划时间小于当前时间");
      } else {
         quartzAnyBase(keyName, jobClass, CronUtil.dateToCron(time), jobDataMap);
      }
   }

   public static void setEveryDayToRun(String keyName, Class<? extends Job> jobClass, String time, JobDataMap jobDataMap) throws Exception {
      quartzAnyBase(keyName, jobClass, CronUtil.everyDayToCron(time), jobDataMap);
   }

   public static void setEveryMonthToRun(String keyName, Class<? extends Job> jobClass, String time, JobDataMap jobDataMap) throws Exception {
      quartzAnyBase(keyName, jobClass, CronUtil.everyMonthToCron(time), jobDataMap);
   }

   public static void setEverySecondToRun(String keyName, Class<? extends Job> jobClass, Integer time, JobDataMap jobDataMap) throws Exception {
      quartzAnyBase(keyName, jobClass, CronUtil.everySecond(time), jobDataMap);
   }

   public static void setEveryMinuteToRun(String keyName, Class<? extends Job> jobClass, Integer time, JobDataMap jobDataMap) throws Exception {
      quartzAnyBase(keyName, jobClass, CronUtil.everyMinuteToCron(time), jobDataMap);
   }

   public static void setEveryHourToRun(String keyName, Class<? extends Job> jobClass, Integer time, JobDataMap jobDataMap) throws Exception {
      quartzAnyBase(keyName, jobClass, CronUtil.everyHour(time), jobDataMap);
   }

   public static void close(String keyName) throws SchedulerException {
      if (QuartzTaskManage.has(keyName)) {
         QuartzTaskManage.stopTask(keyName);
      }

   }
}
