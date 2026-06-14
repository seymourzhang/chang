package com.chang.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class IOUtils {
   private static final int BUFFER_SIZE = 8192;
   public static final int EOF = -1;

   private IOUtils() {
   }

   public static long write(InputStream is, OutputStream os) throws IOException {
      return write((InputStream)is, (OutputStream)os, 8192);
   }

   public static long write(InputStream is, OutputStream os, int bufferSize) throws IOException {
      byte[] buff = new byte[bufferSize];
      return write(is, os, buff);
   }

   public static long write(InputStream input, OutputStream output, byte[] buffer) throws IOException {
      long count;
      int n;
      for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
         output.write(buffer, 0, n);
      }

      return count;
   }

   public static String read(Reader reader) throws IOException {
      StringWriter writer = new StringWriter();
      Throwable var2 = null;

      String var3;
      try {
         write((Reader)reader, (Writer)writer);
         var3 = writer.getBuffer().toString();
      } catch (Throwable var12) {
         var2 = var12;
         throw var12;
      } finally {
         if (writer != null) {
            if (var2 != null) {
               try {
                  writer.close();
               } catch (Throwable var11) {
                  var2.addSuppressed(var11);
               }
            } else {
               writer.close();
            }
         }

      }

      return var3;
   }

   public static long write(Writer writer, String string) throws IOException {
      Reader reader = new StringReader(string);
      Throwable var3 = null;

      long var4;
      try {
         var4 = write((Reader)reader, (Writer)writer);
      } catch (Throwable var14) {
         var3 = var14;
         throw var14;
      } finally {
         if (reader != null) {
            if (var3 != null) {
               try {
                  reader.close();
               } catch (Throwable var13) {
                  var3.addSuppressed(var13);
               }
            } else {
               reader.close();
            }
         }

      }

      return var4;
   }

   public static long write(Reader reader, Writer writer) throws IOException {
      return write((Reader)reader, (Writer)writer, 8192);
   }

   public static long write(Reader reader, Writer writer, int bufferSize) throws IOException {
      long total = 0L;

      int read;
      for(char[] buf = new char[bufferSize]; (read = reader.read(buf)) != -1; total += (long)read) {
         writer.write(buf, 0, read);
      }

      return total;
   }

   public static String[] readLines(File file) throws IOException {
      return file != null && file.exists() && file.canRead() ? readLines((InputStream)(new FileInputStream(file))) : new String[0];
   }

   public static String[] readLines(InputStream is) throws IOException {
      List<String> lines = new ArrayList();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      Throwable var3 = null;

      String[] var5;
      try {
         String line;
         while((line = reader.readLine()) != null) {
            lines.add(line);
         }

         var5 = (String[])lines.toArray(new String[0]);
      } catch (Throwable var14) {
         var3 = var14;
         throw var14;
      } finally {
         if (reader != null) {
            if (var3 != null) {
               try {
                  reader.close();
               } catch (Throwable var13) {
                  var3.addSuppressed(var13);
               }
            } else {
               reader.close();
            }
         }

      }

      return var5;
   }

   public static void writeLines(OutputStream os, String[] lines) throws IOException {
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
      Throwable var3 = null;

      try {
         String[] var4 = lines;
         int var5 = lines.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String line = var4[var6];
            writer.println(line);
         }

         writer.flush();
      } catch (Throwable var15) {
         var3 = var15;
         throw var15;
      } finally {
         if (writer != null) {
            if (var3 != null) {
               try {
                  writer.close();
               } catch (Throwable var14) {
                  var3.addSuppressed(var14);
               }
            } else {
               writer.close();
            }
         }

      }
   }

   public static void writeLines(File file, String[] lines) throws IOException {
      if (file == null) {
         throw new IOException("File is null.");
      } else {
         writeLines((OutputStream)(new FileOutputStream(file)), lines);
      }
   }

   public static void appendLines(File file, String[] lines) throws IOException {
      if (file == null) {
         throw new IOException("File is null.");
      } else {
         writeLines((OutputStream)(new FileOutputStream(file, true)), lines);
      }
   }

   public static String toString(InputStream inputStream) throws IOException {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];

      int length;
      while((length = inputStream.read(buffer)) != -1) {
         result.write(buffer, 0, length);
      }

      return result.toString("UTF-8");
   }

   public static void copy(InputStream in, OutputStream out) throws IOException {
      byte[] buffer = new byte[1024];

      int len;
      while((len = in.read(buffer)) != -1) {
         out.write(buffer, 0, len);
      }

   }

   public static byte[] getBytes(InputStream input) throws IOException {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      copy(input, result);
      result.close();
      return result.toByteArray();
   }

   public static IOException close(InputStream input) {
      return close((Closeable)input);
   }

   public static IOException close(OutputStream output) {
      return close((Closeable)output);
   }

   public static IOException close(Reader input) {
      return close((Closeable)input);
   }

   public static IOException close(Writer output) {
      return close((Closeable)output);
   }

   public static IOException close(Closeable closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }

         return null;
      } catch (IOException var2) {
         return var2;
      }
   }

   public static IOException close(ZipFile zip) {
      try {
         if (zip != null) {
            zip.close();
         }

         return null;
      } catch (IOException var2) {
         return var2;
      }
   }

   public static boolean isSubFile(File parent, File child) throws IOException {
      return child.getCanonicalPath().startsWith(parent.getCanonicalPath() + File.separator);
   }

   public static boolean isSubFile(String parent, String child) throws IOException {
      return isSubFile(new File(parent), new File(child));
   }

   public static void unzip(String zipFile, String extractFolder) throws IOException {
      File file = new File(zipFile);
      ZipFile zip = null;

      try {
         int BUFFER = 8192;
         zip = new ZipFile(file);
         File newPath = new File(extractFolder);
         newPath.mkdirs();
         Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

         while(true) {
            ZipEntry entry;
            File destFile;
            do {
               if (!zipFileEntries.hasMoreElements()) {
                  return;
               }

               entry = (ZipEntry)zipFileEntries.nextElement();
               String currentEntry = entry.getName();
               destFile = new File(newPath, currentEntry);
               if (!isSubFile(newPath, destFile)) {
                  throw new IOException("Bad zip entry: " + currentEntry);
               }

               File destinationParent = destFile.getParentFile();
               destinationParent.mkdirs();
            } while(entry.isDirectory());

            BufferedInputStream is = null;
            BufferedOutputStream dest = null;

            try {
               is = new BufferedInputStream(zip.getInputStream(entry));
               byte[] data = new byte[BUFFER];
               FileOutputStream fos = new FileOutputStream(destFile);
               dest = new BufferedOutputStream(fos, BUFFER);

               int currentByte;
               while((currentByte = is.read(data, 0, BUFFER)) != -1) {
                  dest.write(data, 0, currentByte);
               }

               dest.flush();
            } finally {
               close((OutputStream)dest);
               close((InputStream)is);
            }
         }
      } finally {
         close(zip);
      }
   }
}
