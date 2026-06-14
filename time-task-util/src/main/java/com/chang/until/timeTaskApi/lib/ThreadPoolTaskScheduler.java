package com.chang.until.timeTaskApi.lib;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolTaskScheduler extends ExecutorConfigurationSupport {
   private static final Logger log = LoggerFactory.getLogger(ThreadPoolTaskScheduler.class);
   private final Map<Object, ListenableFuture<?>> listenableFutureMap;
   private volatile int poolSize;
   private ExecutorService executor;
   private volatile ErrorHandler errorHandler;
   private ThreadFactory threadFactory;
   private RejectedExecutionHandler rejectedExecutionHandler;
   private volatile boolean removeOnCancelPolicy;
   private ScheduledExecutorService scheduledExecutor;

   public ThreadPoolTaskScheduler() {
      this.listenableFutureMap = new ConcurrentReferenceHashMap(16, ConcurrentReferenceHashMap.ReferenceType.WEAK);
      this.poolSize = 1;
      this.threadFactory = this;
      this.rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
      this.removeOnCancelPolicy = false;
   }

   public void setErrorHandler(ErrorHandler errorHandler) {
      this.errorHandler = errorHandler;
   }

   public void setPoolSize(int poolSize) {
      this.poolSize = poolSize;
      if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
         ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setCorePoolSize(poolSize);
      }

   }

   protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
      return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
   }

   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
      this.scheduledExecutor = this.createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
      if (this.removeOnCancelPolicy) {
         if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
            ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setRemoveOnCancelPolicy(true);
         } else {
            log.debug("Could not apply remove-on-cancel policy - not a ScheduledThreadPoolExecutor");
         }
      }

      return this.scheduledExecutor;
   }

   public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
      return this.scheduledExecutor;
   }

   public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() throws IllegalStateException {
      return (ScheduledThreadPoolExecutor)this.scheduledExecutor;
   }

   public void initialize() {
      this.executor = this.initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
   }

   public Thread newThread(Runnable runnable) {
      return this.createThread(runnable);
   }

   public DelegatingErrorHandlingRunnable decorateTaskWithErrorHandler(Runnable task, ErrorHandler errorHandler, boolean isRepeatingTask) {
      if (task instanceof DelegatingErrorHandlingRunnable) {
         return (DelegatingErrorHandlingRunnable)task;
      } else {
         ErrorHandler eh = errorHandler != null ? errorHandler : TaskUtils.getDefaultErrorHandler(true);
         return new DelegatingErrorHandlingRunnable(task, eh);
      }
   }

   private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask) {
      return this.decorateTaskWithErrorHandler(task, (t) -> {
         log.error("errorHandlingTask Err", t);
      }, isRepeatingTask);
   }

   public void execute(Runnable task) {
      Executor executor = this.getScheduledExecutor();

      try {
         executor.execute(this.errorHandlingTask(task, false));
      } catch (RejectedExecutionException var4) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var4);
      }
   }

   public Future<?> submit(Runnable task) {
      ExecutorService executor = this.getScheduledExecutor();

      try {
         return executor.submit(this.errorHandlingTask(task, false));
      } catch (RejectedExecutionException var4) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var4);
      }
   }

   public <T> Future<T> submit(Callable<T> task) {
      ExecutorService executor = this.getScheduledExecutor();

      try {
         Callable<T> taskToUse = task;
         ErrorHandler errorHandler = this.errorHandler;
         if (errorHandler != null) {
            taskToUse = new DelegatingErrorHandlingCallable(task, errorHandler);
         }

         return executor.submit((Callable)taskToUse);
      } catch (RejectedExecutionException var5) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var5);
      }
   }

   public ListenableFuture<?> submitListenable(Runnable task) {
      ExecutorService executor = this.getScheduledExecutor();

      try {
         ListenableFutureTask<Object> listenableFuture = new ListenableFutureTask(task, (Object)null);
         this.executeAndTrack(executor, listenableFuture);
         return listenableFuture;
      } catch (RejectedExecutionException var4) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var4);
      }
   }

   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
      ExecutorService executor = this.getScheduledExecutor();

      try {
         ListenableFutureTask<T> listenableFuture = new ListenableFutureTask(task);
         this.executeAndTrack(executor, listenableFuture);
         return listenableFuture;
      } catch (RejectedExecutionException var4) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var4);
      }
   }

   protected void cancelRemainingTask(Runnable task) {
      super.cancelRemainingTask(task);
      ListenableFuture<?> listenableFuture = (ListenableFuture)this.listenableFutureMap.get(task);
      if (listenableFuture != null) {
         listenableFuture.cancel(true);
      }

   }

   private void executeAndTrack(ExecutorService executor, ListenableFutureTask<?> listenableFuture) {
      Future<?> scheduledFuture = executor.submit(this.errorHandlingTask(listenableFuture, false));
      this.listenableFutureMap.put(scheduledFuture, listenableFuture);
      listenableFuture.addCallback((result) -> {
         ListenableFuture var10000 = (ListenableFuture)this.listenableFutureMap.remove(scheduledFuture);
      }, (ex) -> {
         ListenableFuture var10000 = (ListenableFuture)this.listenableFutureMap.remove(scheduledFuture);
      });
   }

   public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
      ScheduledExecutorService executor = this.getScheduledExecutor();
      long initialDelay = startTime.getTime() - System.currentTimeMillis();

      try {
         return executor.schedule(this.errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException var7) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var7);
      }
   }

   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
      ScheduledExecutorService executor = this.getScheduledExecutor();
      long initialDelay = startTime.getTime() - System.currentTimeMillis();

      try {
         return executor.scheduleAtFixedRate(this.errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException var9) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var9);
      }
   }

   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
      ScheduledExecutorService executor = this.getScheduledExecutor();

      try {
         return executor.scheduleAtFixedRate(this.errorHandlingTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException var6) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var6);
      }
   }

   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
      ScheduledExecutorService executor = this.getScheduledExecutor();
      long initialDelay = startTime.getTime() - System.currentTimeMillis();

      try {
         return executor.scheduleWithFixedDelay(this.errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException var9) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var9);
      }
   }

   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
      ScheduledExecutorService executor = this.getScheduledExecutor();

      try {
         return executor.scheduleWithFixedDelay(this.errorHandlingTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException var6) {
         throw new RuntimeException("Executor [" + executor + "] did not accept task: " + task, var6);
      }
   }

   private static class DelegatingErrorHandlingCallable<V> implements Callable<V> {
      private final Callable<V> delegate;
      private final ErrorHandler errorHandler;

      public DelegatingErrorHandlingCallable(Callable<V> delegate, ErrorHandler errorHandler) {
         this.delegate = delegate;
         this.errorHandler = errorHandler;
      }

      public V call() throws Exception {
         try {
            return this.delegate.call();
         } catch (Throwable var2) {
            this.errorHandler.handleError(var2);
            return null;
         }
      }
   }
}
