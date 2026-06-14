package com.chang.until.license.license;

import com.chang.common.LogUtil;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CustomLicenseManager extends LicenseManager {
   private static final LogUtil logger = LogUtil.getLogger(CustomLicenseManager.class);
   private static final String XML_CHARSET = "UTF-8";
   private static final int DEFAULT_BUFSIZE = 8192;

   public CustomLicenseManager(LicenseParam param) {
      super(param);
   }

   protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
      this.initialize(content);
      this.validateCreate(content);
      GenericCertificate certificate = notary.sign(content);
      return this.getPrivacyGuard().cert2key(certificate);
   }

   protected synchronized LicenseContent install(byte[] key, LicenseNotary notary) throws Exception {
      GenericCertificate certificate = this.getPrivacyGuard().key2cert(key);
      notary.verify(certificate);
      LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
      this.validate(content);
      this.setLicenseKey(key);
      this.setCertificate(certificate);
      return content;
   }

   protected synchronized LicenseContent verify(LicenseNotary notary) throws Exception {
      GenericCertificate certificate = this.getCertificate();
      byte[] key = this.getLicenseKey();
      if (null == key) {
         throw new NoLicenseInstalledException(this.getLicenseParam().getSubject());
      } else {
         certificate = this.getPrivacyGuard().key2cert(key);
         notary.verify(certificate);
         LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
         this.validate(content);
         this.setCertificate(certificate);
         return content;
      }
   }

   protected synchronized void validateCreate(LicenseContent content) throws LicenseContentException {
      LicenseParam param = this.getLicenseParam();
      Date now = new Date();
      Date notBefore = content.getNotBefore();
      Date notAfter = content.getNotAfter();
      if (null != notAfter && now.after(notAfter)) {
         throw new LicenseContentException("证书失效时间不能早于当前时间");
      } else if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
         throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
      } else {
         String consumerType = content.getConsumerType();
         if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
         }
      }
   }

   protected synchronized void validate(LicenseContent content) throws LicenseContentException {
      super.validate(content);
      LicenseCheckModel expectedCheckModel = (LicenseCheckModel)content.getExtra();
      if (expectedCheckModel != null) {
         LicenseCheckModel serverCheckModel = this.getServerInfos();
         if (serverCheckModel == null) {
            throw new LicenseContentException("不能获取服务器硬件信息client");
         }

         if (!this.checkIpAddress(expectedCheckModel.getIpAddress(), serverCheckModel.getIpAddress())) {
            throw new LicenseContentException("当前服务器的IP没在授权范围内");
         }

         if (!this.checkIpAddress(expectedCheckModel.getMacAddress(), serverCheckModel.getMacAddress())) {
            throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
         }

         if (!this.checkSerial(expectedCheckModel.getMainBoardSerial(), serverCheckModel.getMainBoardSerial())) {
            throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
         }

         if (!this.checkSerial(expectedCheckModel.getCpuSerial(), serverCheckModel.getCpuSerial())) {
            throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
         }
      }

   }

   private Object load(String encoded) {
      BufferedInputStream inputStream = null;
      XMLDecoder decoder = null;

      try {
         inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes("UTF-8")));
         decoder = new XMLDecoder(new BufferedInputStream(inputStream, 8192), (Object)null, (ExceptionListener)null);
         Object var4 = decoder.readObject();
         return var4;
      } catch (UnsupportedEncodingException var14) {
         var14.printStackTrace();
      } finally {
         try {
            if (decoder != null) {
               decoder.close();
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (Exception var13) {
            logger.error("XMLDecoder解析XML失败", (Throwable)var13);
         }

      }

      return null;
   }

   private LicenseCheckModel getServerInfos() {
      String osName = System.getProperty("os.name").toLowerCase();
      AbstractServerInfos abstractServerInfos = null;
      if (osName.startsWith("windows")) {
         abstractServerInfos = new WindowsServerInfos();
      } else if (osName.startsWith("linux")) {
         abstractServerInfos = new LinuxServerInfos();
      } else {
         abstractServerInfos = new LinuxServerInfos();
      }

      return ((AbstractServerInfos)abstractServerInfos).getServerInfos();
   }

   private boolean checkIpAddress(List<String> expectedList, List<String> serverList) {
      if (expectedList != null && expectedList.size() > 0) {
         if (serverList != null && serverList.size() > 0) {
            Iterator var3 = expectedList.iterator();

            while(var3.hasNext()) {
               String expected = (String)var3.next();
               if (serverList.contains(expected.trim())) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean checkSerial(String expectedSerial, String serverSerial) {
      if (StringUtils.isNotBlank(expectedSerial)) {
         return StringUtils.isNotBlank(serverSerial) && expectedSerial.equals(serverSerial);
      } else {
         return true;
      }
   }

   public CustomLicenseManager() {
   }
}
