package com.chang.util.mybatis.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.chang.util.mybatis.injector.BaseSqlInjector;
import com.chang.util.mybatis.handler.BaseMetaObjectHandler;
import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import com.chang.util.mybatis.ddl.BaseDdl;
import com.chang.util.mybatis.util.TenantIdContextHandler;
import java.util.stream.Stream;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableMPP
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class MybatisPlusConfigBase {
   private final MybatisPlusProperties plusProperties;

   public MybatisPlusConfigBase(MybatisPlusProperties plusProperties) {
      this.plusProperties = plusProperties;
   }

   @Bean
   @Primary
   @ConditionalOnProperty(
      value = {"wg.mybatis.plus.config.enable-sql-injector"},
      havingValue = "true"
   )
   public BaseSqlInjector getBaseSqlInjector() {
      return new BaseSqlInjector();
   }

   @Bean
   @ConditionalOnProperty(
      value = {"wg.mybatis.plus.config.enable-fill"},
      havingValue = "true"
   )
   public BaseMetaObjectHandler getBaseMetaObjectHandler() {
      return new BaseMetaObjectHandler();
   }

   @Bean
   @ConditionalOnProperty(
      value = {"wg.mybatis.plus.config.enable-base-ddl"},
      havingValue = "true"
   )
   public BaseDdl getBaseDdl() {
      String[] sqlFiles = this.plusProperties.getSqlDdlPaths().split(",");
      return new BaseDdl(sqlFiles);
   }

   @Bean
   protected MybatisPlusInterceptor getBaseInterceptor() throws Exception {
      MybatisPlusInterceptor me = new MybatisPlusInterceptor();

      if (this.plusProperties.getOpenDynamicTable()) {
         DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
         dynamicTableNameInnerInterceptor.setTableNameHandler(new BaseTableNameHandler());
         me.addInnerInterceptor(dynamicTableNameInnerInterceptor);
      }

      if (this.plusProperties.getOpenOptimisticLocker()) {
         me.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
      }

      return me;
   }
}
