package com.chang.util.source.common;

import cn.hutool.db.Db;
import cn.hutool.db.Session;
import cn.hutool.db.dialect.impl.MysqlDialect;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBaseApi {
   private static final Logger log = LoggerFactory.getLogger(DataBaseApi.class);
   private String url;
   private String username;
   private String password;
   private final Db db;
   private final DruidDataSource dataSource;

   public DataBaseApi(String url, String username, String password) throws SQLException {
      this.url = url;
      this.username = username;
      this.password = password;
      this.dataSource = new DruidDataSource();
      this.dataSource.setUrl(url);
      this.dataSource.setUsername(username);
      this.dataSource.setPassword(password);
      this.dataSource.setDbType(DbType.mysql);
      this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      this.dataSource.setInitialSize(5);
      this.dataSource.setMinIdle(5);
      this.dataSource.setMaxActive(5);
      this.dataSource.setTimeBetweenEvictionRunsMillis(90000L);
      this.dataSource.setMinEvictableIdleTimeMillis(1800000L);
      this.dataSource.setValidationQuery("SELECT 1 FROM DUAL");
      this.dataSource.setTestWhileIdle(true);
      this.dataSource.setTestOnBorrow(false);
      this.dataSource.setTestOnReturn(false);
      this.dataSource.setPoolPreparedStatements(false);
      this.dataSource.setAsyncInit(true);
      this.dataSource.setLogAbandoned(true);
      this.dataSource.setRemoveAbandoned(true);
      this.dataSource.setRemoveAbandonedTimeout(180);
      this.dataSource.setMaxOpenPreparedStatements(-1);
      this.dataSource.setFilters("stat,wall,log4j");
      MysqlDialect mysqlDialect = new MysqlDialect();
      this.db = Db.use(this.dataSource, mysqlDialect);
   }

   public Db getDb() {
      return this.db;
   }

   public Session getSession() {
      return Session.create(this.dataSource);
   }

   public void close() throws SQLException {
      this.dataSource.close();
   }
}
