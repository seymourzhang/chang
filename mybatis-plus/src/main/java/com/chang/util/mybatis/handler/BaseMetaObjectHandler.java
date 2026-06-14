package com.chang.util.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.chang.util.mybatis.config.MybatisPlusProperties;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class BaseMetaObjectHandler implements MetaObjectHandler {
   @Value("${mybatis-plus.global-config.db-config.logic-not-delete-value:0}")
   private Integer logicNotDeleteValue;
   @Autowired
   private MybatisPlusProperties mybatisPlusProperties;

   public void insertFill(MetaObject metaObject) {
      Long currentTime = System.currentTimeMillis();
      this.strictInsertFill(metaObject, "createdAt", Long.class, currentTime);
      this.strictInsertFill(metaObject, "updatedAt", Long.class, currentTime);
      this.strictInsertFillPublic(metaObject);
   }

   public void updateFill(MetaObject metaObject) {
      this.strictUpdateFill(metaObject, "updatedAt", Long.class, System.currentTimeMillis());
      this.strictInsertFillPublic(metaObject);
   }

   private void strictInsertFillPublic(MetaObject metaObject) {
      TableInfo tableInfo = this.findTableInfo(metaObject);
      TableFieldInfo versionFieldInfo;
      if (tableInfo.isWithLogicDelete()) {
         versionFieldInfo = tableInfo.getLogicDeleteFieldInfo();
         this.strictInsertFill(metaObject, versionFieldInfo.getField().getName(), Integer.class, this.logicNotDeleteValue);
      }

      if (this.mybatisPlusProperties.getOpenOptimisticLocker() && tableInfo.isWithVersion()) {
         versionFieldInfo = tableInfo.getVersionFieldInfo();
         this.strictInsertFill(metaObject, versionFieldInfo.getField().getName(), Integer.class, 1);
      }

   }
}
