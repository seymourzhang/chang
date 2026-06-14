package com.chang.util.source.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.chang.until.timeTaskApi.TimeTaskManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ManageCache {
   private static final Logger log = LoggerFactory.getLogger(ManageCache.class);
   private static volatile Map<String, Long> countMap = new ConcurrentHashMap();

   public static void creatCache(String cacheName) throws SQLException {
      Connection connection = null;
      Statement statement = null;

      try {
         SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
         Db db = dbSource.getDb();
         connection = db.getConnection();
         connection.setAutoCommit(true);
         statement = connection.createStatement();
         statement.execute("create table if not exists data_db (id  integer, date image, topic text,timestamp bigint, primary key (id))");
         statement.execute("create unique index if not exists index_time ON data_db (timestamp)");
      } finally {
         if (ObjectUtil.isNotNull(statement)) {
            statement.close();
         }

         if (ObjectUtil.isNotNull(connection)) {
            connection.close();
         }

      }

   }

   public static void cache(String cacheName, String messageType, String topic, Object o) throws SQLException {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      DataDb dataDb = new DataDb();
      dataDb.setTimestamp(System.currentTimeMillis());
      dataDb.setDate(Util.getSendData(messageType, o));
      dataDb.setTopic(topic);
      db.insert(Util.toEntity(dataDb, "data_db"));
   }

   public static void cacheBach(String cacheName, String messageType, List<MqttQueueData> os) throws Exception {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      List<Entity> entityList = new ArrayList();
      Iterator var6 = os.iterator();

      while(var6.hasNext()) {
         MqttQueueData o = (MqttQueueData)var6.next();
         DataDb dataDb = new DataDb();
         dataDb.setTimestamp(System.currentTimeMillis());
         dataDb.setDate(Util.getSendData(messageType, o.getObject()));
         dataDb.setTopic(o.getTopic());
         Entity entity = Util.toEntity(dataDb, "data_db");
         entityList.add(entity);
         Thread.sleep(2L);
      }

      db.insert(entityList);
   }

   public static List<DataDb> readCache(String cacheName, Long size) throws SQLException {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      List<Entity> query = db.query(String.format("SELECT * FROM data_db ORDER BY timestamp DESC LIMIT %s", size), new Object[0]);
      return (List)query.stream().map((x) -> {
         Long id = x.getLong("id");
         String topic = x.getStr("topic", Charset.defaultCharset());
         Long timestamp = x.getLong("timestamp");
         byte[] date = x.getBytes("date");
         DataDb dataDb = new DataDb();
         dataDb.setId(id);
         dataDb.setTimestamp(timestamp);
         dataDb.setDate(date);
         dataDb.setTopic(topic);
         return dataDb;
      }).collect(Collectors.toList());
   }

   public static void delCache(List<Long> ids, String cacheName) throws SQLException {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      Entity where = Entity.create("data_db").set("id", ids);
      db.del(where);
   }

   public static void delCache(Long id, String cacheName) throws SQLException {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      db.del("data_db", "id", id);
   }

   public static boolean hasCache(String cacheName) throws SQLException {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      return db.count(new Entity("data_db")) > 0L;
   }

   public static long getCacheCount(String cacheName) throws SQLException {
      SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
      Db db = dbSource.getDb();
      return db.count(new Entity("data_db"));
   }

   public static void countCache(final String cacheName) {
      Runnable runnable = new Runnable() {
         public void run() {
            try {
               SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
               Db db = dbSource.getDb();
               long count = db.count(new Entity("data_db"));
               ManageCache.countMap.put(cacheName, count);
            } catch (SQLException var5) {
               ManageCache.log.error("CacheClear", var5);
            }

         }
      };
      TimeTaskManage.scheduleAtFixedRate(cacheName, runnable, 1L, TimeUnit.MINUTES);
   }

   public static void clearExpiredData(String cacheName) {
      try {
         SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
         Db db = dbSource.getDb();
         List<Entity> query = db.query(String.format("SELECT id FROM data_db ORDER BY timestamp ASC LIMIT 100"), new Object[0]);
         List<Long> ids = (List)query.stream().map((x) -> {
            return x.getLong("id");
         }).collect(Collectors.toList());
         Entity where = Entity.create("data_db").set("id", ids);
         db.del(where);
         long count = db.count(new Entity("data_db"));
         log.info("clearExpiredData count: " + count);
         countMap.put(cacheName, count);
      } catch (SQLException var8) {
         log.error("clearExpiredData", var8);
      }

   }

   public static Long getCount(String cacheName) {
      return !countMap.containsKey(cacheName) ? 0L : (Long)countMap.get(cacheName);
   }

   public static void upCacheCount(String cacheName) {
      try {
         SqliteDataBaseApi dbSource = ManageSqliteSource.getDbSource(cacheName);
         Db db = dbSource.getDb();
         long count = db.count(new Entity("data_db"));
         countMap.put(cacheName, count);
      } catch (SQLException var5) {
         throw new RuntimeException(var5);
      }
   }
}
