package com.chang.util.source.mysql;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.chang.util.source.common.DataBaseApi;
import com.chang.util.source.manage.ManageMySqlSource;
import com.chang.util.source.OutputSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(MySqlOutput.class);
   private DataBaseApi dataBaseApi;
   private String tableName;
   private final List<String> filed = new ArrayList();
   private String keyName;
   private Map<String, Object> parm;

   public MySqlOutput(DataBaseApi dataBaseApi, String tableName, String keyName, Map<String, Object> parm) {
      this.dataBaseApi = dataBaseApi;
      this.tableName = tableName;
      this.keyName = keyName;
      this.parm = parm;
   }

   public MySqlOutput(String sourceName, String tableName, String keyName, Map<String, Object> parm) {
      this.dataBaseApi = ManageMySqlSource.getDbSource(sourceName);
      this.tableName = tableName;
      this.keyName = keyName;
      this.parm = parm;
   }

   public void addExcludeFiled(String filedName) {
      this.filed.add(filedName);
   }

   public void Output(Object o) {
      if (BeanUtil.isBean(o.getClass())) {
         this.SendData(this.tableName, BeanUtil.beanToMap(o, new String[0]));
      } else {
         if (!(o instanceof List)) {
            throw new RuntimeException("data is not bean or list");
         }

         this.SendDataAll(this.tableName, (List)((List)o).stream().map((x$0) -> {
            return BeanUtil.beanToMap(x$0, new String[0]);
         }).collect(Collectors.toList()));
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   private void SendData(String tableName, Map<String, Object> map) {
      Iterator var3 = this.filed.iterator();

      while(var3.hasNext()) {
         String f = (String)var3.next();
         map.remove(f);
      }

      Entity record = new Entity(tableName);
      Iterator var8 = map.entrySet().iterator();

      while(var8.hasNext()) {
         Map.Entry<String, Object> entry = (Map.Entry)var8.next();
         record.set((String)entry.getKey(), entry.getValue());
      }

      Db db = this.dataBaseApi.getDb();

      try {
         db.insert(record);
      } catch (SQLException var6) {
         log.error("insert err", var6);
      }

   }

   private void SendDataAll(String tableName, List<Map<String, Object>> mapLists) {
      List<Map<String, Object>> collectMap = (List)mapLists.stream().map((x) -> {
         Iterator var2 = this.filed.iterator();

         while(var2.hasNext()) {
            String f = (String)var2.next();
            x.remove(f);
         }

         return x;
      }).collect(Collectors.toList());
      List<Entity> entities = new ArrayList();
      Iterator var5 = collectMap.iterator();

      while(var5.hasNext()) {
         Map<String, Object> map = (Map)var5.next();
         Entity record = new Entity(tableName);
         Iterator var8 = map.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var8.next();
            record.set((String)entry.getKey(), entry.getValue());
         }

         entities.add(record);
      }

      Db db = this.dataBaseApi.getDb();

      try {
         db.insert(entities);
      } catch (SQLException var10) {
         log.error("insert err", var10);
      }

   }

   public void close() {
      if (ObjectUtil.isNotNull(this.dataBaseApi)) {
         try {
            this.dataBaseApi.close();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public DataBaseApi getDataBaseApi() {
      return this.dataBaseApi;
   }

   public String getTableName() {
      return this.tableName;
   }

   public List<String> getFiled() {
      return this.filed;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setDataBaseApi(DataBaseApi dataBaseApi) {
      this.dataBaseApi = dataBaseApi;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }
}
