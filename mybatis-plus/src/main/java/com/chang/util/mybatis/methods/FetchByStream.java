package com.chang.util.mybatis.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class FetchByStream extends AbstractMethod {
   public FetchByStream(String methodName) {
      super(methodName);
   }

   public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
      String sqlFormat = "<script>\nSELECT %s FROM %s %s limit #{start},#{size}\n</script>";
      String selectColumns = this.sqlSelectColumns(tableInfo, true);
      String tableName = tableInfo.getTableName();
      String sqlWhere = this.sqlWhereEntityWrapper(true, tableInfo);
      String sql = String.format(sqlFormat, selectColumns, tableName, sqlWhere);
      SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
      return this.addSelectMappedStatementForTable(mapperClass, sqlSource, tableInfo);
   }
}
