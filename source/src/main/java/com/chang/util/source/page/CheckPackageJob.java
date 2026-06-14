package com.chang.util.source.page;

import com.chang.common.CommUtils;
import com.chang.common.cache.LoadingCacheBase;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CheckPackageJob implements Job {
   public void execute(JobExecutionContext context) throws JobExecutionException {
      LoadingCacheBase<String, Object[]> cache = (LoadingCacheBase)context.getJobDetail().getJobDataMap().get("PackageCache");
      ICompleteDoWith completeDoWith = (ICompleteDoWith)context.getJobDetail().getJobDataMap().get("DoWith");
      Class aClass = (Class)context.getJobDetail().getJobDataMap().get("Type");
      ConcurrentMap<String, Object[]> map = cache.getLoadingCache().asMap();
      Iterator var6 = map.keySet().iterator();

      while(var6.hasNext()) {
         String key = (String)var6.next();
         Object[] values = (Object[])map.get(key);
         if (PackageTools.isComplete(values)) {
            completeDoWith.completeDoWith(key, CommUtils.convertArray(aClass, values));
            cache.delByKey(key);
         }
      }

   }
}
