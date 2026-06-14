package com.chang.template.repair;

import java.util.List;
import java.util.Map;

public class RepairTemplate {
   private String repairTemplateName;
   private String elevatorType;
   private String creator;
   Map<String, Map<String, List<String>>> repair;

   public String getRepairTemplateName() {
      return this.repairTemplateName;
   }

   public String getElevatorType() {
      return this.elevatorType;
   }

   public String getCreator() {
      return this.creator;
   }

   public Map<String, Map<String, List<String>>> getRepair() {
      return this.repair;
   }

   public void setRepairTemplateName(final String repairTemplateName) {
      this.repairTemplateName = repairTemplateName;
   }

   public void setElevatorType(final String elevatorType) {
      this.elevatorType = elevatorType;
   }

   public void setCreator(final String creator) {
      this.creator = creator;
   }

   public void setRepair(final Map<String, Map<String, List<String>>> repair) {
      this.repair = repair;
   }
}
