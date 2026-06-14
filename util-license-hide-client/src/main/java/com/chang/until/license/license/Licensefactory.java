package com.chang.until.license.license;

import com.chang.until.license.LicenseClient;

import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Licensefactory {
   private static final Logger log = LoggerFactory.getLogger(Licensefactory.class);
   @Value("${license.subject:license_demo}")
   private String subject;
   @Value("${license.publicAlias:publicCert}")
   private String publicAlias;
   @Value("${license.storePass:public_password1234}")
   private String storePass;
   @Value("${license.licensePath:''}")
   private String licensePath;
   @Value("${license.publicKeysStorePath:''}")
   private String publicKeysStorePath;
   @Autowired
   private LicenseClient licenseClient;
   private static final AtomicBoolean isInstallLicense = new AtomicBoolean(false);
   public static final int licenseType = 3;

   public void installLicense() throws Exception {
      synchronized(this) {
         if (!isInstallLicense.get()) {
         }

      }
   }
}
