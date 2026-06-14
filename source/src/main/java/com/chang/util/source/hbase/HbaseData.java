package com.chang.util.source.hbase;

public class HbaseData {
   private String rowKey;
   private Object keyAndData;

   public String getRowKey() {
      return this.rowKey;
   }

   public Object getKeyAndData() {
      return this.keyAndData;
   }

   public void setRowKey(String rowKey) {
      this.rowKey = rowKey;
   }

   public void setKeyAndData(Object keyAndData) {
      this.keyAndData = keyAndData;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HbaseData)) {
         return false;
      } else {
         HbaseData other = (HbaseData)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$rowKey = this.getRowKey();
            Object other$rowKey = other.getRowKey();
            if (this$rowKey == null) {
               if (other$rowKey != null) {
                  return false;
               }
            } else if (!this$rowKey.equals(other$rowKey)) {
               return false;
            }

            Object this$keyAndData = this.getKeyAndData();
            Object other$keyAndData = other.getKeyAndData();
            if (this$keyAndData == null) {
               if (other$keyAndData != null) {
                  return false;
               }
            } else if (!this$keyAndData.equals(other$keyAndData)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HbaseData;
   }

   public int hashCode() {
      int result = 1;
      Object $rowKey = this.getRowKey();
      result = result * 59 + ($rowKey == null ? 43 : $rowKey.hashCode());
      Object $keyAndData = this.getKeyAndData();
      result = result * 59 + ($keyAndData == null ? 43 : $keyAndData.hashCode());
      return result;
   }

   public String toString() {
      return "HbaseData(rowKey=" + this.getRowKey() + ", keyAndData=" + this.getKeyAndData() + ")";
   }
}
