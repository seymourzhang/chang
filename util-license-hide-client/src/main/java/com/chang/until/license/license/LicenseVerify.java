package com.chang.until.license.license;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import java.io.File;
import java.util.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LicenseVerify {
   private static final Logger log = LoggerFactory.getLogger(LicenseVerify.class);

   public LicenseContent install(LicenseVerifyParam param) {
      LicenseContent result = null;

      try {
         LicenseManager licenseManager = LicenseManagerHolder.getInstance(this.initLicenseParam(param));
         licenseManager.uninstall();
         result = licenseManager.install(new File(param.getLicensePath()));
      } catch (Exception var4) {
      }

      return result;
   }

   public boolean verify() throws Exception {
      LicenseManager licenseManager = LicenseManagerHolder.getInstance((LicenseParam)null);
      LicenseContent licenseContent = licenseManager.verify();
      return true;
   }

   private LicenseParam initLicenseParam(LicenseVerifyParam param) {
      Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);
      CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());
      KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class, param.getPublicKeysStorePath(), param.getPublicAlias(), param.getStorePass(), (String)null);
      return new DefaultLicenseParam(param.getSubject(), preferences, publicStoreParam, cipherParam);
   }
}
