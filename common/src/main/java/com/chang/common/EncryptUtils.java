package com.chang.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
   public static String getMD5String(String string) {
      if (org.apache.commons.lang3.StringUtils.isBlank(string)) {
         return null;
      } else {
         MessageDigest messageDigest = null;

         try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(string.getBytes("UTF-8"));
         } catch (NoSuchAlgorithmException var5) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
         } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
         }

         byte[] byteArray = messageDigest.digest();
         StringBuffer md5StrBuff = new StringBuffer();

         for(int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
               md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
               md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
         }

         return md5StrBuff.toString();
      }
   }
}
