package com.chang.fileutil;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.alibaba.fastjson.util.IOUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
   private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

   public static byte[] readFileByBytes(String fileName) throws IOException {
      File file = new File(fileName);
      byte[] returnbytes = new byte[(int)file.length()];
      InputStream in = Files.newInputStream(Paths.get(fileName));
      in.read(returnbytes);
      in.close();
      return returnbytes;
   }

   public static byte[] readFileByBytes(File file) throws IOException {
      BufferedInputStream inputStream = cn.hutool.core.io.FileUtil.getInputStream(file);
      return IoUtil.readBytes(inputStream);
   }

   public static void writeFileByBytes(byte[] fileDate, String fileName) throws IOException {
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
      out.write(fileDate);
      out.flush();
      out.close();
   }

   public static void writeInputStreamToLocal(String destination, InputStream input) throws IOException {
      byte[] bytes = new byte[1024];
      FileOutputStream downloadFile = new FileOutputStream(destination);

      int index;
      while((index = input.read(bytes)) != -1) {
         downloadFile.write(bytes, 0, index);
         downloadFile.flush();
      }

      downloadFile.close();
      input.close();
   }

   public static boolean createFile(String filename) {
      File f = new File(filename);
      boolean res = false;
      if (!f.exists()) {
         try {
            res = f.createNewFile();
            if (!res) {
               log.error("[method: createFile] [filename: " + filename + "] createNewFile fail!");
            }
         } catch (IOException var4) {
            log.error("[method: createFile] [filename: " + filename + "] exception ", var4);
         }
      } else if (f.isFile()) {
         res = true;
      } else {
         log.error("[method: createFile] [filename: " + filename + "] not a file!");
      }

      return res;
   }

   public static File createNewFile(String filename) {
      return createFile(filename) ? new File(filename) : null;
   }

   public static boolean createFolder(String folderPath) {
      File f = new File(folderPath);
      boolean res;
      if (!f.exists()) {
         res = f.mkdirs();
         if (!res) {
            log.error("[method: createFolder] [folderPath: " + folderPath + "] mkdirs fail");
         }
      } else if (!f.isDirectory()) {
         log.error("[method: createFolder] [folderPath: " + folderPath + "] not a directory!");
         res = false;
      } else {
         res = true;
      }

      return res;
   }

   public static boolean deleteFiles(List<String> files) {
      boolean result = false;
      if (files != null && files.size() > 0) {
         Iterator var2 = files.iterator();

         while(var2.hasNext()) {
            String path = (String)var2.next();
            File file = new File(path);
            if (file.exists()) {
               file.delete();
            }
         }

         result = true;
      }

      return result;
   }

   public static void delFileList(File file) {
      if (file.exists()) {
         if (file.isFile()) {
            file.delete();
         } else if (file.isDirectory()) {
            File[] files = file.listFiles();

            for(int i = 0; i < files.length; ++i) {
               delFileList(files[i]);
            }
         }

         file.delete();
      } else {
         log.error("[method: delFileList] [file: " + file.getName() + " not exist");
      }

   }

   public static void compressExe(String srcPathName, File zipFile) {
      File file = new File(srcPathName);
      if (!file.exists()) {
         throw new RuntimeException(srcPathName + "不存在！");
      } else {
         try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compressByType(file, out, basedir);
            out.close();
         } catch (Exception var7) {
            var7.printStackTrace();
            log.error("[method: compressExe] [folderPath: " + srcPathName + "] compress fail");
            throw new RuntimeException(var7);
         }
      }
   }

   private static void compressByType(File file, ZipOutputStream out, String basedir) {
      if (file.isDirectory()) {
         log.info("[method: compressExe]" + basedir + file.getName());
         compressDirectory(file, out, basedir);
      } else {
         log.info("[method: compressExe]" + basedir + file.getName());
         compressFile(file, out, basedir);
      }

   }

   private static void compressFile(File file, ZipOutputStream out, String basedir) {
      if (file.exists()) {
         try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            byte[] data = new byte[8192];

            int count;
            while((count = bis.read(data, 0, 8192)) != -1) {
               out.write(data, 0, count);
            }

            bis.close();
         } catch (Exception var7) {
            throw new RuntimeException(var7);
         }
      }
   }

   private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
      if (dir.exists()) {
         File[] files = dir.listFiles();

         for(int i = 0; i < files.length; ++i) {
            compressByType(files[i], out, basedir + dir.getName() + "/");
         }

      }
   }

   public static boolean zipCompress(List<String> files, String zipname) {
      if (files != null && zipname != null && files.size() >= 1) {
         BufferedOutputStream out = null;

         try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipname));
            out = new BufferedOutputStream(zos);
            File file = null;
            FileInputStream fin = null;
            BufferedInputStream bis = null;
            Iterator var7 = files.iterator();

            while(var7.hasNext()) {
               String filepath = (String)var7.next();

               try {
                  file = new File(filepath);
                  fin = new FileInputStream(file);
                  bis = new BufferedInputStream(fin);
                  zos.putNextEntry(new ZipEntry(file.getName()));
                  byte[] b = new byte[16384];

                  int c;
                  while((c = bis.read(b)) != -1) {
                     out.write(b, 0, c);
                  }
               } catch (IOException var30) {
                  throw var30;
               } finally {
                  if (bis != null) {
                     bis.close();
                  }

                  out.flush();
               }
            }

            boolean var35 = true;
            return var35;
         } catch (FileNotFoundException var32) {
            log.error("[method: zipCompress] FileNotFoundException ", var32);
         } catch (IOException var33) {
            log.error("[method: zipCompress] IOException ", var33);
         } finally {
            if (out != null) {
               try {
                  out.close();
               } catch (IOException var29) {
                  log.error("[method: zipCompress] IOException ", var29);
               }
            }

         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean writeStr(String content, String filename) {
      if (createFile(filename)) {
         BufferedWriter bw = null;

         boolean var4;
         try {
            FileWriter fw = new FileWriter(filename);
            bw = new BufferedWriter(fw);
            bw.write(content);
            var4 = true;
         } catch (IOException var14) {
            log.error("[method: writeStr] IOException ", var14);
            return false;
         } finally {
            if (bw != null) {
               try {
                  bw.flush();
                  bw.close();
               } catch (IOException var13) {
                  log.error("[method: writeStr] IOException ", var13);
               }
            }

         }

         return var4;
      } else {
         return false;
      }
   }

   public static void writeStr(String content, Writer w) throws IOException {
      w.write(content);
   }

   public static boolean isSuffixAble(String fileName, String[] suffix) {
      boolean result = false;
      if (fileName != null) {
         int lastIndex = fileName.lastIndexOf(".");
         if (lastIndex != -1 && lastIndex < fileName.length() - 1) {
            String curSuffix = fileName.substring(lastIndex + 1);
            String[] var5 = suffix;
            int var6 = suffix.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String temp = var5[var7];
               if (curSuffix.equalsIgnoreCase(temp)) {
                  result = true;
                  break;
               }
            }
         }
      }

      return result;
   }

   public static String getSuffix(String fileName) {
      String result = "";
      if (fileName != null) {
         int lastIndex = fileName.lastIndexOf(".");
         if (lastIndex != -1 && lastIndex < fileName.length() - 1) {
            result = fileName.substring(lastIndex + 1);
         }
      }

      return result;
   }

   public static String getFileName(String path) {
      String result = "";
      if (path != null) {
         int lastIndex;
         if (path.lastIndexOf("\\") != -1) {
            lastIndex = path.lastIndexOf("\\");
            if (lastIndex < path.length() - 1) {
               result = path.substring(lastIndex + 1);
            }
         } else if (path.lastIndexOf("/") != -1) {
            lastIndex = path.lastIndexOf("/");
            if (lastIndex < path.length() - 1) {
               result = path.substring(lastIndex + 1);
            }
         } else {
            result = path;
         }
      }

      return result;
   }

   public static File getExistFile(String filename) {
      File f = new File(filename);
      return f.exists() && f.isFile() ? f : null;
   }

   public static byte[] downloadFile(String uri) {
      URL url = null;
      HttpURLConnection urlConn = null;
      InputStream in = null;
      byte[] fileByte = null;

      try {
         url = new URL(uri);
         urlConn = (HttpURLConnection)url.openConnection();
         urlConn.connect();
         in = urlConn.getInputStream();
         fileByte = readContent(in, urlConn.getContentLength());
      } catch (MalformedURLException var16) {
         var16.printStackTrace();
         log.info("FileServerUtils.downloadFile() is failre,the reason is " + var16.fillInStackTrace());
         fileByte = null;
      } catch (IOException var17) {
         var17.printStackTrace();
         log.info("FileServerUtils.downloadFile() is failre,the reason is " + var17.fillInStackTrace());
         fileByte = null;
      } finally {
         if (in != null) {
            try {
               in.close();
            } catch (IOException var15) {
               var15.printStackTrace();
            }
         }

         if (urlConn != null) {
            urlConn.disconnect();
         }

      }

      return fileByte;
   }

   public static byte[] readContent(InputStream in, int bufferSize) throws IOException {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      byte[] buf = new byte[bufferSize];
      int c = 0;
      int b;
      while(c < buf.length && (b = in.read(buf, c, buf.length - c)) >= 0) {
         c += b;
         if (c == bufferSize) {
            bout.write(buf);
            buf = new byte[bufferSize];
            c = 0;
         }
      }

      if (c != 0) {
         bout.write(buf, 0, c);
      }

      return bout.toByteArray();
   }

   public static String getMD5(InputStream is) throws NoSuchAlgorithmException, IOException {
      StringBuffer md5 = new StringBuffer();
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] dataBytes = new byte[1024];
      int nread;
      while((nread = is.read(dataBytes)) != -1) {
         md.update(dataBytes, 0, nread);
      }

      byte[] mdbytes = md.digest();

      for(int i = 0; i < mdbytes.length; ++i) {
         md5.append(Integer.toString((mdbytes[i] & 255) + 256, 16).substring(1));
      }

      return md5.toString();
   }

   public static String getMD5(byte[] fileDate) throws NoSuchAlgorithmException {
      StringBuffer md5 = new StringBuffer();
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(fileDate);
      byte[] mdbytes = md.digest();

      for(int i = 0; i < mdbytes.length; ++i) {
         md5.append(Integer.toString((mdbytes[i] & 255) + 256, 16).substring(1));
      }

      return md5.toString();
   }

   public static File getTempDirectory() {
      return new File(System.getProperty("java.io.tmpdir"));
   }

   public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
      writeByteArrayToFile(file, data, false);
   }

   public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
      writeByteArrayToFile(file, data, 0, data.length, append);
   }

   public static void writeByteArrayToFile(File file, byte[] data, int off, int len) throws IOException {
      writeByteArrayToFile(file, data, off, len, false);
   }

   public static void writeByteArrayToFile(File file, byte[] data, int off, int len, boolean append) throws IOException {
      FileOutputStream out = null;

      try {
         out = openOutputStream(file, append);
         out.write(data, off, len);
      } finally {
         IOUtils.close(out);
      }

   }

   public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
      if (file.exists()) {
         if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
         }

         if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
         }
      } else {
         File parent = file.getParentFile();
         if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("Directory '" + parent + "' could not be created");
         }
      }

      return new FileOutputStream(file, append);
   }

   public static void copy(InputStream in, OutputStream out) throws IOException {
      byte[] buffer = new byte[1024];

      int len;
      while((len = in.read(buffer)) != -1) {
         out.write(buffer, 0, len);
      }

   }

   public static byte[] readFileToByteArray(File file) throws IOException {
      InputStream in = null;

      byte[] var3;
      try {
         in = new FileInputStream(file);
         ByteArrayOutputStream result = new ByteArrayOutputStream();
         copy(in, result);
         result.close();
         var3 = result.toByteArray();
      } finally {
         IOUtils.close(in);
      }

      return var3;
   }

   public static File getResource(String path) throws Exception {
      File file = File.createTempFile("Temp_", ".temp");
      if (!file.exists() || file.length() == 0L) {
         InputStream cotInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
         writeInputStreamToLocal(file.getAbsolutePath(), cotInputStream);
      }

      return file;
   }

   public static int fileWrite(String filePath, byte[] src, int index) {
      try {
         RandomAccessFile randomAccessTargetFile = new RandomAccessFile(filePath, "rw");
         FileChannel targetFileChannel = randomAccessTargetFile.getChannel();
         MappedByteBuffer map = targetFileChannel.map(MapMode.READ_WRITE, (long)index, (long)src.length);
         map.put(src);
         int position = map.position();
         map.force();
         randomAccessTargetFile.close();
         targetFileChannel.close();
         return position;
      } catch (Exception var7) {
         log.error("fileWrite: ", var7);
         return 0;
      }
   }

   public static byte[] fileRead(String filePath, int index, int length) {
      try {
         RandomAccessFile randomAccessTargetFile = new RandomAccessFile(filePath, "rw");
         FileChannel targetFileChannel = randomAccessTargetFile.getChannel();
         MappedByteBuffer map = targetFileChannel.map(MapMode.READ_WRITE, (long)index, (long)length);
         byte[] byteArr = new byte[length];
         map.get(byteArr);
         randomAccessTargetFile.close();
         targetFileChannel.close();
         return byteArr;
      } catch (Exception var7) {
         log.error("fileRead: ", var7);
         return new byte[0];
      }
   }

   public static void EncryptFile(String filePath, String outPutPath, String keyOutPath) {
      byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
      AES aes = SecureUtil.aes(key);
      byte[] bytes = cn.hutool.core.io.FileUtil.readBytes(filePath);
      byte[] encrypt = aes.encrypt(bytes);
      cn.hutool.core.io.FileUtil.writeBytes(encrypt, new File(outPutPath));
      cn.hutool.core.io.FileUtil.writeBytes(key, new File(keyOutPath));
   }

   public static void DecryptFile(String encryptFilePath, String outPutPath, String keyPath) {
      byte[] key = cn.hutool.core.io.FileUtil.readBytes(keyPath);
      AES aes = SecureUtil.aes(key);
      byte[] encryptFile = cn.hutool.core.io.FileUtil.readBytes(encryptFilePath);
      byte[] decryptFile = aes.decrypt(encryptFile);
      cn.hutool.core.io.FileUtil.writeBytes(decryptFile, new File(outPutPath));
   }

   public static void DecryptFileEx(String encryptFilePath, String outPutPath, String keyPath) {
      ClassLoader classLoader = FileUtil.class.getClassLoader();
      InputStream resourceAsStream = classLoader.getResourceAsStream(keyPath);
      byte[] key = IoUtil.readBytes(resourceAsStream);
      AES aes = SecureUtil.aes(key);
      byte[] encryptFile = cn.hutool.core.io.FileUtil.readBytes(encryptFilePath);
      byte[] decryptFile = aes.decrypt(encryptFile);
      cn.hutool.core.io.FileUtil.writeBytes(decryptFile, new File(outPutPath));
   }

   public static InputStream DecryptFileStream(InputStream encryptInput, String keyPath) {
      ClassLoader classLoader = FileUtil.class.getClassLoader();
      InputStream resourceAsStream = classLoader.getResourceAsStream(keyPath);
      byte[] key = IoUtil.readBytes(resourceAsStream);
      AES aes = SecureUtil.aes(key);
      byte[] encryptFileByte = IoUtil.readBytes(encryptInput);
      byte[] decryptFileByte = aes.decrypt(encryptFileByte);
      return new ByteArrayInputStream(decryptFileByte);
   }

   public static List<String> unzipFile(String zipPath, String descDir, String charSetName) throws IOException {
      try {
         File zipFile = new File(zipPath);
         if (!zipFile.exists()) {
            throw new IOException("要解压的压缩文件不存在");
         } else {
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
               pathFile.mkdirs();
            }

            InputStream input = Files.newInputStream(Paths.get(zipPath));
            return unzipWithStream(input, descDir, charSetName);
         }
      } catch (Exception var6) {
         throw new IOException(var6);
      }
   }

   public static void toZip(String zipFileName, String sourceFileName, boolean KeepDirStructure) {
      long start = System.currentTimeMillis();
      ZipOutputStream zos = null;

      try {
         FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
         zos = new ZipOutputStream(fileOutputStream);
         File sourceFile = new File(sourceFileName);
         compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
         long end = System.currentTimeMillis();
         log.info("压缩完成，耗时：" + (end - start) + " 毫秒");
      } catch (Exception var18) {
         log.error("toZip Err", var18);
      } finally {
         if (zos != null) {
            try {
               zos.close();
            } catch (IOException var17) {
               log.error("toZip Err", var17);
            }
         }

      }

   }

   public static void toZip(String zipFileName, List<File> srcFiles) throws Exception {
      long start = System.currentTimeMillis();
      ZipOutputStream zos = null;

      try {
         FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
         zos = new ZipOutputStream(fileOutputStream);
         Iterator var6 = srcFiles.iterator();

         while(var6.hasNext()) {
            File srcFile = (File)var6.next();
            compress(srcFile, zos, srcFile.getName(), true);
         }

         long end = System.currentTimeMillis();
         log.info("压缩完成，耗时：" + (end - start) + " 毫秒");
      } catch (Exception var15) {
         log.error("zip error from ZipUtils", var15);
         throw new RuntimeException("zip error from ZipUtils", var15);
      } finally {
         if (zos != null) {
            try {
               zos.close();
            } catch (IOException var14) {
               log.error("zip error from ZipUtils", var14);
            }
         }

      }
   }

   public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
      byte[] buf = new byte[1024];
      if (sourceFile.isFile()) {
         zos.putNextEntry(new ZipEntry(name));
         FileInputStream in = new FileInputStream(sourceFile);

         int len;
         while((len = in.read(buf)) != -1) {
            zos.write(buf, 0, len);
         }

         zos.closeEntry();
         in.close();
      } else {
         File[] listFiles = sourceFile.listFiles();
         if (listFiles != null && listFiles.length != 0) {
            File[] var11 = listFiles;
            int var7 = listFiles.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               File file = var11[var8];
               if (KeepDirStructure) {
                  compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
               } else {
                  compress(file, zos, file.getName(), KeepDirStructure);
               }
            }
         } else if (KeepDirStructure) {
            zos.putNextEntry(new ZipEntry(name + "/"));
            zos.closeEntry();
         }
      }

   }

   public static List<String> unzipWithStream(InputStream inputStream, String descDir, String charSetName) {
      if (!descDir.endsWith(File.separator)) {
         descDir = descDir + File.separator;
      }

      try {
         ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName(charSetName));
         Throwable var4 = null;

         try {
            java.util.zip.ZipEntry zipEntry;
            try {
               while((zipEntry = zipInputStream.getNextEntry()) != null) {
                  String zipEntryNameStr = zipEntry.getName();
                  String zipEntryName = zipEntryNameStr;
                  String outPath;
                  if (zipEntryNameStr.contains("/")) {
                     outPath = zipEntryNameStr.substring(0, zipEntryNameStr.indexOf("/"));
                     zipEntryName = zipEntryNameStr.substring(outPath.length() + 1);
                  }

                  outPath = (descDir + zipEntryName).replace("\\\\", "/");
                  File outFile = new File(outPath.substring(0, outPath.lastIndexOf(47)));
                  if (!outFile.exists()) {
                     outFile.mkdirs();
                  }

                  if (!(new File(outPath)).isDirectory()) {
                     writeFile(outPath, zipInputStream);
                     zipInputStream.closeEntry();
                  }
               }
            } catch (Throwable var18) {
               var4 = var18;
               throw var18;
            }
         } finally {
            if (zipInputStream != null) {
               if (var4 != null) {
                  try {
                     zipInputStream.close();
                  } catch (Throwable var17) {
                     var4.addSuppressed(var17);
                  }
               } else {
                  zipInputStream.close();
               }
            }

         }
      } catch (IOException var20) {
         log.error("unzipWithStream Err", var20);
         throw new RuntimeException("unzipWithStream Err");
      }

      List<String> fileNames = cn.hutool.core.io.FileUtil.listFileNames(descDir);
      if (!descDir.endsWith("/")) {
         descDir = descDir + "/";
      }

      String finalDescDir = descDir;
      return (List)fileNames.stream().map((x) -> {
         return finalDescDir + x;
      }).collect(Collectors.toList());
   }

   public static void writeFile(String filePath, ZipInputStream zipInputStream) {
      try {
         OutputStream outputStream = Files.newOutputStream(Paths.get(filePath));
         Throwable var3 = null;

         try {
            byte[] bytes = new byte[4096];

            int len;
            while((len = zipInputStream.read(bytes)) != -1) {
               outputStream.write(bytes, 0, len);
            }
         } catch (Throwable var14) {
            var3 = var14;
            throw var14;
         } finally {
            if (outputStream != null) {
               if (var3 != null) {
                  try {
                     outputStream.close();
                  } catch (Throwable var13) {
                     var3.addSuppressed(var13);
                  }
               } else {
                  outputStream.close();
               }
            }

         }

      } catch (IOException var16) {
         log.error("writeFile Err", var16);
         throw new RuntimeException("writeFile Err");
      }
   }

   public static List<File> getAllFilesByExtName(String path, int maxDepth, String extName) {
      return cn.hutool.core.io.FileUtil.loopFiles(cn.hutool.core.io.FileUtil.file(path), maxDepth, (pathfile) -> {
         String extNameTemp = FileNameUtil.extName(pathfile);
         return StringUtils.isNoneBlank(new CharSequence[]{extNameTemp}) && extNameTemp.equals(extName);
      });
   }
}
