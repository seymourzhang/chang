package com.chang.until.thread.jdkmodel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadManage {
   private static final Map<String, ExecutorService> Manager = new ConcurrentHashMap();

   private static ExecutorService threadFactory(ThreadType threadType, int nThreads) {
      if (ThreadType.newCachedThreadPool == threadType) {
         return Executors.newCachedThreadPool();
      } else if (ThreadType.newWorkStealingPool == threadType) {
         return Executors.newWorkStealingPool();
      } else if (ThreadType.newSingleThreadExecutor == threadType) {
         return Executors.newSingleThreadExecutor();
      } else if (ThreadType.newFixedThreadPool == threadType) {
         return Executors.newFixedThreadPool(nThreads);
      } else {
         return ThreadType.newScheduledThreadPool == threadType ? Executors.newScheduledThreadPool(nThreads) : null;
      }
   }

   public static boolean createThread(String threadName, ThreadType threadType, int nThreads) {
      if (Manager.containsKey(threadName)) {
         return false;
      } else {
         ExecutorService es = threadFactory(threadType, nThreads);
         Manager.put(threadName, es);
         return true;
      }
   }

   public static Future<?> submitTask(String threadName, Runnable task) {
      return !Manager.containsKey(threadName) ? null : ((ExecutorService)Manager.get(threadName)).submit(task);
   }

   public static <T> Future<T> submitTask(String threadName, Runnable task, T result) {
      return !Manager.containsKey(threadName) ? null : ((ExecutorService)Manager.get(threadName)).submit(task, result);
   }

   public static <T> Future<T> submitTask(String threadName, Callable<T> task) {
      return !Manager.containsKey(threadName) ? null : ((ExecutorService)Manager.get(threadName)).submit(task);
   }

   public static boolean shutdownTask(String threadName) {
      if (!Manager.containsKey(threadName)) {
         return false;
      } else {
         ((ExecutorService)Manager.get(threadName)).shutdown();
         return true;
      }
   }

   public static boolean shutdownNowTask(String threadName) {
      if (!Manager.containsKey(threadName)) {
         return false;
      } else {
         ((ExecutorService)Manager.get(threadName)).shutdownNow();
         return true;
      }
   }

   public static boolean isShutdown(String threadName) {
      return ((ExecutorService)Manager.get(threadName)).isShutdown();
   }

   public static boolean isTerminated(String threadName) {
      return ((ExecutorService)Manager.get(threadName)).isTerminated();
   }

   public static boolean awaitTermination(String threadName, long timeout, TimeUnit unit) throws InterruptedException {
      return ((ExecutorService)Manager.get(threadName)).awaitTermination(timeout, unit);
   }

   public static void shutdownAllTask(boolean isNow) {
      Iterator var1 = Manager.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry<String, ExecutorService> entry = (Map.Entry)var1.next();
         ExecutorService mapValue = (ExecutorService)entry.getValue();
         if (isNow) {
            mapValue.shutdownNow();
         } else {
            mapValue.shutdown();
         }
      }

   }
}
