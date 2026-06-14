package com.chang.hbaselib;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.CommUtils;
import com.chang.common.DataConsumer;
import com.chang.store.HbaseDataStore;
import com.chang.until.thread.threadpool.support.ThreadConfig;
import com.chang.until.thread.threadpool.support.cached.CachedThreadPool;
import com.chang.until.thread.threadpool.support.fixed.FixedThreadPool;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.RegionException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.BufferedMutatorParams;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.CoprocessorDescriptorBuilder;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.ColumnRangeFilter;
import org.apache.hadoop.hbase.filter.DependentColumnFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SkipFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.TimestampsFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.WhileMatchFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseUtil {
   private static final Logger log = LoggerFactory.getLogger(HbaseUtil.class);
   private Configuration conf = null;
   private Connection connection = null;
   private Admin admin = null;
   private static ExecutorService workerPool = null;
   private final CachedThreadPool cachedThreadPool = new CachedThreadPool();
   private final FixedThreadPool fixedThreadPool = new FixedThreadPool();
   private ExecutorService executor = null;
   private UserGroupInformation ugi = null;

   public void RefreshKerberosCertificate() throws IOException {
      if (ObjectUtil.isNotNull(this.ugi)) {
         this.ugi.reloginFromKeytab();
      } else {
         log.info("ugi is null Refresh fail");
      }

   }

   public HbaseUtil(String master, String zookeeper) {
      try {
         this.conf = HBaseConfiguration.create();
         this.conf.set("hbase.zookeeper.quorum", zookeeper);
         this.conf.set("hbase.master", master);
         this.connection = ConnectionFactory.createConnection(this.conf);
         this.admin = this.connection.getAdmin();
         ThreadConfig workerPoolThreadConfig = new ThreadConfig();
         workerPoolThreadConfig.setName("Hbase Work ThreadPool");
         workerPoolThreadConfig.setDumpPath("");
         workerPoolThreadConfig.setCores(Runtime.getRuntime().availableProcessors() * 2);
         workerPoolThreadConfig.setQueues(1000);
         workerPoolThreadConfig.setThreads(500);
         workerPoolThreadConfig.setAlive(500);
         workerPool = (ExecutorService)this.fixedThreadPool.getExecutor(workerPoolThreadConfig);
      } catch (IOException var4) {
         log.error(var4.getMessage());
         throw new RuntimeException(var4);
      }
   }

   public HbaseUtil() {
      try {
         this.conf = HBaseConfiguration.create();
         this.connection = ConnectionFactory.createConnection(this.conf);
         this.admin = this.connection.getAdmin();
         ThreadConfig workerPoolThreadConfig = new ThreadConfig();
         workerPoolThreadConfig.setName("Hbase Work ThreadPool");
         workerPoolThreadConfig.setDumpPath("");
         workerPoolThreadConfig.setCores(Runtime.getRuntime().availableProcessors() * 2);
         workerPoolThreadConfig.setQueues(1000);
         workerPoolThreadConfig.setThreads(500);
         workerPoolThreadConfig.setAlive(500);
         workerPool = (ExecutorService)this.fixedThreadPool.getExecutor(workerPoolThreadConfig);
      } catch (IOException var2) {
         log.error(var2.getMessage());
         throw new RuntimeException(var2);
      }
   }

   public HbaseUtil(String master, String zookeeper, ThreadConfig threadConfig) {
      try {
         this.conf = HBaseConfiguration.create();
         this.conf.set("hbase.zookeeper.quorum", zookeeper);
         this.conf.set("hbase.master", master);
         this.connection = ConnectionFactory.createConnection(this.conf);
         this.admin = this.connection.getAdmin();
         ThreadConfig workerPoolThreadConfig = new ThreadConfig();
         workerPoolThreadConfig.setName("Hbase Work ThreadPool");
         workerPoolThreadConfig.setDumpPath("");
         workerPoolThreadConfig.setCores(Runtime.getRuntime().availableProcessors() * 2);
         workerPoolThreadConfig.setQueues(1000);
         workerPoolThreadConfig.setThreads(500);
         workerPoolThreadConfig.setAlive(500);
         workerPool = (ExecutorService)this.fixedThreadPool.getExecutor(workerPoolThreadConfig);
         this.executor = (ExecutorService)this.cachedThreadPool.getExecutor(threadConfig);
      } catch (IOException var5) {
         log.error(var5.getMessage());
         throw new RuntimeException(var5);
      }
   }

   public HbaseUtil(String master, String zookeeper, String user, String krbConf, String keytab) {
      try {
         System.setProperty("java.security.krb5.conf", krbConf);
         System.setProperty("zookeeper.server.principal", user);
         this.conf = HBaseConfiguration.create();
         this.conf.set("hbase.zookeeper.quorum", zookeeper);
         this.conf.set("hbase.master", master);
         this.conf.set("hbase.cluster.distributed", "true");
         this.conf.set("hbase.rpc.protection", "authentication");
         this.conf.set("hadoop.security.authentication", "kerberos");
         this.conf.set("hbase.security.authentication", "kerberos");
         this.conf.set("hbase.regionserver.kerberos.principal", user);
         this.conf.set("hbase.master.kerberos.principal", user);
         UserGroupInformation.setConfiguration(this.conf);
         this.ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(user, keytab);
         UserGroupInformation.setLoginUser(this.ugi);
         HBaseAdmin.available(this.conf);
         this.connection = (Connection)this.ugi.doAs((PrivilegedAction<Connection>) () -> {
             try {
                 return ConnectionFactory.createConnection(this.conf);
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
         });
         this.admin = this.connection.getAdmin();
         ThreadConfig workerPoolThreadConfig = new ThreadConfig();
         workerPoolThreadConfig.setName("Hbase Work ThreadPool");
         workerPoolThreadConfig.setDumpPath("");
         workerPoolThreadConfig.setCores(Runtime.getRuntime().availableProcessors() * 2);
         workerPoolThreadConfig.setQueues(1000);
         workerPoolThreadConfig.setThreads(500);
         workerPoolThreadConfig.setAlive(500);
         workerPool = (ExecutorService)this.fixedThreadPool.getExecutor(workerPoolThreadConfig);
      } catch (Exception var7) {
         log.error(var7.getMessage());
         throw new RuntimeException(var7);
      }
   }

   public boolean isClose() {
      return this.connection.isClosed();
   }

   public void CloseHbaseClient() throws InterruptedException, ExecutionException, TimeoutException, IOException {
      Thread.sleep(1000L);
      workerPool.shutdown();
      this.connection.close();
      this.admin.close();
   }

   public TableDescriptorBuilder getTable(String tableName) {
      return TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
   }

   public ColumnFamilyDescriptorBuilder getFamily(String familyName) {
      return ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName));
   }

   public CoprocessorDescriptorBuilder getCoprocessor(String className) {
      return CoprocessorDescriptorBuilder.newBuilder(className);
   }

   public boolean hasTable(String tableName) throws IOException {
      return this.admin.tableExists(TableName.valueOf(tableName));
   }

   public byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
      byte[][] splits = new byte[numRegions - 1][];
      BigInteger lowestKey = new BigInteger(startKey, 16);
      BigInteger highestKey = new BigInteger(endKey, 16);
      BigInteger range = highestKey.subtract(lowestKey);
      BigInteger regionIncrement = range.divide(BigInteger.valueOf((long)numRegions));
      lowestKey = lowestKey.add(regionIncrement);

      for(int i = 0; i < numRegions - 1; ++i) {
         BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf((long)i)));
         byte[] b = String.format("%016x", key).getBytes();
         splits[i] = b;
      }

      return splits;
   }

   private void creatTable(String tableName, TableDescriptorBuilder desc, byte[][] splits) throws IOException {
      if (this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table Exists!");
      } else {
         if (null != splits) {
            this.admin.createTable(desc.build(), splits);
         } else {
            this.admin.createTable(desc.build());
         }

         if (this.admin.isTableAvailable(TableName.valueOf(tableName))) {
            log.info("create table Success!");
         } else {
            log.info("create table fail,Not available!");
         }

      }
   }

   public void creatTable(String tableName, String[] family, Compression.Algorithm CompressionType, boolean isMemory, BloomType bt) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setMaxVersions(3).setCompressionType(CompressionType).setInMemory(isMemory).setBloomFilterType(bt).build());
      }

      this.creatTable(tableName, desc, (byte[][])null);
   }

   public void creatTable(String tableName, String[] family, Compression.Algorithm CompressionType, boolean isMemory, BloomType bt, byte[][] splits) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setMaxVersions(3).setCompressionType(CompressionType).setInMemory(isMemory).setBloomFilterType(bt).build());
      }

      this.creatTable(tableName, desc, splits);
   }

   public void creatTable(String tableName, int ttlTime, String[] family, Compression.Algorithm CompressionType, boolean isMemory, BloomType bt, byte[][] splits) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setMaxVersions(3).setTimeToLive(ttlTime).setCompressionType(CompressionType).setInMemory(isMemory).setBloomFilterType(bt).build());
      }

      this.creatTable(tableName, desc, splits);
   }

   public void creatTable(String tableName, String[] family, boolean isMemory) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setMaxVersions(3).setInMemory(isMemory).build());
      }

      this.creatTable(tableName, desc, (byte[][])null);
   }

   public void creatTable(String tableName, int ttlTime, String[] family, boolean isMemory) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setTimeToLive(ttlTime).setMaxVersions(3).setInMemory(isMemory).build());
      }

      this.creatTable(tableName, desc, (byte[][])null);
   }

   public void creatTable(String tableName, String[] family) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setMaxVersions(3).build());
      }

      this.creatTable(tableName, desc, (byte[][])null);
   }

   public void creatTable(String tableName, int ttlTime, String[] family) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

      for(int i = 0; i < family.length; ++i) {
         desc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i])).setTimeToLive(ttlTime).setMaxVersions(3).build());
      }

      this.creatTable(tableName, desc, (byte[][])null);
   }

   public String GettableInfo(String tableName) throws TableNotFoundException, IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         TableDescriptor desc = this.admin.getDescriptor(TableName.valueOf(tableName));
         JSONObject tableinfo = new JSONObject();
         tableinfo.put("table_MaxFileSize", desc.getMaxFileSize());
         tableinfo.put("table_isReadOnly", desc.isReadOnly());
         tableinfo.put("table_MemStoreFlushSize", desc.getMemStoreFlushSize());
         ColumnFamilyDescriptor[] Families = desc.getColumnFamilies();
         ColumnFamilyDescriptor[] var5 = Families;
         int var6 = Families.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ColumnFamilyDescriptor fa = var5[var7];
            JSONObject Familiesinfo = new JSONObject();
            Familiesinfo.put("family_Name", Bytes.toString(fa.getName()));
            Familiesinfo.put("family_MaxVersions", fa.getMaxVersions());
            Compression.Algorithm type = fa.getCompactionCompressionType();
            if (type == Algorithm.GZ) {
               Familiesinfo.put("family_CompactionCompressionType", "GZ");
            } else if (type == Algorithm.LZ4) {
               Familiesinfo.put("family_CompactionCompressionType", "LZ4");
            } else if (type == Algorithm.LZO) {
               Familiesinfo.put("family_CompactionCompressionType", "LZO");
            } else if (type == Algorithm.NONE) {
               Familiesinfo.put("family_CompactionCompressionType", "NONE");
            } else if (type == Algorithm.SNAPPY) {
               Familiesinfo.put("family_CompactionCompressionType", "SNAPPY");
            } else {
               Familiesinfo.put("family_CompactionCompressionType", "NULL");
            }

            Familiesinfo.put("family_Blocksize", fa.getBlocksize());
            Familiesinfo.put("family_isBlockCacheEnabled", fa.isBlockCacheEnabled());
            Familiesinfo.put("family_isInMemory", fa.isInMemory());
            tableinfo.put("family_" + Bytes.toString(fa.getName()), Familiesinfo.toString());
         }

         return tableinfo.toString();
      }
   }

   public void addData(String rowKey, String tableName, String family, JSONObject columnJson) throws IOException {
      int size = columnJson.size();
      String[] column = new String[size];
      String[] value = new String[size];
      int offset = 0;

      for(Iterator var9 = columnJson.entrySet().iterator(); var9.hasNext(); ++offset) {
         Map.Entry<String, Object> entry = (Map.Entry)var9.next();
         column[offset] = (String)entry.getKey();
         value[offset] = ObjectUtil.isNull(entry.getValue()) ? "" : entry.getValue().toString();
      }

      this.addData(rowKey, tableName, family, column, value);
   }

   private byte[] getByte(Object value) throws Exception {
      Class<?> type = value.getClass();
      byte[] hbaseValue;
      if (BeanUtil.isBean(type)) {
         hbaseValue = Bytes.toBytes(CommUtils.getJSONStringFromObject(value));
      } else if (type == String.class) {
         hbaseValue = Bytes.toBytes((String)value);
      } else if (type == Byte.TYPE) {
         hbaseValue = new byte[]{(Byte)value};
      } else if (type == Byte.class) {
         hbaseValue = new byte[]{(Byte)value};
      } else if (type == Short.TYPE) {
         hbaseValue = Bytes.toBytes((Short)value);
      } else if (type == Short.class) {
         hbaseValue = Bytes.toBytes((Short)value);
      } else if (type == Integer.TYPE) {
         hbaseValue = Bytes.toBytes((Integer)value);
      } else if (type == Integer.class) {
         hbaseValue = Bytes.toBytes((Integer)value);
      } else if (type == Long.TYPE) {
         hbaseValue = Bytes.toBytes((Long)value);
      } else if (type == Long.class) {
         hbaseValue = Bytes.toBytes((Long)value);
      } else if (type == Float.TYPE) {
         hbaseValue = Bytes.toBytes((Float)value);
      } else if (type == Float.class) {
         hbaseValue = Bytes.toBytes((Float)value);
      } else if (type == Double.TYPE) {
         hbaseValue = Bytes.toBytes((Double)value);
      } else if (type == Double.class) {
         hbaseValue = Bytes.toBytes((Double)value);
      } else if (type == double[].class) {
         hbaseValue = CommUtils.doubleArrayToByteArray((double[])((double[])value));
      } else if (type == Double[].class) {
         hbaseValue = CommUtils.doubleArrayToByteArray((Double[])((Double[])value));
      } else if (type == float[].class) {
         hbaseValue = CommUtils.floatArrayToByteArray((float[])((float[])value));
      } else if (type == Float[].class) {
         hbaseValue = CommUtils.floatArrayToByteArray((Float[])((Float[])value));
      } else if (type == long[].class) {
         hbaseValue = CommUtils.longArrayToByteArray((long[])((long[])value));
      } else if (type == Long[].class) {
         hbaseValue = CommUtils.longArrayToByteArray((Long[])((Long[])value));
      } else if (type == int[].class) {
         hbaseValue = CommUtils.intArrayToByteArray((int[])((int[])value));
      } else if (type == Integer[].class) {
         hbaseValue = CommUtils.intArrayToByteArray((Integer[])((Integer[])value));
      } else if (type == short[].class) {
         hbaseValue = CommUtils.shortArrayToByteArray((short[])((short[])value));
      } else if (type == Short[].class) {
         hbaseValue = CommUtils.shortArrayToByteArray((Short[])((Short[])value));
      } else if (type == byte[].class) {
         hbaseValue = (byte[])((byte[])value);
      } else {
         if (type != Byte[].class) {
            throw new RuntimeException("type is Err and " + type.getName() + " won't support it");
         }

         hbaseValue = CommUtils.getBytes((Byte[])((Byte[])value));
      }

      return hbaseValue;
   }

   private Map<String, byte[]> getMap(Object o) throws Exception {
      Map<String, Object> bean = BeanUtil.beanToMap(o, (String[])null);
      Map<String, byte[]> columns = new HashMap();
      Iterator var4 = bean.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry<String, Object> entry = (Map.Entry)var4.next();
         Object value = entry.getValue();
         if (ObjectUtil.isNull(value)) {
            log.info((String)entry.getKey() + "is null");
         } else {
            columns.put(entry.getKey(), this.getByte(value));
         }
      }

      return columns;
   }

   public void addByteData(String rowKey, String tableName, String family, Object o) throws Exception {
      if (ObjectUtil.isNull(o)) {
         throw new RuntimeException("Object is not null");
      } else if (!BeanUtil.isBean(o.getClass())) {
         throw new RuntimeException("Object is not Bean");
      } else {
         this.addData(rowKey, tableName, family, this.getMap(o));
      }
   }

   public void addStringData(String rowKey, String tableName, String family, Object o) throws IOException {
      if (ObjectUtil.isNull(o)) {
         throw new RuntimeException("Object is null");
      } else if (!BeanUtil.isBean(o.getClass())) {
         throw new RuntimeException("Object is not bean");
      } else {
         if (o instanceof JSONObject) {
            this.addData(rowKey, tableName, family, (JSONObject)o);
         } else {
            this.addData(rowKey, tableName, family, new JSONObject(BeanUtil.beanToMap(o, (String[])null)));
         }

      }
   }

   public void addData(String rowKey, String tableName, String family, String[] column, String[] value) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         boolean familyexists = false;
         Table table = this.connection.getTable(TableName.valueOf(tableName), this.executor);
         Put put = new Put(Bytes.toBytes(rowKey));
         ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();

         for(int i = 0; i < columnFamilies.length; ++i) {
            String familyName = columnFamilies[i].getNameAsString();
            log.info("familyName:" + familyName);
            if (familyName.equals(family)) {
               for(int j = 0; j < column.length; ++j) {
                  put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column[j]), Bytes.toBytes(value[j]));
               }

               familyexists = true;
            }
         }

         if (!familyexists) {
            log.info("add data fail!");
         } else {
            log.info("add data Success!");
         }

         table.put(put);
         table.close();
      }
   }

   public void addData(String rowKey, String tableName, String family, Map<String, byte[]> columns) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         boolean familyExists = false;
         Table table = this.connection.getTable(TableName.valueOf(tableName), this.executor);
         Put put = new Put(Bytes.toBytes(rowKey));
         ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();

         for(int i = 0; i < columnFamilies.length; ++i) {
            String familyName = columnFamilies[i].getNameAsString();
            if (familyName.equals(family)) {
               Iterator var11 = columns.entrySet().iterator();

               while(var11.hasNext()) {
                  Map.Entry<String, byte[]> entry = (Map.Entry)var11.next();
                  put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes((String)entry.getKey()), (byte[])entry.getValue());
               }

               familyExists = true;
            }
         }

         if (!familyExists) {
            log.info("add data fail!");
         } else {
            log.info("add data Success!");
         }

         table.put(put);
         table.close();
      }
   }

   public Long counter(String rowKey, String tableName, String familyName, String column, long num) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();

         for(int i = 0; i < columnFamilies.length; ++i) {
            String name = columnFamilies[i].getNameAsString();
            log.info("familyName:" + familyName);
            if (name.equals(familyName)) {
               return table.incrementColumnValue(Bytes.toBytes(rowKey), Bytes.toBytes(familyName), Bytes.toBytes(column), num);
            }
         }

         return null;
      }
   }

   public void addCoprocessor(String tableName, String hdfsPath, String className) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
      TableName name = TableName.valueOf(tableName);
      Table table = this.connection.getTable(name);
      ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();
      this.admin.disableTable(name);
      ColumnFamilyDescriptor[] var8 = columnFamilies;
      int var9 = columnFamilies.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         ColumnFamilyDescriptor cfd = var8[var10];
         desc.setColumnFamily(cfd);
      }

      CoprocessorDescriptorBuilder cop = CoprocessorDescriptorBuilder.newBuilder(className);
      cop.setJarPath(hdfsPath).setPriority(1073741823);
      desc.setCoprocessor(cop.build());
      this.admin.modifyTable(desc.build());
      this.admin.enableTable(name);
   }

   public void deleteCoprocessor(String tableName) throws IOException {
      TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
      TableName name = TableName.valueOf(tableName);
      Table table = this.connection.getTable(name);
      ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();
      this.admin.disableTable(name);
      ColumnFamilyDescriptor[] var6 = columnFamilies;
      int var7 = columnFamilies.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         ColumnFamilyDescriptor cfd = var6[var8];
         desc.setColumnFamily(cfd);
      }

      this.admin.modifyTable(desc.build());
      this.admin.enableTable(name);
   }

   public Future<Void> addDataBufferedMutator(String rowKey, String tableName, String family, JSONObject columnJson) throws IOException, InterruptedException, ExecutionException, TimeoutException {
      int size = columnJson.size();
      if (size == 0) {
         throw new RuntimeException("size == 0");
      } else {
         String[] column = new String[size];
         String[] value = new String[size];
         int offset = 0;

         for(Iterator var9 = columnJson.entrySet().iterator(); var9.hasNext(); ++offset) {
            Map.Entry<String, Object> entry = (Map.Entry)var9.next();
            column[offset] = (String)entry.getKey();
            if (entry.getValue() != null) {
               value[offset] = entry.getValue().toString();
            } else {
               value[offset] = null;
            }
         }

         return this.addDataBufferedMutator(rowKey, tableName, family, column, value);
      }
   }

   private Future<Void> addDataBufferedMutator(final String rowKey, final String tableName, final String family, final String[] column, final String[] value) throws IOException, InterruptedException, ExecutionException, TimeoutException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
               for(int i = 0; i < e.getNumExceptions(); ++i) {
                  HbaseUtil.log.info("Failed to sent put " + e.getRow(i) + ".");
               }

            }
         };
         BufferedMutatorParams params = (new BufferedMutatorParams(TableName.valueOf(tableName))).listener(listener);
         final BufferedMutator mutator = this.connection.getBufferedMutator(params);
         Future<Void> future = workerPool.submit(new Callable<Void>() {
            public Void call() throws Exception {
               boolean familyexists = false;
               Table table = HbaseUtil.this.connection.getTable(TableName.valueOf(tableName));
               ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();
               table.close();
               Put put = new Put(Bytes.toBytes(rowKey));

               for(int i = 0; i < columnFamilies.length; ++i) {
                  String familyName = columnFamilies[i].getNameAsString();
                  HbaseUtil.log.info("familyName:" + familyName);
                  if (familyName.equals(family)) {
                     for(int j = 0; j < column.length; ++j) {
                        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column[j]), Bytes.toBytes(value[j]));
                     }

                     familyexists = true;
                  }
               }

               if (!familyexists) {
                  HbaseUtil.log.info("add data fail!");
               } else {
                  HbaseUtil.log.info("add data Success!");
               }

               mutator.mutate(put);
               mutator.flush();
               return null;
            }
         });
         return future;
      }
   }

   public Future<Void> addDataByteBufferedMutator(String rowKey, String tableName, String family, Object o) throws Exception {
      if (ObjectUtil.isNull(o)) {
         throw new RuntimeException("Object is not null");
      } else if (!BeanUtil.isBean(o.getClass())) {
         throw new RuntimeException("Object is not Bean");
      } else {
         return this.addDataByteBufferedMutator(rowKey, tableName, family, this.getMap(o));
      }
   }

   private Future<Void> addDataByteBufferedMutator(final String rowKey, final String tableName, final String family, final Map<String, byte[]> values) throws Exception {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
               for(int i = 0; i < e.getNumExceptions(); ++i) {
                  HbaseUtil.log.info("Failed to sent put " + e.getRow(i) + ".");
               }

            }
         };
         BufferedMutatorParams params = (new BufferedMutatorParams(TableName.valueOf(tableName))).listener(listener);
         final BufferedMutator mutator = this.connection.getBufferedMutator(params);
         Future<Void> future = workerPool.submit(new Callable<Void>() {
            public Void call() throws Exception {
               boolean familyexists = false;
               Table table = HbaseUtil.this.connection.getTable(TableName.valueOf(tableName));
               ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();
               table.close();
               Put put = new Put(Bytes.toBytes(rowKey));

               for(int i = 0; i < columnFamilies.length; ++i) {
                  String familyName = columnFamilies[i].getNameAsString();
                  HbaseUtil.log.info("familyName:" + familyName);
                  if (familyName.equals(family)) {
                     Iterator var7 = values.entrySet().iterator();

                     while(var7.hasNext()) {
                        Map.Entry<String, byte[]> entry = (Map.Entry)var7.next();
                        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes((String)entry.getKey()), (byte[])entry.getValue());
                     }

                     familyexists = true;
                  }
               }

               if (!familyexists) {
                  HbaseUtil.log.info("add data fail!");
               } else {
                  HbaseUtil.log.info("add data Success!");
               }

               mutator.mutate(put);
               mutator.flush();
               return null;
            }
         });
         return future;
      }
   }

   public void addDataForColumn(String rowKey, String tableName, String family, String column, String value) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         boolean familyexists = false;
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Put put = new Put(Bytes.toBytes(rowKey));
         ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();

         for(int i = 0; i < columnFamilies.length; ++i) {
            String familyName = columnFamilies[i].getNameAsString();
            log.info("familyName:" + familyName);
            if (familyName.equals(family)) {
               put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column), Bytes.toBytes(value));
               familyexists = true;
            }
         }

         if (!familyexists) {
            log.info("add data fail!");
         } else {
            log.info("add data Success!");
         }

         table.put(put);
         table.close();
      }
   }

   public void addByteData(String rowKey, String tableName, String family, String column, Object value) throws Exception {
      if (ObjectUtil.isNull(value)) {
         throw new RegionException("Object is null");
      } else {
         this.addDataForColumn(rowKey, tableName, family, column, this.getByte(value));
      }
   }

   public void addDataForColumn(String rowKey, String tableName, String family, String column, byte[] value) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         boolean familyexists = false;
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Put put = new Put(Bytes.toBytes(rowKey));
         ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();

         for(int i = 0; i < columnFamilies.length; ++i) {
            String familyName = columnFamilies[i].getNameAsString();
            log.info("familyName:" + familyName);
            if (familyName.equals(family)) {
               put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column), value);
               familyexists = true;
            }
         }

         if (!familyexists) {
            log.info("add data fail!");
         } else {
            log.info("add data Success!");
         }

         table.put(put);
         table.close();
      }
   }

   public void addDataForColumn(String rowKey, String tableName, String family, Map<String, String> keyAndData) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         boolean familyexists = false;
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Put put = new Put(Bytes.toBytes(rowKey));
         ColumnFamilyDescriptor[] columnFamilies = table.getDescriptor().getColumnFamilies();

         for(int i = 0; i < columnFamilies.length; ++i) {
            String familyName = columnFamilies[i].getNameAsString();
            log.info("familyName:" + familyName);
            if (familyName.equals(family)) {
               Iterator var11 = keyAndData.entrySet().iterator();

               while(var11.hasNext()) {
                  Map.Entry<String, String> data = (Map.Entry)var11.next();
                  put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes((String)data.getKey()), Bytes.toBytes((String)data.getValue()));
               }

               familyexists = true;
            }
         }

         if (!familyexists) {
            log.info("add data fail!");
         } else {
            log.info("add data Success!");
         }

         table.put(put);
         table.close();
      }
   }

   public ResultScanner getResult(String tableName, String familyName, Scan scan, QualifierFilter qualifierFilter) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         scan.setFilter(qualifierFilter);
         scan.addFamily(Bytes.toBytes(familyName));
         ResultScanner rs = table.getScanner(scan);
         table.close();
         return rs;
      }
   }

   public Result getResult(String tableName, String familyName, String rowKey) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Get get = new Get(Bytes.toBytes(rowKey));
         get.addFamily(Bytes.toBytes(familyName));
         Result result = table.get(get);
         table.close();
         return result;
      }
   }

   public ResultScanner getResultScann(String tableName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Scan scan = new Scan();
         ResultScanner rs = null;
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         rs = table.getScanner(scan);
         table.close();
         return rs;
      }
   }

   public ResultScanner getResultScann(String tableName, String familyName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Scan scan = new Scan();
         scan.addFamily(Bytes.toBytes(familyName));
         ResultScanner rs = null;
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         rs = table.getScanner(scan);
         table.close();
         return rs;
      }
   }

   public Result getResultByColumn(String tableName, String rowKey, String familyName, String columnName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Get get = new Get(Bytes.toBytes(rowKey));
         get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
         Result result = table.get(get);
         table.close();
         return result;
      }
   }

   public Result getResultByColumn(String tableName, String rowKey, String familyName, String[] columnNames) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Get get = new Get(Bytes.toBytes(rowKey));
         String[] var7 = columnNames;
         int var8 = columnNames.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String columnName = var7[var9];
            get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
         }

         Result result = table.get(get);
         table.close();
         return result;
      }
   }

   public void updateTable(String tableName, String rowKey, String familyName, String columnName, String value) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Get get = new Get(Bytes.toBytes(rowKey));
         get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
         RowMutations rms = new RowMutations(Bytes.toBytes(rowKey));
         if (table.exists(get)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName), Bytes.toBytes(value));
            rms.add(put);
            table.mutateRow(rms);
            table.close();
            log.info("update table Success!");
         } else {
            log.info("update table columnName  is  not exit! columnName: " + columnName);
            table.close();
         }
      }
   }

   public void updateTable(String tableName, String rowKey, String familyName, JSONObject jsonObject) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         RowMutations rms = new RowMutations(Bytes.toBytes(rowKey));
         Put put = new Put(Bytes.toBytes(rowKey));
         Iterator var8 = jsonObject.keySet().iterator();

         while(var8.hasNext()) {
            Object key = var8.next();
            if (key instanceof String) {
               String columnName = (String)key;
               Get get = new Get(Bytes.toBytes(rowKey));
               get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
               Object value = jsonObject.get(columnName);
               if (!table.exists(get)) {
                  log.info("update table columnName  is  not exit! columnName: " + columnName);
                  table.close();
                  return;
               }

               if (value instanceof String) {
                  String svalue = (String)value;
                  put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes((String)key), Bytes.toBytes(svalue));
               }
            }
         }

         rms.add(put);
         table.mutateRow(rms);
         table.close();
         log.info("update table Success!");
      }
   }

   public Result getResultByVersion(String tableName, String rowKey, String familyName, String columnName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Get get = new Get(Bytes.toBytes(rowKey));
         get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
         get.readVersions(5);
         Result result = table.get(get);
         table.close();
         return result;
      }
   }

   public void deleteColumn(String tableName, String rowKey, String falilyName, String columnName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         RowMutations rms = new RowMutations(Bytes.toBytes(rowKey));
         Get get = new Get(Bytes.toBytes(rowKey));
         get.addColumn(Bytes.toBytes(falilyName), Bytes.toBytes(columnName));
         if (table.exists(get)) {
            Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
            deleteColumn.addColumn(Bytes.toBytes(falilyName), Bytes.toBytes(columnName));
            rms.add(deleteColumn);
            table.mutateRow(rms);
            table.close();
            log.info(falilyName + ":" + columnName + "is deleted!");
         } else {
            log.info("delete table columnName  is  not exit! columnName: " + columnName);
            table.close();
         }
      }
   }

   public void deleteAllColumn(String tableName, String rowKey) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         RowMutations rms = new RowMutations(Bytes.toBytes(rowKey));
         Get get = new Get(Bytes.toBytes(rowKey));
         if (table.exists(get)) {
            Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
            rms.add(deleteAll);
            table.mutateRow(rms);
            table.close();
            log.info("all columns are deleted!");
         }

      }
   }

   public void clearTable(String tableName, boolean preserveSplits) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         this.admin.truncateTable(TableName.valueOf(tableName), preserveSplits);
      }
   }

   public void deleteTable(String tableName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         this.admin.disableTable(TableName.valueOf(tableName));
         this.admin.deleteTable(TableName.valueOf(tableName));
         log.info(tableName + "is deleted!");
      }
   }

   public Scan getRowRangScan(String keyup, String keydown) {
      Scan scan = new Scan();
      scan.withStartRow(Bytes.toBytes(keyup));
      scan.withStopRow(Bytes.toBytes(keydown));
      return scan;
   }

   public Scan getRowPrefixScan(String rowPrefixFilter) {
      Scan scan = new Scan();
      scan.setRowPrefixFilter(Bytes.toBytes(rowPrefixFilter));
      return scan;
   }

   public Scan getNewScan() {
      return new Scan();
   }

   public Scan addScanFamily(Scan scan, String family) {
      scan.addFamily(Bytes.toBytes(family));
      return scan;
   }

   public Scan addScanColumn(Scan scan, String family, String qualifier) {
      scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
      return scan;
   }

   public FilterList getFilterList() {
      return new FilterList(new Filter[0]);
   }

   public FilterList getFilterList(FilterList.Operator op) {
      return new FilterList(op);
   }

   public KeyOnlyFilter geKeyOnlyFilter() {
      KeyOnlyFilter keyOnlyFilter = new KeyOnlyFilter();
      return keyOnlyFilter;
   }

   public FirstKeyOnlyFilter getFirstKeyOnlyFilter() {
      FirstKeyOnlyFilter filter = new FirstKeyOnlyFilter();
      return filter;
   }

   public QualifierFilter getQualifierFilter(CompareOperator compareOperator, String value) {
      ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes(value));
      QualifierFilter qualifierFilter = new QualifierFilter(compareOperator, rowComparator);
      return qualifierFilter;
   }

   public QualifierFilter getBinaryPrefixFilter(CompareOperator compareOperator, String value) {
      BinaryPrefixComparator rowComparator = new BinaryPrefixComparator(Bytes.toBytes(value));
      QualifierFilter qualifierFilter = new QualifierFilter(compareOperator, rowComparator);
      return qualifierFilter;
   }

   public QualifierFilter getRegexStringFilter(CompareOperator compareOperator, String value) {
      RegexStringComparator rowComparator = new RegexStringComparator(value);
      QualifierFilter qualifierFilter = new QualifierFilter(compareOperator, rowComparator);
      return qualifierFilter;
   }

   public QualifierFilter getSubstringFilter(CompareOperator compareOperator, String value) {
      SubstringComparator rowComparator = new SubstringComparator(value);
      QualifierFilter qualifierFilter = new QualifierFilter(compareOperator, rowComparator);
      return qualifierFilter;
   }

   public PageFilter getPageFilter(long pageSize) {
      PageFilter pageFilter = new PageFilter(pageSize);
      return pageFilter;
   }

   public ColumnPaginationFilter getColumnPaginationFilter(int limit, int offset) {
      ColumnPaginationFilter filter = new ColumnPaginationFilter(limit, offset);
      return filter;
   }

   public PrefixFilter getPrefixFilter(String value) {
      return new PrefixFilter(Bytes.toBytes(value));
   }

   public ValueFilter getValueFilter(CompareOperator compareOperator, String value) {
      ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes(value));
      ValueFilter filter = new ValueFilter(compareOperator, rowComparator);
      return filter;
   }

   public RowFilter getRowFilter(CompareOperator compareOperator, String value) {
      ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes(value));
      RowFilter rowFilter = new RowFilter(compareOperator, rowComparator);
      return rowFilter;
   }

   public DependentColumnFilter getDependentColumnFilter(String family, String qualifier, CompareOperator compareOperator, String value) {
      DependentColumnFilter filter = new DependentColumnFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), false, compareOperator, new BinaryComparator(Bytes.toBytes(value)));
      return filter;
   }

   public DependentColumnFilter getDependentColumnFilter(String family, String qualifier) {
      DependentColumnFilter filter = new DependentColumnFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier));
      return filter;
   }

   public SingleColumnValueFilter getSingleColumnValueFilter(String family, String qualifier, CompareOperator op, String value) {
      ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes(value));
      SingleColumnValueFilter single = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), op, rowComparator);
      return single;
   }

   public ColumnRangeFilter getColumnRangeFilter(String minColumn, boolean minColumnInclusive, String maxColumn, boolean maxColumnInclusive) {
      ColumnRangeFilter columnRangeFilter = new ColumnRangeFilter(Bytes.toBytes(minColumn), minColumnInclusive, Bytes.toBytes(maxColumn), maxColumnInclusive);
      return columnRangeFilter;
   }

   public MultipleColumnPrefixFilter getMultipleColumnPrefixFilter(List<String> qualifiers) {
      List<byte[]> qb = (List)qualifiers.stream().map((x) -> {
         return Bytes.toBytes("x");
      }).collect(Collectors.toList());
      byte[][] prefixes = (byte[][])qb.toArray(new byte[0][0]);
      MultipleColumnPrefixFilter columnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
      return columnPrefixFilter;
   }

   public ColumnPrefixFilter getColumnPrefixFilter(String qualifier) {
      ColumnPrefixFilter columnPrefixFilter = new ColumnPrefixFilter(Bytes.toBytes(qualifier));
      return columnPrefixFilter;
   }

   public TimestampsFilter getTimestampsFilter(List<Long> listTimestamps) {
      TimestampsFilter timestampsFilter = new TimestampsFilter(listTimestamps);
      return timestampsFilter;
   }

   public InclusiveStopFilter getInclusiveStopFilter(String stopRowKey) {
      InclusiveStopFilter filter = new InclusiveStopFilter(Bytes.toBytes(stopRowKey));
      return filter;
   }

   public WhileMatchFilter getWhileMatchFilter(Filter filter) {
      WhileMatchFilter whileMatchFilter = new WhileMatchFilter(filter);
      return whileMatchFilter;
   }

   public SkipFilter getSkipFilter(CompareOperator compareOperator, String value) {
      return new SkipFilter(this.getValueFilter(compareOperator, value));
   }

   public SingleColumnValueExcludeFilter getSingColumnValueExcludeFilter(String family, String qualifier, CompareOperator op, String value) {
      ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes(value));
      SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), op, rowComparator);
      return filter;
   }

   public ResultScanner getResultByScanPrefixFilter(String tableName, String familyName, Scan rowRange) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         rowRange.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(rowRange);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResult(String tableName, Scan rowRange) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         ResultScanner scanner = table.getScanner(rowRange);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResult(String tableName, String familyName, Scan rowRange) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         rowRange.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(rowRange);
         table.close();
         return scanner;
      }
   }

   public Long countTableAndFamilyNum(String tableName, String familyName) throws IOException {
      Scan newScan = this.getNewScan();
      FirstKeyOnlyFilter firstKeyOnlyFilter = this.getFirstKeyOnlyFilter();
      newScan.setFilter(firstKeyOnlyFilter);
      ResultScanner scanner = this.getResult(tableName, familyName, newScan);
      return this.getQueryResultCount(scanner);
   }

   public Long countTableAndFamilyNum(String tableName, String familyName, String rowKeyPrefix) throws IOException {
      Scan newScan = this.getNewScan();
      FilterList filterList = this.getFilterList();
      PrefixFilter prefixFilter = this.getPrefixFilter(rowKeyPrefix);
      filterList.addFilter(prefixFilter);
      FirstKeyOnlyFilter firstKeyOnlyFilter = this.getFirstKeyOnlyFilter();
      filterList.addFilter(firstKeyOnlyFilter);
      newScan.setFilter(filterList);
      ResultScanner scanner = this.getResult(tableName, familyName, newScan);
      return this.getQueryResultCount(scanner);
   }

   public Long countTableAndFamilyNum(String tableName, String familyName, String startKey, String endKey) throws IOException {
      Scan newScan = this.getRowRangScan(startKey, endKey);
      FirstKeyOnlyFilter firstKeyOnlyFilter = this.getFirstKeyOnlyFilter();
      newScan.setFilter(firstKeyOnlyFilter);
      ResultScanner scanner = this.getResult(tableName, familyName, newScan);
      return this.getQueryResultCount(scanner);
   }

   public void deleteRow(String tableName, String familyName, String RowKey) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Delete delete = new Delete(Bytes.toBytes(RowKey));
         delete.addFamily(Bytes.toBytes(familyName));
         table.delete(delete);
         table.close();
      }
   }

   public ResultScanner getResultAllData(String tableName, String familyName) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Scan scan = new Scan();
         scan.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(scan);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResultByScanPrefixFilter(String tableName, String familyName, Scan rowRange, Filter filter) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         rowRange.addFamily(Bytes.toBytes(familyName));
         rowRange.setFilter(filter);
         ResultScanner scanner = table.getScanner(rowRange);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResultByRowFilter(String tableName, String familyName, String rowKeyValue, Scan rangeValue, CompareOperator Filter) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes(rowKeyValue));
         RowFilter rf = new RowFilter(Filter, rowComparator);
         rangeValue.setFilter(rf);
         rangeValue.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(rangeValue);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResultByRegularExpression(String tableName, String familyName, Scan rangeValue, String RegularExpression) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         RegexStringComparator RegexString = new RegexStringComparator(RegularExpression);
         RowFilter rf = new RowFilter(CompareOperator.EQUAL, RegexString);
         rangeValue.setFilter(rf);
         rangeValue.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(rangeValue);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResultBySingleColumn(String tableName, String familyName, String Qualifier, String value) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Filter scvf = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(Qualifier), CompareOperator.EQUAL, Bytes.toBytes(value));
         Scan singleColumnScan = new Scan();
         singleColumnScan.setFilter(scvf);
         singleColumnScan.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(singleColumnScan);
         table.close();
         return scanner;
      }
   }

   public SingleColumnValueFilter setSingleColumnValueFilter(String familyName, String Qualifier, String value, CompareOperator Filter) {
      return new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(Qualifier), Filter, Bytes.toBytes(value));
   }

   public ResultScanner getResultByManyFilter(String tableName, String familyName, FilterList filterList) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         Scan singleColumnScan = new Scan();
         singleColumnScan.setFilter(filterList);
         singleColumnScan.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(singleColumnScan);
         table.close();
         return scanner;
      }
   }

   public ResultScanner getResultByManyFilter(String tableName, String familyName, FilterList filterList, Scan rangescan) throws IOException {
      if (!this.admin.tableExists(TableName.valueOf(tableName))) {
         log.info("table is not Exists!");
         return null;
      } else if (null != filterList && null != rangescan) {
         Table table = this.connection.getTable(TableName.valueOf(tableName));
         rangescan.setFilter(filterList);
         rangescan.addFamily(Bytes.toBytes(familyName));
         ResultScanner scanner = table.getScanner(rangescan);
         table.close();
         return scanner;
      } else {
         log.error("filterList or rangescan is null");
         return null;
      }
   }

   private String getRowKey(Cell cell) {
      return Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
   }

   private String getQualifier(Cell cell) {
      return Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
   }

   private byte[] getQualifierValue(Cell cell) {
      return ArrayUtil.sub(cell.getValueArray(), cell.getValueOffset(), cell.getValueOffset() + cell.getValueLength());
   }

   public HbaseDataStore getQueryResult(Result result, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Cell[] var4 = result.rawCells();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Cell cell = var4[var6];
         hbaseDataStore.add(this.getRowKey(cell), this.getQualifier(cell), this.getQualifierValue(cell));
      }

      return hbaseDataStore;
   }

   public HbaseDataStore getStringQueryResult(Result result, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Cell[] var4 = result.rawCells();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Cell cell = var4[var6];
         hbaseDataStore.add(this.getRowKey(cell), this.getQualifier(cell), Bytes.toString(this.getQualifierValue(cell)));
      }

      return hbaseDataStore;
   }

   public HbaseDataStore getQueryResult(ResultScanner resultScanner, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Iterator var4 = resultScanner.iterator();

      while(var4.hasNext()) {
         Result result = (Result)var4.next();
         Cell[] cells = result.rawCells();
         Cell[] var7 = cells;
         int var8 = cells.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Cell cell = var7[var9];
            hbaseDataStore.add(this.getRowKey(cell), this.getQualifier(cell), this.getQualifierValue(cell));
         }
      }

      return hbaseDataStore;
   }

   public Long getQueryResultCount(ResultScanner resultScanner) {
      new HbaseDataStore(false);
      Long rowNum = 0L;

      for(Iterator var4 = resultScanner.iterator(); var4.hasNext(); rowNum = rowNum + 1L) {
         Result result = (Result)var4.next();
      }

      return rowNum;
   }

   public HbaseDataStore getQueryResult(ResultScanner resultScanner, Integer page, Integer size, boolean ordered) throws IOException {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      int startRow = (page - 1) * size;
      int endRow = startRow + size;
      int rowNum = 0;
      Iterator var9 = resultScanner.iterator();

      while(var9.hasNext()) {
         Result result = (Result)var9.next();
         ++rowNum;
         if (rowNum > startRow && rowNum <= endRow) {
            Cell[] cells = result.rawCells();
            Cell[] var12 = cells;
            int var13 = cells.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               Cell cell = var12[var14];
               hbaseDataStore.add(this.getRowKey(cell), this.getQualifier(cell), this.getQualifierValue(cell));
            }
         }

         if (rowNum > endRow) {
            break;
         }
      }

      return hbaseDataStore;
   }

   public HbaseDataStore getQueryStringResult(ResultScanner resultScanner, Integer page, Integer size, boolean ordered) throws IOException {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      int startRow = (page - 1) * size;
      int endRow = startRow + size;
      int rowNum = 0;
      Iterator var9 = resultScanner.iterator();

      while(var9.hasNext()) {
         Result result = (Result)var9.next();
         ++rowNum;
         if (rowNum > startRow && rowNum <= endRow) {
            Cell[] cells = result.rawCells();
            Cell[] var12 = cells;
            int var13 = cells.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               Cell cell = var12[var14];
               hbaseDataStore.add(this.getRowKey(cell), this.getQualifier(cell), Bytes.toString(this.getQualifierValue(cell)));
            }
         }

         if (rowNum > endRow) {
            break;
         }
      }

      return hbaseDataStore;
   }

   public <T> void getQueryStream(Map<String, Object> contentMap, ResultScanner resultScanner, DataConsumer<T> consumer, Class<T> clazz) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(false);
      Iterator var6 = resultScanner.iterator();

      while(var6.hasNext()) {
         Result result = (Result)var6.next();
         Cell[] cells = result.rawCells();
         String rowKey = "";
         Cell[] var10 = cells;
         int var11 = cells.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            Cell cell = var10[var12];
            rowKey = this.getRowKey(cell);
            hbaseDataStore.add(rowKey, this.getQualifier(cell), this.getQualifierValue(cell));
         }

         consumer.accept(contentMap, rowKey, hbaseDataStore.get(rowKey, clazz));
         hbaseDataStore.removeAll();
      }

   }

   public <T> void getQueryJSONObjectStream(Map<String, Object> contentMap, ResultScanner resultScanner, DataConsumer<JSONObject> consumer) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(false);
      Iterator var5 = resultScanner.iterator();

      while(var5.hasNext()) {
         Result result = (Result)var5.next();
         Cell[] cells = result.rawCells();
         String rowKey = "";
         Cell[] var9 = cells;
         int var10 = cells.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Cell cell = var9[var11];
            rowKey = this.getRowKey(cell);
            hbaseDataStore.add(rowKey, this.getQualifier(cell), Bytes.toString(this.getQualifierValue(cell)));
         }

         consumer.accept(contentMap, rowKey, hbaseDataStore.get(rowKey, JSONObject.class));
         hbaseDataStore.removeAll();
      }

   }

   public HbaseDataStore getStringQueryResult(Result result, FiledFun filedFun, boolean ordered) {
      if (ObjectUtil.isNull(filedFun)) {
         throw new RuntimeException("filedFun is null");
      } else {
         HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
         Cell[] var5 = result.rawCells();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Cell cell = var5[var7];
            String rowKey = this.getRowKey(cell);
            String qualifier = this.getQualifier(cell);
            String value = Bytes.toString(this.getQualifierValue(cell));
            if (filedFun.doWith(rowKey, qualifier, value)) {
               hbaseDataStore.add(rowKey, qualifier, value);
            }
         }

         return hbaseDataStore;
      }
   }

   public HbaseDataStore getStringQueryResult(ResultScanner resultScanner, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Iterator var4 = resultScanner.iterator();

      while(var4.hasNext()) {
         Result result = (Result)var4.next();
         Cell[] cells = result.rawCells();
         Cell[] var7 = cells;
         int var8 = cells.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Cell cell = var7[var9];
            hbaseDataStore.add(this.getRowKey(cell), this.getQualifier(cell), Bytes.toString(this.getQualifierValue(cell)));
         }
      }

      return hbaseDataStore;
   }

   public HbaseDataStore getStringQueryResult(ResultScanner resultScanner, FiledFun filedFun, boolean ordered) {
      HbaseDataStore hbaseDataStore = new HbaseDataStore(ordered);
      Iterator var5 = resultScanner.iterator();

      while(var5.hasNext()) {
         Result result = (Result)var5.next();
         Cell[] cells = result.rawCells();
         Cell[] var8 = cells;
         int var9 = cells.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Cell cell = var8[var10];
            String rowKey = this.getRowKey(cell);
            String qualifier = this.getQualifier(cell);
            String value = Bytes.toString(this.getQualifierValue(cell));
            if (filedFun.doWith(rowKey, qualifier, value)) {
               hbaseDataStore.add(rowKey, qualifier, value);
            }
         }
      }

      return hbaseDataStore;
   }
}
