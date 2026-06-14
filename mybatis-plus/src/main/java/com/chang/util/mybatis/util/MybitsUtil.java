package com.chang.util.mybatis.util;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chang.common.exception.StringUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MybitsUtil {
   public static List<String> getAllAlias(String sql) {
      MySqlStatementParser parser = new MySqlStatementParser(sql);
      SQLStatement sqlStatement = parser.parseStatement();
      if (!(sqlStatement instanceof SQLSelectStatement)) {
         return ListUtil.empty();
      } else {
         SQLSelectStatement sqlSelectStatement = (SQLSelectStatement)SQLSelectStatement.class.cast(sqlStatement);
         SQLSelect select = sqlSelectStatement.getSelect();
         SQLSelectQuery query = select.getQuery();
         if (!(query instanceof MySqlSelectQueryBlock)) {
            return ListUtil.empty();
         } else {
            MySqlSelectQueryBlock mySqlSelectQueryBlock = (MySqlSelectQueryBlock)MySqlSelectQueryBlock.class.cast(query);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            SQLTableSource from = mySqlSelectQueryBlock.getFrom();
            from.accept(visitor);
            List<SQLName> originalTables = visitor.getOriginalTables();
            List<String> allAlias = (List)originalTables.stream().map((table) -> {
               SQLObject parent = table.getParent();
               if (!(parent instanceof SQLExprTableSource)) {
                  return "";
               } else {
                  SQLExprTableSource sqlExprTableSource = (SQLExprTableSource)SQLExprTableSource.class.cast(table.getParent());
                  return sqlExprTableSource.getAlias();
               }
            }).filter((name) -> {
               return StringUtil.isNotBlank(name);
            }).collect(Collectors.toList());
            return allAlias;
         }
      }
   }

   public static List<String> getAllTableName(String sql) {
      MySqlStatementParser parser = new MySqlStatementParser(sql);
      SQLSelectStatement sqlStatement = (SQLSelectStatement)parser.parseStatement();
      SQLSelect select = sqlStatement.getSelect();
      MySqlSelectQueryBlock query = (MySqlSelectQueryBlock)select.getQuery();
      MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
      SQLTableSource from = query.getFrom();
      from.accept(visitor);
      List<SQLName> originalTables = visitor.getOriginalTables();
      List<String> allTable = (List)originalTables.stream().map((table) -> {
         SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr)table;
         return sqlIdentifierExpr.getName();
      }).collect(Collectors.toList());
      return allTable;
   }

   public static String addWhere(String sql, String where) {
      MySqlStatementParser parser = new MySqlStatementParser(sql);
      SQLSelectStatement sqlStatement = (SQLSelectStatement)parser.parseStatement();
      SQLSelect select = sqlStatement.getSelect();
      MySqlSelectQueryBlock query = (MySqlSelectQueryBlock)select.getQuery();
      MySqlCharExpr mySqlCharExpr = new MySqlCharExpr();
      mySqlCharExpr.setCharset(where);
      mySqlCharExpr.setText("");
      query.addWhere(mySqlCharExpr);
      return query.toString().replace("''", "");
   }

   public static String addFromSubSelectWhere(String sql, String where) {
      MySqlStatementParser parser = new MySqlStatementParser(sql);
      SQLStatement sqlStatement = parser.parseStatement();
      if (!(sqlStatement instanceof SQLSelectStatement)) {
         return sql;
      } else {
         SQLSelectStatement sqlSelectStatement = (SQLSelectStatement)SQLSelectStatement.class.cast(sqlStatement);
         SQLSelect select = sqlSelectStatement.getSelect();
         SQLSelectQuery query = select.getQuery();
         if (!(query instanceof MySqlSelectQueryBlock)) {
            return sql;
         } else {
            MySqlSelectQueryBlock mySqlSelectQueryBlock = (MySqlSelectQueryBlock)MySqlSelectQueryBlock.class.cast(query);
            SQLTableSource from = mySqlSelectQueryBlock.getFrom();
            if (!(from instanceof SQLSubqueryTableSource)) {
               return sql;
            } else {
               SQLSubqueryTableSource mySqlSelectQueryBlockFrom = (SQLSubqueryTableSource)SQLSubqueryTableSource.class.cast(from);
               SQLSelect subSelect = mySqlSelectQueryBlockFrom.getSelect();
               SQLSelectQuery subSelectQuery = subSelect.getQuery();
               if (!(subSelectQuery instanceof MySqlSelectQueryBlock)) {
                  return sql;
               } else {
                  MySqlSelectQueryBlock sqlSelectQueryBlock = (MySqlSelectQueryBlock)MySqlSelectQueryBlock.class.cast(subSelectQuery);
                  MySqlCharExpr mySqlCharExpr = new MySqlCharExpr();
                  mySqlCharExpr.setCharset(where);
                  mySqlCharExpr.setText("");
                  sqlSelectQueryBlock.addWhere(mySqlCharExpr);
                  return mySqlSelectQueryBlock.toString().replace("''", "");
               }
            }
         }
      }
   }

   public static String addXCustomWhere(String sql, String where) {
      boolean hasSubQuery = false;
      MySqlStatementParser parser = new MySqlStatementParser(sql);
      SQLStatement sqlStatement = parser.parseStatement();
      if (!(sqlStatement instanceof SQLSelectStatement)) {
         return sql;
      } else {
         SQLSelectStatement sqlSelectStatement = (SQLSelectStatement)SQLSelectStatement.class.cast(sqlStatement);
         SQLSelect select = sqlSelectStatement.getSelect();
         SQLSelectQuery sqlSelectQuery = select.getQuery();
         if (sqlSelectQuery instanceof MySqlSelectQueryBlock) {
            MySqlSelectQueryBlock query = (MySqlSelectQueryBlock)MySqlSelectQueryBlock.class.cast(sqlSelectQuery);
            SQLTableSource from = query.getFrom();
            if (from instanceof SQLSubqueryTableSource) {
               SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource)SQLSubqueryTableSource.class.cast(from);
               if (Objects.nonNull(sqlSubqueryTableSource.getSelect())) {
                  SQLSelect subSelect = sqlSubqueryTableSource.getSelect();
                  SQLSelectQuery subQuery = subSelect.getQuery();
                  if (subQuery instanceof MySqlSelectQueryBlock) {
                     MySqlSelectQueryBlock mySqlSelectQueryBlock = (MySqlSelectQueryBlock)MySqlSelectQueryBlock.class.cast(subQuery);
                     MySqlCharExpr mySqlCharExpr = new MySqlCharExpr();
                     mySqlCharExpr.setCharset(where);
                     mySqlCharExpr.setText("");
                     mySqlSelectQueryBlock.addWhere(mySqlCharExpr);
                     hasSubQuery = true;
                  }
               }
            } else if (!(from instanceof SQLJoinTableSource) && from instanceof SQLExprTableSource) {
            }

            if (!hasSubQuery) {
               MySqlCharExpr mySqlCharExpr = new MySqlCharExpr();
               mySqlCharExpr.setCharset(where);
               mySqlCharExpr.setText("");
               query.addWhere(mySqlCharExpr);
            }

            return query.toString().replace("''", "");
         } else {
            return sql;
         }
      }
   }

   public static <T> LambdaQueryWrapper<T> getQueryWrapper(Class<T> tClass) {
      return Wrappers.lambdaQuery();
   }

   public static <T> LambdaUpdateWrapper<T> getUpdateWrapper(Class<T> tClass) {
      return Wrappers.lambdaUpdate();
   }
}
