package com.chang.hbaselib;

import com.alibaba.fastjson.JSONObject;

public class HbaseDataVo {
   private String rowKey;
   private JSONObject object;

   public String getRowKey() {
      return this.rowKey;
   }

   public JSONObject getObject() {
      return this.object;
   }

   public void setRowKey(String rowKey) {
      this.rowKey = rowKey;
   }

   public void setObject(JSONObject object) {
      this.object = object;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HbaseDataVo)) {
         return false;
      } else {
         HbaseDataVo other = (HbaseDataVo)o;
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

            Object this$object = this.getObject();
            Object other$object = other.getObject();
            if (this$object == null) {
               if (other$object != null) {
                  return false;
               }
            } else if (!this$object.equals(other$object)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HbaseDataVo;
   }

   public int hashCode() {
      int result = 1;
      Object $rowKey = this.getRowKey();
      result = result * 59 + ($rowKey == null ? 43 : $rowKey.hashCode());
      Object $object = this.getObject();
      result = result * 59 + ($object == null ? 43 : $object.hashCode());
      return result;
   }

   public String toString() {
      return "HbaseDataVo(rowKey=" + this.getRowKey() + ", object=" + this.getObject() + ")";
   }
}
