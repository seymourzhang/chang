package com.chang.netty.until.common;

public class HeartBeat {
   private String messageType = "HEARTBEAT";
   private String name;
   private String addr;
   private String type;
   private String groupName;

   public String getMessageType() {
      return this.messageType;
   }

   public String getName() {
      return this.name;
   }

   public String getAddr() {
      return this.addr;
   }

   public String getType() {
      return this.type;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setAddr(String addr) {
      this.addr = addr;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HeartBeat)) {
         return false;
      } else {
         HeartBeat other = (HeartBeat)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label71: {
               Object this$messageType = this.getMessageType();
               Object other$messageType = other.getMessageType();
               if (this$messageType == null) {
                  if (other$messageType == null) {
                     break label71;
                  }
               } else if (this$messageType.equals(other$messageType)) {
                  break label71;
               }

               return false;
            }

            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            label57: {
               Object this$addr = this.getAddr();
               Object other$addr = other.getAddr();
               if (this$addr == null) {
                  if (other$addr == null) {
                     break label57;
                  }
               } else if (this$addr.equals(other$addr)) {
                  break label57;
               }

               return false;
            }

            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type != null) {
                  return false;
               }
            } else if (!this$type.equals(other$type)) {
               return false;
            }

            Object this$groupName = this.getGroupName();
            Object other$groupName = other.getGroupName();
            if (this$groupName == null) {
               if (other$groupName == null) {
                  return true;
               }
            } else if (this$groupName.equals(other$groupName)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HeartBeat;
   }

   public int hashCode() {
      int result = 1;
      Object $messageType = this.getMessageType();
      result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $addr = this.getAddr();
      result = result * 59 + ($addr == null ? 43 : $addr.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $groupName = this.getGroupName();
      result = result * 59 + ($groupName == null ? 43 : $groupName.hashCode());
      return result;
   }

   public String toString() {
      return "HeartBeat(messageType=" + this.getMessageType() + ", name=" + this.getName() + ", addr=" + this.getAddr() + ", type=" + this.getType() + ", groupName=" + this.getGroupName() + ")";
   }
}
