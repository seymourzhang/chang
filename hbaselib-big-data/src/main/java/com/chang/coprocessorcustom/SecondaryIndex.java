package com.chang.coprocessorcustom;

import com.chang.coprocessor.RegionObserverCustom;
import com.chang.hbaselib.HbaseUtil;
import java.io.IOException;
import java.util.List;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.wal.WALEdit;

public class SecondaryIndex extends RegionObserverCustom {
   public String indexFamilyName = "data";
   public String qualifierName = "name";
   public String indexTableName = "indexTable";
   public String tableName = "testcopressor";
   public String familyName = "data";

   public void prePut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {
      HbaseUtil hbaseUtil = new HbaseUtil("cdh-124", "zookeeper-1:2181,zookeeper-2:2181,zookeeper-3:2181");
      if (!hbaseUtil.hasTable(this.indexTableName)) {
         hbaseUtil.creatTable(this.indexTableName, new String[]{this.indexFamilyName});
      }

      String RowKey = Bytes.toString(put.getRow());
      List<Cell> cells = put.get(Bytes.toBytes(this.familyName), Bytes.toBytes(this.qualifierName));
      if (null != cells && cells.size() > 0) {
         Cell cell = (Cell)cells.get(0);
         String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
         hbaseUtil.addData(value, this.indexTableName, this.indexFamilyName, new String[]{this.tableName + "_RowKey"}, new String[]{RowKey});
      }

   }
}
