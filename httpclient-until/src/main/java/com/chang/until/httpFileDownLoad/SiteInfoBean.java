package com.chang.until.httpFileDownLoad;

public class SiteInfoBean {
   private String urlDownLoad;
   private Long fileSize;
   private String filePath;
   private String fileName;

   public SiteInfoBean(String urlDownLoad, Long fileSize, String filePath, String fileName) {
      this.urlDownLoad = urlDownLoad;
      this.fileSize = fileSize;
      this.filePath = filePath;
      this.fileName = fileName;
   }

   public String getUrlDownLoad() {
      return this.urlDownLoad;
   }

   public Long getFileSize() {
      return this.fileSize;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public String getFileName() {
      return this.fileName;
   }
}
