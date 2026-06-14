package com.chang.util.source.spring.start.config;

import com.chang.util.source.common.TaskModeType;

import java.util.HashMap;
import java.util.Map;

public class CsvInProperties {
   private String keyName;
   private String fileFullPath;
   private String clazzName;
   private String time;
   private TaskModeType taskModeType;
   private Map<String, Object> parm = new HashMap();

   public String getKeyName() {
      return this.keyName;
   }

   public String getFileFullPath() {
      return this.fileFullPath;
   }

   public String getClazzName() {
      return this.clazzName;
   }

   public String getTime() {
      return this.time;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public void setFileFullPath(String fileFullPath) {
      this.fileFullPath = fileFullPath;
   }

   public void setClazzName(String clazzName) {
      this.clazzName = clazzName;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public void setTaskModeType(TaskModeType taskModeType) {
      this.taskModeType = taskModeType;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CsvInProperties)) {
         return false;
      } else {
         CsvInProperties other = (CsvInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$keyName = this.getKeyName();
            Object other$keyName = other.getKeyName();
            if (this$keyName == null) {
               if (other$keyName != null) {
                  return false;
               }
            } else if (!this$keyName.equals(other$keyName)) {
               return false;
            }

            Object this$fileFullPath = this.getFileFullPath();
            Object other$fileFullPath = other.getFileFullPath();
            if (this$fileFullPath == null) {
               if (other$fileFullPath != null) {
                  return false;
               }
            } else if (!this$fileFullPath.equals(other$fileFullPath)) {
               return false;
            }

            Object this$clazzName = this.getClazzName();
            Object other$clazzName = other.getClazzName();
            if (this$clazzName == null) {
               if (other$clazzName != null) {
                  return false;
               }
            } else if (!this$clazzName.equals(other$clazzName)) {
               return false;
            }

            label62: {
               Object this$time = this.getTime();
               Object other$time = other.getTime();
               if (this$time == null) {
                  if (other$time == null) {
                     break label62;
                  }
               } else if (this$time.equals(other$time)) {
                  break label62;
               }

               return false;
            }

            label55: {
               Object this$taskModeType = this.getTaskModeType();
               Object other$taskModeType = other.getTaskModeType();
               if (this$taskModeType == null) {
                  if (other$taskModeType == null) {
                     break label55;
                  }
               } else if (this$taskModeType.equals(other$taskModeType)) {
                  break label55;
               }

               return false;
            }

            Object this$parm = this.getParm();
            Object other$parm = other.getParm();
            if (this$parm == null) {
               if (other$parm != null) {
                  return false;
               }
            } else if (!this$parm.equals(other$parm)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof CsvInProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $keyName = this.getKeyName();
      result = result * 59 + ($keyName == null ? 43 : $keyName.hashCode());
      Object $fileFullPath = this.getFileFullPath();
      result = result * 59 + ($fileFullPath == null ? 43 : $fileFullPath.hashCode());
      Object $clazzName = this.getClazzName();
      result = result * 59 + ($clazzName == null ? 43 : $clazzName.hashCode());
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      Object $taskModeType = this.getTaskModeType();
      result = result * 59 + ($taskModeType == null ? 43 : $taskModeType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "CsvInProperties(keyName=" + this.getKeyName() + ", fileFullPath=" + this.getFileFullPath() + ", clazzName=" + this.getClazzName() + ", time=" + this.getTime() + ", taskModeType=" + this.getTaskModeType() + ", parm=" + this.getParm() + ")";
   }
}
