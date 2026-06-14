package com.chang.util.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import org.apache.commons.lang3.StringUtils;

public class BaseTableNameHandler implements TableNameHandler {
   private static final ThreadLocal<String> SUFFIX = new ThreadLocal();

   public static void setTableNameSuffix(String suffix) {
      SUFFIX.set(suffix);
   }

   public String dynamicTableName(String sql, String tableName) {
      String suffix = (String)SUFFIX.get();
      if (StringUtils.isNotBlank(suffix)) {
         SUFFIX.remove();
         return tableName + suffix;
      } else {
         return tableName;
      }
   }
}
