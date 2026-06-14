package com.chang.binlog.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.dialect.impl.MysqlDialect;
import com.alibaba.druid.pool.DruidDataSource;
import com.chang.binlog.config.SyncConfig;
import com.chang.binlog.event.EventEntity;
import com.chang.binlog.event.EventEntityType;
import com.chang.binlog.tablemeta.TableMetaEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlUtil {
   private static final Logger log = LoggerFactory.getLogger(SqlUtil.class);
   private static final String INSERT = "INSERT INTO `%s` (%s) VALUES (%s)";
   private static final String UPDATE = "UPDATE `%s` SET %s WHERE id = %s";
   private static final String DELETE = "DELETE FROM `%s` WHERE id = %s";

   public static String event2Sql(EventEntity event, SyncConfig syncConfig) {
      EventEntityType eventEntityType = event.getEventEntityType();
      String tableName = event.getTableName();
      tableName = tableName.replaceAll("`", "");
      List<TableMetaEntity.ColumnMetaData> columns = event.getColumns();
      List<Object> changeBefore = event.getChangeBefore();
      List<Object> changeAfter = event.getChangeAfter();
      Stream var10000 = columns.stream().filter((x) -> {
         return x.getName().equals("id");
      });
      columns.getClass();
      int index = (Integer)var10000.map(columns::indexOf).findFirst().get();
      String idValue = String.valueOf(changeAfter.get(index));
      List<String> noNullColumns = new ArrayList();
      List<String> noNullValue = new ArrayList();
      if (EventEntityType.INSERT.equals(eventEntityType)) {
         String sql;
         for(int i = 0; i < columns.size(); ++i) {
            if (StringUtils.isNotBlank((String)changeAfter.get(i))) {
               noNullColumns.add(((TableMetaEntity.ColumnMetaData)columns.get(i)).getName());
               if (!((TableMetaEntity.ColumnMetaData)columns.get(i)).getType().equals("VARCHAR") && !((TableMetaEntity.ColumnMetaData)columns.get(i)).getType().equals("TEXT")) {
                  noNullValue.add(String.valueOf(changeAfter.get(i)));
               } else if (syncConfig.getSyncDbType().equals(DbType.MYSQL)) {
                  noNullValue.add("'" + ((String)changeAfter.get(i)).replace("\"", "\\\"") + "'");
               } else {
                  if (!syncConfig.getSyncDbType().equals(DbType.DM)) {
                     throw new RuntimeException("暂不支持的数据库类型");
                  }

                  sql = (String)changeAfter.get(i);
                  log.info("INSERT DM Sql: {}", sql);
                  noNullValue.add("'" + sql + "'");
               }
            }
         }

         if (noNullColumns.size() != noNullValue.size()) {
            throw new RuntimeException("insert column size not equal value size");
         } else {
            String fields = String.join(",", noNullColumns);
            sql = (String)noNullValue.stream().map(String::valueOf).collect(Collectors.joining(","));
            return String.format("INSERT INTO `%s` (%s) VALUES (%s)", tableName, fields, sql);
         }
      } else if (!EventEntityType.UPDATE.equals(eventEntityType)) {
         if (EventEntityType.DELETE.equals(eventEntityType)) {
            return String.format("DELETE FROM `%s` WHERE id = %s", tableName, idValue);
         } else {
            throw new RuntimeException("不支持的事件类型");
         }
      } else if (columns.size() != changeAfter.size()) {
         throw new RuntimeException("update column size not equal value size");
      } else {
         List<String> setList = new ArrayList();

         for(int i = 0; i < columns.size(); ++i) {
            TableMetaEntity.ColumnMetaData columnMetaData = (TableMetaEntity.ColumnMetaData)columns.get(i);
            if (!columnMetaData.getName().equals("id")) {
               Object valueBefore = changeBefore.get(i);
               Object valueAfter = changeAfter.get(i);
               if (!ObjectUtil.equals(valueAfter, valueBefore)) {
                  if (!((TableMetaEntity.ColumnMetaData)columns.get(i)).getType().equals("VARCHAR") && !((TableMetaEntity.ColumnMetaData)columns.get(i)).getType().equals("TEXT")) {
                     if (StringUtils.isBlank((String)valueAfter)) {
                        setList.add(((TableMetaEntity.ColumnMetaData)columns.get(i)).getName() + "=null");
                     } else {
                        setList.add(((TableMetaEntity.ColumnMetaData)columns.get(i)).getName() + "=" + valueAfter);
                     }
                  } else {
                     String date;
                     if (ObjectUtil.isNull(valueAfter)) {
                        date = "";
                     } else {
                        date = String.valueOf(valueAfter);
                     }

                     if (syncConfig.getSyncDbType().equals(DbType.MYSQL)) {
                        setList.add(((TableMetaEntity.ColumnMetaData)columns.get(i)).getName() + "='" + date.replace("\"", "\\\"") + "'");
                     } else {
                        if (!syncConfig.getSyncDbType().equals(DbType.DM)) {
                           throw new RuntimeException("暂不支持的数据库类型");
                        }

                        log.info("UPDATE DM Sql: {}", date);
                        setList.add(((TableMetaEntity.ColumnMetaData)columns.get(i)).getName() + "='" + date + "'");
                     }
                  }
               }
            }
         }

         return String.format("UPDATE `%s` SET %s WHERE id = %s", tableName, setList.stream().collect(Collectors.joining(",")), idValue);
      }
   }

   public static DruidDataSource getMysqlSource(String url, String username, String password) throws SQLException {
      DruidDataSource dataSource = new DruidDataSource();
      dataSource.setUrl(url);
      dataSource.setUsername(username);
      dataSource.setPassword(password);
      dataSource.setDbType(com.alibaba.druid.DbType.mysql);
      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      dataSource.setInitialSize(8);
      dataSource.setMinIdle(8);
      dataSource.setMaxActive(8);
      dataSource.setTimeBetweenEvictionRunsMillis(90000L);
      dataSource.setMinEvictableIdleTimeMillis(1800000L);
      dataSource.setValidationQuery("SELECT 1 FROM DUAL");
      dataSource.setTestWhileIdle(true);
      dataSource.setTestOnBorrow(false);
      dataSource.setTestOnReturn(false);
      dataSource.setPoolPreparedStatements(false);
      dataSource.setAsyncInit(true);
      dataSource.setLogAbandoned(true);
      dataSource.setRemoveAbandoned(true);
      dataSource.setRemoveAbandonedTimeout(180);
      dataSource.setMaxOpenPreparedStatements(-1);
      dataSource.setFilters("stat,wall,log4j");
      return dataSource;
   }

   public static void exportSqlForDm(DataSource mysqlSource, String tableName, String sqlTypeField, String tableNameField, String sqlField) throws SQLException {
      MysqlDialect mysqlDialect = new MysqlDialect();
      Db db = Db.use(mysqlSource, mysqlDialect);
      Connection connection = mysqlSource.getConnection();
      Statement statement = connection.createStatement();

      try {
         List<Entity> secures = db.query("show global variables like '%secure%'", new Object[0]);
         Entity entity = (Entity)secures.stream().filter((x) -> {
            return x.getStr("Variable_name").equals("secure_file_priv");
         }).findFirst().get();
         String exPath = entity.getStr("Value");
         String tableNameSql = String.format("SELECT DISTINCT %s FROM %s WHERE %s = 'insert'", tableNameField, tableName, sqlTypeField);
         String exSql = "SELECT t.%s INTO OUTFILE '%s' FROM (SELECT 'SET IDENTITY_INSERT %s on;' AS %s FROM DUAL UNION ALL SELECT %s FROM %s  WHERE %s = '%s' AND %s = '%s' UNION ALL SELECT 'SET IDENTITY_INSERT %s off;' AS %s FROM DUAL) t";
         List<Entity> query = db.query(tableNameSql, new Object[0]);
         Iterator var15 = query.iterator();

         while(var15.hasNext()) {
            Entity e = (Entity)var15.next();
            String name = e.getStr(tableNameField);
            String fullPath = exPath + name + ".sql";
            String formatExSql = String.format(exSql, sqlField, fullPath, name, sqlField, sqlField, tableName, tableNameField, name, sqlTypeField, "insert", name, sqlField);
            statement.execute(formatExSql);
         }

         String otherSql = "SELECT %s INTO OUTFILE '%s' FROM %s WHERE %s != '%s'";
         String formatOtherSql = String.format(otherSql, sqlField, exPath + "other.sql", tableName, sqlTypeField, "insert");
         statement.execute(formatOtherSql);
      } catch (Exception var23) {
         throw new RuntimeException(var23);
      } finally {
         statement.close();
         connection.close();
      }
   }
}
