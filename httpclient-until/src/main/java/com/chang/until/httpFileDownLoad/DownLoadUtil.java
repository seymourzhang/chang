package com.chang.until.httpFileDownLoad;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import java.io.File;

public class DownLoadUtil {
   public static String BreakPointDownLoad(SiteInfoBean siteInfoBean, DownLoadCallBack downLoadCallBack) throws Exception {
      String pathAndName = siteInfoBean.getFilePath() + File.separator + siteInfoBean.getFileName();
      File file = new File(pathAndName);
      if (!file.exists()) {
         file.createNewFile();
      }

      SiteFileFetch siteFileFetch = new SiteFileFetch(file, pathAndName, siteInfoBean.getUrlDownLoad(), siteInfoBean.getFileSize());

      while(true) {
         long size = FileUtil.size(file);
         if (ObjectUtil.equal(size, siteInfoBean.getFileSize())) {
            return DigestUtil.md5Hex(file);
         }

         siteFileFetch.refreshStartSize();
         siteFileFetch.breakPointRenewalDownLoad();
         double progress = (double)size / Double.valueOf((double)siteInfoBean.getFileSize());
         downLoadCallBack.callBack(progress);
      }
   }
}
