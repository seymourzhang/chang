package com.chang.util.source.tcpServer;

public class SendMode {
   private String groupName;
   private String mode = "";

   public String getGroupName() {
      return this.groupName;
   }

   public String getMode() {
      return this.mode;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public void setMode(String mode) {
      this.mode = mode;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SendMode)) {
         return false;
      } else {
         SendMode other = (SendMode)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$groupName = this.getGroupName();
            Object other$groupName = other.getGroupName();
            if (this$groupName == null) {
               if (other$groupName != null) {
                  return false;
               }
            } else if (!this$groupName.equals(other$groupName)) {
               return false;
            }

            Object this$mode = this.getMode();
            Object other$mode = other.getMode();
            if (this$mode == null) {
               if (other$mode != null) {
                  return false;
               }
            } else if (!this$mode.equals(other$mode)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SendMode;
   }

   public int hashCode() {
      int result = 1;
      Object $groupName = this.getGroupName();
      result = result * 59 + ($groupName == null ? 43 : $groupName.hashCode());
      Object $mode = this.getMode();
      result = result * 59 + ($mode == null ? 43 : $mode.hashCode());
      return result;
   }

   public String toString() {
      return "SendMode(groupName=" + this.getGroupName() + ", mode=" + this.getMode() + ")";
   }
}
