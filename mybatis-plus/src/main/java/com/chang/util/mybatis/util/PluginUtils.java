package com.chang.util.mybatis.util;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

public abstract class PluginUtils {
   public static final String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";

   public static <T> T realTarget(Object target) {
      if (Proxy.isProxyClass(target.getClass())) {
         MetaObject metaObject = SystemMetaObject.forObject(target);
         return realTarget(metaObject.getValue("h.target"));
      } else {
         return (T) target;
      }
   }

   public static void setAdditionalParameter(BoundSql boundSql, Map<String, Object> additionalParameters) {
      additionalParameters.forEach(boundSql::setAdditionalParameter);
   }

   public static MPBoundSql mpBoundSql(BoundSql boundSql) {
      return new MPBoundSql(boundSql);
   }

   public static MPStatementHandler mpStatementHandler(StatementHandler statementHandler) {
      statementHandler = (StatementHandler)realTarget(statementHandler);
      MetaObject object = SystemMetaObject.forObject(statementHandler);
      return new MPStatementHandler(SystemMetaObject.forObject(object.getValue("delegate")));
   }

   public static class MPBoundSql {
      private final MetaObject boundSql;
      private final BoundSql delegate;

      MPBoundSql(BoundSql boundSql) {
         this.delegate = boundSql;
         this.boundSql = SystemMetaObject.forObject(boundSql);
      }

      public String sql() {
         return this.delegate.getSql();
      }

      public void sql(String sql) {
         this.boundSql.setValue("sql", sql);
      }

      public List<ParameterMapping> parameterMappings() {
         List<ParameterMapping> parameterMappings = this.delegate.getParameterMappings();
         return new ArrayList(parameterMappings);
      }

      public void parameterMappings(List<ParameterMapping> parameterMappings) {
         this.boundSql.setValue("parameterMappings", Collections.unmodifiableList(parameterMappings));
      }

      public Object parameterObject() {
         return this.get("parameterObject");
      }

      public Map<String, Object> additionalParameters() {
         return (Map)this.get("additionalParameters");
      }

      private <T> T get(String property) {
         return (T) this.boundSql.getValue(property);
      }
   }

   public static class MPStatementHandler {
      private final MetaObject statementHandler;

      MPStatementHandler(MetaObject statementHandler) {
         this.statementHandler = statementHandler;
      }

      public ParameterHandler parameterHandler() {
         return (ParameterHandler)this.get("parameterHandler");
      }

      public MappedStatement mappedStatement() {
         return (MappedStatement)this.get("mappedStatement");
      }

      public Executor executor() {
         return (Executor)this.get("executor");
      }

      public MPBoundSql mPBoundSql() {
         return new MPBoundSql(this.boundSql());
      }

      public BoundSql boundSql() {
         return (BoundSql)this.get("boundSql");
      }

      public Configuration configuration() {
         return (Configuration)this.get("configuration");
      }

      private <T> T get(String property) {
         return (T) this.statementHandler.getValue(property);
      }
   }
}
