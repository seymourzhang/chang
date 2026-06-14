package com.chang.util.source.mongodb;

public class Condition {
   private String name;
   private String type;
   private Object value;

   public String getName() {
      return this.name;
   }

   public String getType() {
      return this.type;
   }

   public Object getValue() {
      return this.value;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Condition)) {
         return false;
      } else {
         Condition other = (Condition)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name == null) {
                     break label47;
                  }
               } else if (this$name.equals(other$name)) {
                  break label47;
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

            Object this$value = this.getValue();
            Object other$value = other.getValue();
            if (this$value == null) {
               if (other$value != null) {
                  return false;
               }
            } else if (!this$value.equals(other$value)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Condition;
   }

   public int hashCode() {
      int result = 1;
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $value = this.getValue();
      result = result * 59 + ($value == null ? 43 : $value.hashCode());
      return result;
   }

   public String toString() {
      return "Condition(name=" + this.getName() + ", type=" + this.getType() + ", value=" + this.getValue() + ")";
   }
}
