package com.chang.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
   protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   protected static MessageDigest messagedigest = null;

   private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
      char c0 = hexDigits[(bt & 240) >> 4];
      char c1 = hexDigits[bt & 15];
      stringbuffer.append(c0);
      stringbuffer.append(c1);
   }

   private static String bufferToHex(byte[] bytes) {
      return bufferToHex(bytes, 0, bytes.length);
   }

   private static String bufferToHex(byte[] bytes, int m, int n) {
      StringBuffer stringbuffer = new StringBuffer(2 * n);
      int k = m + n;

      for(int l = m; l < k; ++l) {
         appendHexPair(bytes[l], stringbuffer);
      }

      return stringbuffer.toString();
   }

   public static String getFileMD5String(File file) {
      FileInputStream in = null;
      FileChannel ch = null;
      String tempMD5 = "";

      try {
         in = new FileInputStream(file);
         ch = in.getChannel();
         MappedByteBuffer byteBuffer = ch.map(MapMode.READ_ONLY, 0L, file.length());
         messagedigest.update(byteBuffer);
         tempMD5 = bufferToHex(messagedigest.digest());
      } catch (FileNotFoundException var15) {
         var15.printStackTrace();
      } catch (IOException var16) {
         var16.printStackTrace();
      } finally {
         try {
            ch.close();
            ch = null;
            in.close();
            in = null;
         } catch (IOException var14) {
            var14.printStackTrace();
         }

      }

      return tempMD5;
   }

   public static String getFileMD5String2(File file) {
      FileInputStream in = null;
      String tempMD5 = "";

      try {
         in = new FileInputStream(file);
         byte[] cache = new byte[1048576];
         int len;
         while((len = in.read(cache)) > 0) {
            messagedigest.update(cache, 0, len);
         }

         tempMD5 = bufferToHex(messagedigest.digest());
      } catch (FileNotFoundException var15) {
         var15.printStackTrace();
      } catch (IOException var16) {
         var16.printStackTrace();
      } finally {
         try {
            in.close();
            in = null;
         } catch (IOException var14) {
            var14.printStackTrace();
         }

      }

      return tempMD5;
   }

   public static String getMD5String(String s) {
      return getMD5String(s.getBytes());
   }

   public static String getMD5String(byte[] bytes) {
      messagedigest.update(bytes);
      return bufferToHex(messagedigest.digest());
   }

   static {
      try {
         messagedigest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException var1) {
         System.err.println(MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
         var1.printStackTrace();
      }

   }
}
