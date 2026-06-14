package com.chang.util.source.spring.start.config;

import com.chang.util.source.common.TaskModeType;

import java.util.HashMap;
import java.util.Map;

public class FileInProperties {
   private String path;
   private String time;
   private TaskModeType taskModeType;
   private Map<String, Object> parm = new HashMap();

   public String getPath() {
      return this.path;
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

   public void setPath(String path) {
      this.path = path;
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
      } else if (!(o instanceof FileInProperties)) {
         return false;
      } else {
         FileInProperties other = (FileInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$path = this.getPath();
               Object other$path = other.getPath();
               if (this$path == null) {
                  if (other$path == null) {
                     break label59;
                  }
               } else if (this$path.equals(other$path)) {
                  break label59;
               }

               return false;
            }

            Object this$time = this.getTime();
            Object other$time = other.getTime();
            if (this$time == null) {
               if (other$time != null) {
                  return false;
               }
            } else if (!this$time.equals(other$time)) {
               return false;
            }

            Object this$taskModeType = this.getTaskModeType();
            Object other$taskModeType = other.getTaskModeType();
            if (this$taskModeType == null) {
               if (other$taskModeType != null) {
                  return false;
               }
            } else if (!this$taskModeType.equals(other$taskModeType)) {
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
      return other instanceof FileInProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $path = this.getPath();
      result = result * 59 + ($path == null ? 43 : $path.hashCode());
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      Object $taskModeType = this.getTaskModeType();
      result = result * 59 + ($taskModeType == null ? 43 : $taskModeType.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "FileInProperties(path=" + this.getPath() + ", time=" + this.getTime() + ", taskModeType=" + this.getTaskModeType() + ", parm=" + this.getParm() + ")";
   }
}
