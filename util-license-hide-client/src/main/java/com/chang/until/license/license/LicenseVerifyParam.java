package com.chang.until.license.license;

public class LicenseVerifyParam {
   private String subject;
   private String publicAlias;
   private String storePass;
   private String licensePath;
   private String publicKeysStorePath;

   public LicenseVerifyParam(String subject, String publicAlias, String storePass, String licensePath, String publicKeysStorePath) {
      this.subject = subject;
      this.publicAlias = publicAlias;
      this.storePass = storePass;
      this.licensePath = licensePath;
      this.publicKeysStorePath = publicKeysStorePath;
   }

   public String toString() {
      return "LicenseVerifyParam{subject='" + this.subject + '\'' + ", publicAlias='" + this.publicAlias + '\'' + ", storePass='" + this.storePass + '\'' + ", licensePath='" + this.licensePath + '\'' + ", publicKeysStorePath='" + this.publicKeysStorePath + '\'' + '}';
   }

   public String getSubject() {
      return this.subject;
   }

   public String getPublicAlias() {
      return this.publicAlias;
   }

   public String getStorePass() {
      return this.storePass;
   }

   public String getLicensePath() {
      return this.licensePath;
   }

   public String getPublicKeysStorePath() {
      return this.publicKeysStorePath;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public void setPublicAlias(String publicAlias) {
      this.publicAlias = publicAlias;
   }

   public void setStorePass(String storePass) {
      this.storePass = storePass;
   }

   public void setLicensePath(String licensePath) {
      this.licensePath = licensePath;
   }

   public void setPublicKeysStorePath(String publicKeysStorePath) {
      this.publicKeysStorePath = publicKeysStorePath;
   }

   public LicenseVerifyParam() {
   }
}
