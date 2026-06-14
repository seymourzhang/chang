package com.chang.common.gifauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {
   public static void close(InputStream in) {
      if (in != null) {
         try {
            in.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void close(OutputStream out) {
      if (out != null) {
         try {
            out.flush();
         } catch (IOException var3) {
         }

         try {
            out.close();
         } catch (IOException var2) {
         }
      }

   }
}
