package com.chang.until.mqttclient.v3.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class RunTask {
   private static final Logger logger = LoggerFactory.getLogger(RunTask.class);
   private static ThreadPoolTaskScheduler executor = null;

   public static void createNewTask(Runnable task) {
      executor.execute(task);
   }

   static {
      executor = new ThreadPoolTaskScheduler();
      executor.setPoolSize(100);
      executor.setThreadNamePrefix("MqttTask-");
      executor.setWaitForTasksToCompleteOnShutdown(true);
      executor.setAwaitTerminationSeconds(6000);
      executor.setErrorHandler((t) -> {
         logger.error("[Mqtt Task] ErrorHandler: ", t);
      });
      executor.initialize();
   }
}
