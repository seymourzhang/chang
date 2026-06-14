package com.chang.until.license.license;

import java.io.Serializable;
import java.util.List;

public class LicenseCheckModel implements Serializable {
   private static final long serialVersionUID = 8600137500316662317L;
   private List<String> ipAddress;
   private List<String> macAddress;
   private String cpuSerial;
   private String mainBoardSerial;

   public String toString() {
      return "LicenseCheckModel{ipAddress=" + this.ipAddress + ", macAddress=" + this.macAddress + ", cpuSerial='" + this.cpuSerial + '\'' + ", mainBoardSerial='" + this.mainBoardSerial + '\'' + '}';
   }

   public List<String> getIpAddress() {
      return this.ipAddress;
   }

   public List<String> getMacAddress() {
      return this.macAddress;
   }

   public String getCpuSerial() {
      return this.cpuSerial;
   }

   public String getMainBoardSerial() {
      return this.mainBoardSerial;
   }

   public void setIpAddress(List<String> ipAddress) {
      this.ipAddress = ipAddress;
   }

   public void setMacAddress(List<String> macAddress) {
      this.macAddress = macAddress;
   }

   public void setCpuSerial(String cpuSerial) {
      this.cpuSerial = cpuSerial;
   }

   public void setMainBoardSerial(String mainBoardSerial) {
      this.mainBoardSerial = mainBoardSerial;
   }
}
