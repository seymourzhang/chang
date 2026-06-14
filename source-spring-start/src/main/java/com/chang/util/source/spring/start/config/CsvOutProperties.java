package com.chang.util.source.spring.start.config;

import java.util.HashMap;
import java.util.Map;

public class CsvOutProperties {
   private String keyName;
   private String filePath;
   private String fileName;
   private Long fileMaxSize;
   private Map<String, Object> parm = new HashMap();

   public String getKeyName() {
      return this.keyName;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public String getFileName() {
      return this.fileName;
   }

   public Long getFileMaxSize() {
      return this.fileMaxSize;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public void setFileMaxSize(Long fileMaxSize) {
      this.fileMaxSize = fileMaxSize;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CsvOutProperties)) {
         return false;
      } else {
         CsvOutProperties other = (CsvOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label71: {
               Object this$fileMaxSize = this.getFileMaxSize();
               Object other$fileMaxSize = other.getFileMaxSize();
               if (this$fileMaxSize == null) {
                  if (other$fileMaxSize == null) {
                     break label71;
                  }
               } else if (this$fileMaxSize.equals(other$fileMaxSize)) {
                  break label71;
               }

               return false;
            }

            Object this$keyName = this.getKeyName();
            Object other$keyName = other.getKeyName();
            if (this$keyName == null) {
               if (other$keyName != null) {
                  return false;
               }
            } else if (!this$keyName.equals(other$keyName)) {
               return false;
            }

            label57: {
               Object this$filePath = this.getFilePath();
               Object other$filePath = other.getFilePath();
               if (this$filePath == null) {
                  if (other$filePath == null) {
                     break label57;
                  }
               } else if (this$filePath.equals(other$filePath)) {
                  break label57;
               }

               return false;
            }

            Object this$fileName = this.getFileName();
            Object other$fileName = other.getFileName();
            if (this$fileName == null) {
               if (other$fileName != null) {
                  return false;
               }
            } else if (!this$fileName.equals(other$fileName)) {
               return false;
            }

            Object this$parm = this.getParm();
            Object other$parm = other.getParm();
            if (this$parm == null) {
               if (other$parm == null) {
                  return true;
               }
            } else if (this$parm.equals(other$parm)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof CsvOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $fileMaxSize = this.getFileMaxSize();
      result = result * 59 + ($fileMaxSize == null ? 43 : $fileMaxSize.hashCode());
      Object $keyName = this.getKeyName();
      result = result * 59 + ($keyName == null ? 43 : $keyName.hashCode());
      Object $filePath = this.getFilePath();
      result = result * 59 + ($filePath == null ? 43 : $filePath.hashCode());
      Object $fileName = this.getFileName();
      result = result * 59 + ($fileName == null ? 43 : $fileName.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "CsvOutProperties(keyName=" + this.getKeyName() + ", filePath=" + this.getFilePath() + ", fileName=" + this.getFileName() + ", fileMaxSize=" + this.getFileMaxSize() + ", parm=" + this.getParm() + ")";
   }
}
