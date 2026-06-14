package com.chang.util.source.page;

import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.chang.common.CommUtils;
import com.chang.common.cache.LoadingCacheBase;
import com.chang.common.cache.Write;
import com.chang.common.cron.CronUtil;
import com.chang.until.QuartzTimeTask.QuartzConfig;
import com.chang.until.QuartzTimeTask.QuartzTaskManage;
import java.util.concurrent.TimeUnit;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackageTools<T> {
   private static final Logger log = LoggerFactory.getLogger(PackageTools.class);
   private final LoadingCacheBase<String, T[]> packageDataCache;

   public PackageTools(Class<T> tClass, Long maximumSize, Integer initialCapacity, Integer checkDuration, Long expireAfterAccessDuration, TimeUnit expireAfterAccessUnit, Long expireAfterWriteDuration, TimeUnit expireAfterWriteUnit, Long refreshAfterWriteDuration, TimeUnit refreshAfterWriteUnit, ICompleteDoWith<T> doWith) {
      this.packageDataCache = new LoadingCacheBase(maximumSize, initialCapacity, expireAfterAccessDuration, expireAfterAccessUnit, expireAfterWriteDuration, expireAfterWriteUnit, refreshAfterWriteDuration, refreshAfterWriteUnit, (CacheLoader)null, (Write)null, new RemoveDoWith(doWith, tClass));
      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("PackageCache", this.packageDataCache);
      jobDataMap.put("DoWith", doWith);
      jobDataMap.put("Type", tClass);
      QuartzConfig quartzConfig = new QuartzConfig();
      quartzConfig.setJobClass(CheckPackageJob.class);
      quartzConfig.setCronExpression(CronUtil.everySecond(checkDuration));
      quartzConfig.setJobDataMap(jobDataMap);

      try {
         QuartzTaskManage.addStartTask(CommUtils.getSnowflakeIdStr(0L), quartzConfig);
      } catch (Exception var15) {
         throw new RuntimeException(var15);
      }
   }

   public boolean containsKey(String key) {
      return this.packageDataCache.containsKey(key);
   }

   public void create(String key, Integer size) {
      if (!this.containsKey(key)) {
         T[] data = (T[])(new Object[size]);
         this.packageDataCache.getLoadingCache().put(key, data);
      }

   }

   public T[] getData(String key) {
      return (T[])this.packageDataCache.getLoadingCache().get(key);
   }

   public void addPackage(String key, Integer index, T data) {
      if (this.containsKey(key)) {
         T[] dataArray = this.getData(key);
         dataArray[index] = data;
         this.packageDataCache.getLoadingCache().put(key, dataArray);
      } else {
         log.info("Key: {} is not contains", key);
      }

   }

   public Integer getPageSize(String key) {
      if (this.containsKey(key)) {
         return this.getData(key).length;
      } else {
         log.info("Key: {} is not contains", key);
         return null;
      }
   }

   public static boolean isComplete(Object[] values) {
      Object[] var1 = values;
      int var2 = values.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object value = var1[var3];
         if (ObjectUtil.isNull(value)) {
            return false;
         }
      }

      return true;
   }
}
