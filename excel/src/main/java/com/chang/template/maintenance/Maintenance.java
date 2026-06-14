package com.chang.template.maintenance;

public class Maintenance {
   private String maintenanceProgram;
   private String MaintenanceRequirements;

   public void setMaintenanceProgram(final String maintenanceProgram) {
      this.maintenanceProgram = maintenanceProgram;
   }

   public void setMaintenanceRequirements(final String MaintenanceRequirements) {
      this.MaintenanceRequirements = MaintenanceRequirements;
   }

   public String getMaintenanceProgram() {
      return this.maintenanceProgram;
   }

   public String getMaintenanceRequirements() {
      return this.MaintenanceRequirements;
   }
}
