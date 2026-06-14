package com.chang.util.mybatis.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

public abstract class BaseEntity<T extends BaseEntity<?>> extends Model<T> {
   @TableField(
      fill = FieldFill.INSERT
   )
   private Long createdAt;
   @TableField(
      fill = FieldFill.INSERT_UPDATE
   )
   private Long updatedAt;

   public Long getCreatedAt() {
      return this.createdAt;
   }

   public Long getUpdatedAt() {
      return this.updatedAt;
   }

   public BaseEntity<T> setCreatedAt(final Long createdAt) {
      this.createdAt = createdAt;
      return this;
   }

   public BaseEntity<T> setUpdatedAt(final Long updatedAt) {
      this.updatedAt = updatedAt;
      return this;
   }

   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BaseEntity)) {
         return false;
      } else {
         BaseEntity<?> other = (BaseEntity)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$createdAt = this.getCreatedAt();
            Object other$createdAt = other.getCreatedAt();
            if (this$createdAt == null) {
               if (other$createdAt != null) {
                  return false;
               }
            } else if (!this$createdAt.equals(other$createdAt)) {
               return false;
            }

            Object this$updatedAt = this.getUpdatedAt();
            Object other$updatedAt = other.getUpdatedAt();
            if (this$updatedAt == null) {
               if (other$updatedAt != null) {
                  return false;
               }
            } else if (!this$updatedAt.equals(other$updatedAt)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return other instanceof BaseEntity;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $createdAt = this.getCreatedAt();
      result = result * 59 + ($createdAt == null ? 43 : $createdAt.hashCode());
      Object $updatedAt = this.getUpdatedAt();
      result = result * 59 + ($updatedAt == null ? 43 : $updatedAt.hashCode());
      return result;
   }

   public String toString() {
      return "BaseEntity(createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
   }
}
