package com.chang.util.source.common;

import cn.hutool.db.Db;
import cn.hutool.db.Session;
import cn.hutool.db.dialect.impl.Sqlite3Dialect;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;

import java.sql.SQLException;

public class SqliteDataBaseApi {
   private final Db db;
   private final DruidDataSource source;

   public SqliteDataBaseApi(String url) throws SQLException {
      Sqlite3Dialect sqlite3Dialect = new Sqlite3Dialect();
      this.source = new DruidDataSource();
      this.source.setUrl(url);
      this.source.setDbType(DbType.sqlite);
      this.source.setDriverClassName("org.sqlite.JDBC");
      this.source.setInitialSize(5);
      this.source.setMinIdle(5);
      this.source.setMaxActive(5);
      this.source.setTimeBetweenEvictionRunsMillis(90000L);
      this.source.setMinEvictableIdleTimeMillis(1800000L);
      this.source.setValidationQuery("SELECT 'x'");
      this.source.setTestWhileIdle(true);
      this.source.setTestOnBorrow(false);
      this.source.setTestOnReturn(false);
      this.source.setPoolPreparedStatements(true);
      this.source.setMaxOpenPreparedStatements(-1);
      this.source.setFilters("stat,log4j");
      this.source.setMaxPoolPreparedStatementPerConnectionSize(-1);
      this.source.setAsyncInit(true);
      this.source.setLogAbandoned(true);
      this.source.setRemoveAbandoned(true);
      this.source.setRemoveAbandonedTimeout(180);
      this.db = Db.use(this.source, sqlite3Dialect);
   }

   public Db getDb() {
      return this.db;
   }

   public Session getSession() {
      return Session.create(this.source);
   }
}
