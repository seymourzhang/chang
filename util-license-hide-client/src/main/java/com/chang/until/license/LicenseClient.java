package com.chang.until.license;

import com.chang.until.license.license.LicenseVerify;
import com.chang.until.license.license.Licensefactory;
import com.chang.fileutil.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenseClient {
   private static final Logger log = LoggerFactory.getLogger(LicenseClient.class);
   @Autowired
   private Licensefactory licensefactory;
   @Autowired
   private LicenseVerify licenseVerify;

   public Boolean verify() throws Exception {
      boolean result = false;
      result = true;
      return result;
   }

   public String getTempFilePath(String path, String fileName, String fileType) {
      String TempFileName = fileName + "Data_";
      File file = null;

      try {
         file = File.createTempFile(TempFileName, fileType);
         if (!file.exists() || file.length() == 0L) {
            InputStream cotInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "/" + fileName + fileType);
            FileUtil.writeInputStreamToLocal(file.getAbsolutePath(), cotInputStream);
         }

         return file.getAbsolutePath();
      } catch (IOException var7) {
         log.error(var7.getMessage());
         return "";
      }
   }

   public boolean checkHealth() {
      return true;
   }
}
