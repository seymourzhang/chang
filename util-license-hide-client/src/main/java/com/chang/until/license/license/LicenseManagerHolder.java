package com.chang.until.license.license;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

public class LicenseManagerHolder {
   private static volatile LicenseManager LICENSE_MANAGER;

   public static LicenseManager getInstance(LicenseParam param) {
      if (LICENSE_MANAGER == null) {
         Class var1 = LicenseManagerHolder.class;
         synchronized(LicenseManagerHolder.class) {
            if (LICENSE_MANAGER == null) {
               LICENSE_MANAGER = new CustomLicenseManager(param);
            }
         }
      }

      return LICENSE_MANAGER;
   }
}
