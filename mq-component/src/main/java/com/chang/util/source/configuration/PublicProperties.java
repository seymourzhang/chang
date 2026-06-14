package com.chang.util.source.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
   prefix = "wg.data.source.public-conf"
)
public class PublicProperties {
   private Boolean enableMqttOutPrint = true;

   public Boolean getEnableMqttOutPrint() {
      return this.enableMqttOutPrint;
   }

   public void setEnableMqttOutPrint(Boolean enableMqttOutPrint) {
      this.enableMqttOutPrint = enableMqttOutPrint;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PublicProperties)) {
         return false;
      } else {
         PublicProperties other = (PublicProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$enableMqttOutPrint = this.getEnableMqttOutPrint();
            Object other$enableMqttOutPrint = other.getEnableMqttOutPrint();
            if (this$enableMqttOutPrint == null) {
               if (other$enableMqttOutPrint != null) {
                  return false;
               }
            } else if (!this$enableMqttOutPrint.equals(other$enableMqttOutPrint)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PublicProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $enableMqttOutPrint = this.getEnableMqttOutPrint();
      result = result * 59 + ($enableMqttOutPrint == null ? 43 : $enableMqttOutPrint.hashCode());
      return result;
   }

   public String toString() {
      return "PublicProperties(enableMqttOutPrint=" + this.getEnableMqttOutPrint() + ")";
   }
}
