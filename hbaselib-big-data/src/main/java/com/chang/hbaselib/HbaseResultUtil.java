package com.chang.hbaselib;

import cn.hutool.core.util.ArrayUtil;
import com.chang.store.HbaseDataStore;
import java.util.Iterator;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseResultUtil {
   private static String getRowKey(Cell cell) {
      return Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
   }

   private static String getQualifier(Cell cell) {
      return Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
   }

   private static byte[] getQualifierValue(Cell cell) {
      return ArrayUtil.sub(cell.getValueArray(), cell.getValueOffset(), cell.getValueOffset() + cell.getValueLength());
   }

   public static HbaseDataStore getQueryResult(Result result, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Cell[] var3 = result.rawCells();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Cell cell = var3[var5];
         hbaseDataStore.add(getRowKey(cell), getQualifier(cell), getQualifierValue(cell));
      }

      return hbaseDataStore;
   }

   public static HbaseDataStore getQueryResult(ResultScanner resultScanner, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Iterator var3 = resultScanner.iterator();

      while(var3.hasNext()) {
         Result result = (Result)var3.next();
         Cell[] cells = result.rawCells();
         Cell[] var6 = cells;
         int var7 = cells.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Cell cell = var6[var8];
            hbaseDataStore.add(getRowKey(cell), getQualifier(cell), getQualifierValue(cell));
         }
      }

      return hbaseDataStore;
   }
}
