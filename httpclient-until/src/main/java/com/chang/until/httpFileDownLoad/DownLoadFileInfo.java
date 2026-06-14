package com.chang.until.httpFileDownLoad;

public class DownLoadFileInfo {
   private Long start;
   private Long end;
   private Long downloadBytes;

   public Long getStart() {
      return this.start;
   }

   public Long getEnd() {
      return this.end;
   }

   public Long getDownloadBytes() {
      return this.downloadBytes;
   }

   public void setStart(Long start) {
      this.start = start;
   }

   public void setEnd(Long end) {
      this.end = end;
   }

   public void setDownloadBytes(Long downloadBytes) {
      this.downloadBytes = downloadBytes;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DownLoadFileInfo)) {
         return false;
      } else {
         DownLoadFileInfo other = (DownLoadFileInfo)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$start = this.getStart();
               Object other$start = other.getStart();
               if (this$start == null) {
                  if (other$start == null) {
                     break label47;
                  }
               } else if (this$start.equals(other$start)) {
                  break label47;
               }

               return false;
            }

            Object this$end = this.getEnd();
            Object other$end = other.getEnd();
            if (this$end == null) {
               if (other$end != null) {
                  return false;
               }
            } else if (!this$end.equals(other$end)) {
               return false;
            }

            Object this$downloadBytes = this.getDownloadBytes();
            Object other$downloadBytes = other.getDownloadBytes();
            if (this$downloadBytes == null) {
               if (other$downloadBytes != null) {
                  return false;
               }
            } else if (!this$downloadBytes.equals(other$downloadBytes)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof DownLoadFileInfo;
   }

   public int hashCode() {
      int result = 1;
      Object $start = this.getStart();
      result = result * 59 + ($start == null ? 43 : $start.hashCode());
      Object $end = this.getEnd();
      result = result * 59 + ($end == null ? 43 : $end.hashCode());
      Object $downloadBytes = this.getDownloadBytes();
      result = result * 59 + ($downloadBytes == null ? 43 : $downloadBytes.hashCode());
      return result;
   }

   public String toString() {
      return "DownLoadFileInfo(start=" + this.getStart() + ", end=" + this.getEnd() + ", downloadBytes=" + this.getDownloadBytes() + ")";
   }
}
