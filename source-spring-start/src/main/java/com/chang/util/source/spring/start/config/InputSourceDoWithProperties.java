package com.chang.util.source.spring.start.config;

public class InputSourceDoWithProperties {
   private String inputSourceName;
   private String beanName;

   public String getInputSourceName() {
      return this.inputSourceName;
   }

   public String getBeanName() {
      return this.beanName;
   }

   public void setInputSourceName(String inputSourceName) {
      this.inputSourceName = inputSourceName;
   }

   public void setBeanName(String beanName) {
      this.beanName = beanName;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof InputSourceDoWithProperties)) {
         return false;
      } else {
         InputSourceDoWithProperties other = (InputSourceDoWithProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$inputSourceName = this.getInputSourceName();
            Object other$inputSourceName = other.getInputSourceName();
            if (this$inputSourceName == null) {
               if (other$inputSourceName != null) {
                  return false;
               }
            } else if (!this$inputSourceName.equals(other$inputSourceName)) {
               return false;
            }

            Object this$beanName = this.getBeanName();
            Object other$beanName = other.getBeanName();
            if (this$beanName == null) {
               if (other$beanName != null) {
                  return false;
               }
            } else if (!this$beanName.equals(other$beanName)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof InputSourceDoWithProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $inputSourceName = this.getInputSourceName();
      result = result * 59 + ($inputSourceName == null ? 43 : $inputSourceName.hashCode());
      Object $beanName = this.getBeanName();
      result = result * 59 + ($beanName == null ? 43 : $beanName.hashCode());
      return result;
   }

   public String toString() {
      return "InputSourceDoWithProperties(inputSourceName=" + this.getInputSourceName() + ", beanName=" + this.getBeanName() + ")";
   }
}
