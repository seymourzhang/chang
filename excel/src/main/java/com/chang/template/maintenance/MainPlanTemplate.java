package com.chang.template.maintenance;

import java.util.List;

public class MainPlanTemplate {
   private String elevatorName;
   private String maintenanceUnit;
   private String userUnit;
   private List<String> halfMonthMaintenance;
   private List<String> quarterlyMaintenance;
   private List<String> halfYearMaintenance;
   private List<String> annualMaintenance;

   public String getElevatorName() {
      return this.elevatorName;
   }

   public String getMaintenanceUnit() {
      return this.maintenanceUnit;
   }

   public String getUserUnit() {
      return this.userUnit;
   }

   public List<String> getHalfMonthMaintenance() {
      return this.halfMonthMaintenance;
   }

   public List<String> getQuarterlyMaintenance() {
      return this.quarterlyMaintenance;
   }

   public List<String> getHalfYearMaintenance() {
      return this.halfYearMaintenance;
   }

   public List<String> getAnnualMaintenance() {
      return this.annualMaintenance;
   }

   public void setElevatorName(final String elevatorName) {
      this.elevatorName = elevatorName;
   }

   public void setMaintenanceUnit(final String maintenanceUnit) {
      this.maintenanceUnit = maintenanceUnit;
   }

   public void setUserUnit(final String userUnit) {
      this.userUnit = userUnit;
   }

   public void setHalfMonthMaintenance(final List<String> halfMonthMaintenance) {
      this.halfMonthMaintenance = halfMonthMaintenance;
   }

   public void setQuarterlyMaintenance(final List<String> quarterlyMaintenance) {
      this.quarterlyMaintenance = quarterlyMaintenance;
   }

   public void setHalfYearMaintenance(final List<String> halfYearMaintenance) {
      this.halfYearMaintenance = halfYearMaintenance;
   }

   public void setAnnualMaintenance(final List<String> annualMaintenance) {
      this.annualMaintenance = annualMaintenance;
   }
}
