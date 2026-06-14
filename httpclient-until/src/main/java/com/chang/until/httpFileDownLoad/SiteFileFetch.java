package com.chang.until.httpFileDownLoad;

import cn.hutool.core.io.FileUtil;
import java.io.File;

public class SiteFileFetch {
   private long nStartPos;
   private long nEndPos;
   private String downLoadUrl;
   private File downLoadFile;
   private String pathAndName;

   public SiteFileFetch(File file, String pathAndName, String downLoadUrl, Long fileSize) throws Exception {
      this.pathAndName = pathAndName;
      this.downLoadFile = file;
      this.downLoadUrl = downLoadUrl;
      this.nEndPos = fileSize;
      this.nStartPos = FileUtil.size(this.downLoadFile);
   }

   public void refreshStartSize() {
      this.nStartPos = FileUtil.size(this.downLoadFile);
   }

   public void breakPointRenewalDownLoad() throws Exception {
      FileFetch fileFetch = new FileFetch(this.downLoadUrl, this.pathAndName, this.nStartPos, this.nEndPos);
      fileFetch.fileDownLoad();
   }
}
