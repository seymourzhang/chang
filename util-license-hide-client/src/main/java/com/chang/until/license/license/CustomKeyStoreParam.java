package com.chang.until.license.license;

import de.schlichtherle.license.AbstractKeyStoreParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CustomKeyStoreParam extends AbstractKeyStoreParam {
   private String storePath;
   private String alias;
   private String storePwd;
   private String keyPwd;

   public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
      super(clazz, resource);
      this.storePath = resource;
      this.alias = alias;
      this.storePwd = storePwd;
      this.keyPwd = keyPwd;
   }

   public String getAlias() {
      return this.alias;
   }

   public String getStorePwd() {
      return this.storePwd;
   }

   public String getKeyPwd() {
      return this.keyPwd;
   }

   public InputStream getStream() throws IOException {
      InputStream in = new FileInputStream(new File(this.storePath));
      if (null == in) {
         throw new FileNotFoundException(this.storePath);
      } else {
         return in;
      }
   }
}
