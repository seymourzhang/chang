package com.chang.util.source.common;

public class TcpData {
   private String client;
   private String groupName;
   private Object data;

   public String getClient() {
      return this.client;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public Object getData() {
      return this.data;
   }

   public void setClient(String client) {
      this.client = client;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public void setData(Object data) {
      this.data = data;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TcpData)) {
         return false;
      } else {
         TcpData other = (TcpData)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$client = this.getClient();
               Object other$client = other.getClient();
               if (this$client == null) {
                  if (other$client == null) {
                     break label47;
                  }
               } else if (this$client.equals(other$client)) {
                  break label47;
               }

               return false;
            }

            Object this$groupName = this.getGroupName();
            Object other$groupName = other.getGroupName();
            if (this$groupName == null) {
               if (other$groupName != null) {
                  return false;
               }
            } else if (!this$groupName.equals(other$groupName)) {
               return false;
            }

            Object this$data = this.getData();
            Object other$data = other.getData();
            if (this$data == null) {
               if (other$data != null) {
                  return false;
               }
            } else if (!this$data.equals(other$data)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof TcpData;
   }

   public int hashCode() {
      int result = 1;
      Object $client = this.getClient();
      result = result * 59 + ($client == null ? 43 : $client.hashCode());
      Object $groupName = this.getGroupName();
      result = result * 59 + ($groupName == null ? 43 : $groupName.hashCode());
      Object $data = this.getData();
      result = result * 59 + ($data == null ? 43 : $data.hashCode());
      return result;
   }

   public String toString() {
      return "TcpData(client=" + this.getClient() + ", groupName=" + this.getGroupName() + ", data=" + this.getData() + ")";
   }
}
