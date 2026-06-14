package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class FileOutProperties {
   private String path;
   private Map<String, Object> parm = new HashMap();

   public String getPath() {
      return this.path;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof FileOutProperties)) {
         return false;
      } else {
         FileOutProperties other = (FileOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$path = this.getPath();
            Object other$path = other.getPath();
            if (this$path == null) {
               if (other$path != null) {
                  return false;
               }
            } else if (!this$path.equals(other$path)) {
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
      return other instanceof FileOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $path = this.getPath();
      result = result * 59 + ($path == null ? 43 : $path.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "FileOutProperties(path=" + this.getPath() + ", parm=" + this.getParm() + ")";
   }
}
