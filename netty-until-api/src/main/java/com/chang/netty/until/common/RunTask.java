package com.chang.netty.until.common;

import com.chang.until.timeTaskApi.lib.ThreadPoolTaskScheduler;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunTask {
   private static final Logger log = LoggerFactory.getLogger(RunTask.class);
   private static ThreadPoolTaskScheduler executor = null;

   public static Future<?> createNewTask(Runnable task) {
      return executor.submit(task);
   }

   static {
      executor = new ThreadPoolTaskScheduler();
      executor.setPoolSize(100);
      executor.setThreadNamePrefix("NettyTask-");
      executor.setWaitForTasksToCompleteOnShutdown(true);
      executor.setAwaitTerminationSeconds(6000);
      executor.setErrorHandler((t) -> {
         log.error("[Netty] ErrorHandler: ", t);
      });
      executor.initialize();
   }
}
