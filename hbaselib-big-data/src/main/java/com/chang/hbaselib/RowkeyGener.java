package com.chang.hbaselib;

import java.util.Random;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;

public class RowkeyGener {
   private static Long MAXTIME = 9999999999999L;
   private static int RANDOM_CEIL = 999999999;

   public static String generkey(String tablename) {
      long time = System.currentTimeMillis();
      Random random = new Random();
      int offset = random.nextInt(RANDOM_CEIL);
      long timeing = MAXTIME - time + (long)offset;
      return MD5Hash.getMD5AsHex(Bytes.toBytes(timeing + tablename));
   }

   public static void main(String[] args) {
      for(int i = 0; i < 100; ++i) {
         System.out.println(generkey("weiwei"));
      }

   }
}
