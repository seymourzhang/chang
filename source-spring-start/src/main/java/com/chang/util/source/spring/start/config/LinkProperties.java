package com.chang.util.source.spring.start.config;

import java.util.Map;

public class LinkProperties {
   private String from;
   private String to;
   private String functionBeanClass;
   private Map<String, Object> parm;

   public String getFrom() {
      return this.from;
   }

   public String getTo() {
      return this.to;
   }

   public String getFunctionBeanClass() {
      return this.functionBeanClass;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public void setTo(String to) {
      this.to = to;
   }

   public void setFunctionBeanClass(String functionBeanClass) {
      this.functionBeanClass = functionBeanClass;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LinkProperties)) {
         return false;
      } else {
         LinkProperties other = (LinkProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label59: {
               Object this$from = this.getFrom();
               Object other$from = other.getFrom();
               if (this$from == null) {
                  if (other$from == null) {
                     break label59;
                  }
               } else if (this$from.equals(other$from)) {
                  break label59;
               }

               return false;
            }

            Object this$to = this.getTo();
            Object other$to = other.getTo();
            if (this$to == null) {
               if (other$to != null) {
                  return false;
               }
            } else if (!this$to.equals(other$to)) {
               return false;
            }

            Object this$functionBeanClass = this.getFunctionBeanClass();
            Object other$functionBeanClass = other.getFunctionBeanClass();
            if (this$functionBeanClass == null) {
               if (other$functionBeanClass != null) {
                  return false;
               }
            } else if (!this$functionBeanClass.equals(other$functionBeanClass)) {
               return false;
            }

            Object this$parm = this.getParm();
            Object other$parm = other.getParm();
            if (this$parm == null) {
               if (other$parm != null) {
                  return false;
               }
            } else if (!this$parm.equals(other$parm)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof LinkProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $from = this.getFrom();
      result = result * 59 + ($from == null ? 43 : $from.hashCode());
      Object $to = this.getTo();
      result = result * 59 + ($to == null ? 43 : $to.hashCode());
      Object $functionBeanClass = this.getFunctionBeanClass();
      result = result * 59 + ($functionBeanClass == null ? 43 : $functionBeanClass.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "LinkProperties(from=" + this.getFrom() + ", to=" + this.getTo() + ", functionBeanClass=" + this.getFunctionBeanClass() + ", parm=" + this.getParm() + ")";
   }
}
