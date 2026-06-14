package com.chang.common.template.maintenance;

import java.util.List;
import java.util.Map;

public class MaintenanceTemplate {
   private String maintenanceTemplateName;
   private String elevatorType;
   private String maintenanceType;
   private String creator;
   private Map<String, List<Maintenance>> maintenanceList;

   public String getMaintenanceTemplateName() {
      return this.maintenanceTemplateName;
   }

   public String getElevatorType() {
      return this.elevatorType;
   }

   public String getMaintenanceType() {
      return this.maintenanceType;
   }

   public String getCreator() {
      return this.creator;
   }

   public Map<String, List<Maintenance>> getMaintenanceList() {
      return this.maintenanceList;
   }

   public void setMaintenanceTemplateName(final String maintenanceTemplateName) {
      this.maintenanceTemplateName = maintenanceTemplateName;
   }

   public void setElevatorType(final String elevatorType) {
      this.elevatorType = elevatorType;
   }

   public void setMaintenanceType(final String maintenanceType) {
      this.maintenanceType = maintenanceType;
   }

   public void setCreator(final String creator) {
      this.creator = creator;
   }

   public void setMaintenanceList(final Map<String, List<Maintenance>> maintenanceList) {
      this.maintenanceList = maintenanceList;
   }
}
