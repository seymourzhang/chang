package com.chang.until.QuartzTimeTask;

import org.quartz.Job;
import org.quartz.JobDataMap;

public class QuartzConfig {
   private Class<? extends Job> jobClass;
   private String cronExpression;
   private JobDataMap jobDataMap;

   public Class<? extends Job> getJobClass() {
      return this.jobClass;
   }

   public String getCronExpression() {
      return this.cronExpression;
   }

   public JobDataMap getJobDataMap() {
      return this.jobDataMap;
   }

   public void setJobClass(Class<? extends Job> jobClass) {
      this.jobClass = jobClass;
   }

   public void setCronExpression(String cronExpression) {
      this.cronExpression = cronExpression;
   }

   public void setJobDataMap(JobDataMap jobDataMap) {
      this.jobDataMap = jobDataMap;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof QuartzConfig)) {
         return false;
      } else {
         QuartzConfig other = (QuartzConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$jobClass = this.getJobClass();
               Object other$jobClass = other.getJobClass();
               if (this$jobClass == null) {
                  if (other$jobClass == null) {
                     break label47;
                  }
               } else if (this$jobClass.equals(other$jobClass)) {
                  break label47;
               }

               return false;
            }

            Object this$cronExpression = this.getCronExpression();
            Object other$cronExpression = other.getCronExpression();
            if (this$cronExpression == null) {
               if (other$cronExpression != null) {
                  return false;
               }
            } else if (!this$cronExpression.equals(other$cronExpression)) {
               return false;
            }

            Object this$jobDataMap = this.getJobDataMap();
            Object other$jobDataMap = other.getJobDataMap();
            if (this$jobDataMap == null) {
               if (other$jobDataMap != null) {
                  return false;
               }
            } else if (!this$jobDataMap.equals(other$jobDataMap)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof QuartzConfig;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $jobClass = this.getJobClass();
      result = result * 59 + ($jobClass == null ? 43 : $jobClass.hashCode());
      Object $cronExpression = this.getCronExpression();
      result = result * 59 + ($cronExpression == null ? 43 : $cronExpression.hashCode());
      Object $jobDataMap = this.getJobDataMap();
      result = result * 59 + ($jobDataMap == null ? 43 : $jobDataMap.hashCode());
      return result;
   }

   public String toString() {
      return "QuartzConfig(jobClass=" + this.getJobClass() + ", cronExpression=" + this.getCronExpression() + ", jobDataMap=" + this.getJobDataMap() + ")";
   }
}
