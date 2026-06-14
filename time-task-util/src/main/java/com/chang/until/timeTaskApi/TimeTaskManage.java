package com.chang.until.timeTaskApi;

import com.alibaba.fastjson.JSONObject;
import com.chang.until.timeTaskApi.lib.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTaskManage {
   private static final Logger logger = LoggerFactory.getLogger(TimeTaskManage.class);
   private static final ConcurrentHashMap<String, Future> taskFutureRepo = new ConcurrentHashMap();
   private static ThreadPoolTaskScheduler executor = null;

   public static void clearDoneTask() {
      Iterator var0 = taskFutureRepo.entrySet().iterator();

      while(var0.hasNext()) {
         Map.Entry<String, Future> entry = (Map.Entry)var0.next();
         String mapKey = (String)entry.getKey();
         Future mapValue = (Future)entry.getValue();
         if (mapValue.isDone()) {
            taskFutureRepo.remove(mapKey);
         }
      }

   }

   public static boolean clearDoneTask(String taskId) {
      Iterator var1 = taskFutureRepo.entrySet().iterator();

      String mapKey;
      Future mapValue;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         Map.Entry<String, Future> entry = (Map.Entry)var1.next();
         mapKey = (String)entry.getKey();
         mapValue = (Future)entry.getValue();
      } while(!mapKey.equals(taskId) || !mapValue.isDone());

      taskFutureRepo.remove(mapKey);
      return true;
   }

   public static JSONObject getAllTaskStatus() {
      JSONObject status = new JSONObject();
      Iterator var1 = taskFutureRepo.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry<String, Future> entry = (Map.Entry)var1.next();
         String mapKey = (String)entry.getKey();
         Future mapValue = (Future)entry.getValue();
         if (mapValue.isDone()) {
            status.put(mapKey, TaskStatus.Done.getValue());
         } else if (mapValue.isCancelled()) {
            status.put(mapKey, TaskStatus.CANCEL.getValue());
         } else {
            status.put(mapKey, TaskStatus.ALIVE.getValue());
         }
      }

      return status;
   }

   public static void stopAllTask() {
      Iterator var0 = taskFutureRepo.entrySet().iterator();

      while(var0.hasNext()) {
         Map.Entry<String, Future> entry = (Map.Entry)var0.next();
         Future mapValue = (Future)entry.getValue();
         if (!mapValue.isDone() && !mapValue.isCancelled()) {
            mapValue.cancel(true);
         }
      }

   }

   public static void createNewTask(String taskId, Runnable task, Date startTime) {
      if (taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id already existed");
      } else {
         ScheduledFuture<?> scheduledFuture = executor.schedule(task, startTime);
         taskFutureRepo.put(taskId, scheduledFuture);
      }
   }

   public static void createNewTask(String taskId, Runnable task) {
      if (taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id already existed");
      } else {
         Future<?> future = executor.submit(task);
         taskFutureRepo.put(taskId, future);
      }
   }

   public static <T> T createNewTask(Callable<T> task) throws ExecutionException, InterruptedException {
      Future<T> future = executor.submit(task);
      return future.get();
   }

   public static <T> Future<T> createNewTaskFuture(Callable<T> task) throws ExecutionException, InterruptedException {
      return executor.submit(task);
   }

   public static void createNewTask(Runnable task) throws ExecutionException, InterruptedException {
      executor.submit(task);
   }

   public static <T> T createNewTaskTimeOut(Callable<T> task, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      Future<T> future = executor.submit(task);
      return future.get(timeout, unit);
   }

   public static void scheduleAtFixedRate(String taskId, Runnable task, long time, TimeUnit unit) {
      if (taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id already existed");
      } else {
         long period = unit.toMillis(time);
         ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(task, period);
         taskFutureRepo.put(taskId, scheduledFuture);
      }
   }

   public static void scheduleAtFixedRate(String taskId, Date startTime, Runnable task, long time, TimeUnit unit) {
      if (taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id already existed");
      } else {
         long period = unit.toMillis(time);
         ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(task, startTime, period);
         taskFutureRepo.put(taskId, scheduledFuture);
      }
   }

   public static void scheduleWithFixedDelay(String taskId, Runnable task, long time, TimeUnit unit) {
      if (taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id already existed");
      } else {
         long delay = unit.toMillis(time);
         ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(task, delay);
         taskFutureRepo.put(taskId, scheduledFuture);
      }
   }

   public static void scheduleWithFixedDelay(String taskId, Date startTime, Runnable task, long time, TimeUnit unit) {
      if (taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id already existed");
      } else {
         long delay = unit.toMillis(time);
         ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(task, startTime, delay);
         taskFutureRepo.put(taskId, scheduledFuture);
      }
   }

   public static boolean stopTask(String taskId) {
      if (!taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id not existed");
      } else {
         return ((Future)taskFutureRepo.get(taskId)).cancel(true);
      }
   }

   public static boolean isCancelledTask(String taskId) {
      if (!taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id not existed");
      } else {
         return ((Future)taskFutureRepo.get(taskId)).isCancelled();
      }
   }

   public static boolean isDoneTask(String taskId) {
      if (!taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id not existed");
      } else {
         return ((Future)taskFutureRepo.get(taskId)).isDone();
      }
   }

   public static boolean isRunTask(String taskId) {
      if (!taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id not existed");
      } else {
         boolean done = ((Future)taskFutureRepo.get(taskId)).isDone();
         boolean cancelled = ((Future)taskFutureRepo.get(taskId)).isCancelled();
         return !done && !cancelled;
      }
   }

   public static void awaitDoneTask(String taskId) throws InterruptedException {
      if (!taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id not existed");
      } else {
         Future future = (Future)taskFutureRepo.get(taskId);
         if (!future.isCancelled()) {
            while(!future.isDone()) {
               TimeUnit.MILLISECONDS.sleep(100L);
            }

         }
      }
   }

   public static void awaitDoneTask(String taskId, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
      if (!taskFutureRepo.containsKey(taskId)) {
         throw new RuntimeException("[Time Task]  The task id not existed");
      } else {
         Future future = (Future)taskFutureRepo.get(taskId);
         if (!future.isCancelled() && !future.isDone()) {
            unit.sleep(timeout);
            if (!future.isDone()) {
               throw new TimeoutException();
            }
         }
      }
   }

   public static Boolean hasTask(String taskId) {
      clearDoneTask();
      return taskFutureRepo.containsKey(taskId) ? true : false;
   }

   static {
      executor = new ThreadPoolTaskScheduler();
      executor.setPoolSize(200);
      executor.setThreadNamePrefix("TimeTask-");
      executor.setWaitForTasksToCompleteOnShutdown(true);
      executor.setAwaitTerminationSeconds(6000);
      executor.setErrorHandler((t) -> {
         logger.error("[Time Task] ErrorHandler: ", t);
      });
      executor.initialize();
   }
}
