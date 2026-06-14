package com.chang.until.timeTaskApi.lib;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ExecutorConfigurationSupport extends CustomizableThreadFactory {
   protected final Log logger = LogFactory.getLog(this.getClass());
   private ThreadFactory threadFactory = this;
   private boolean threadNamePrefixSet = false;
   private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
   private boolean waitForTasksToCompleteOnShutdown = false;
   private long awaitTerminationMillis = 0L;
   private String beanName;
   private ExecutorService executor;

   public void setThreadFactory(ThreadFactory threadFactory) {
      this.threadFactory = (ThreadFactory)(threadFactory != null ? threadFactory : this);
   }

   public void setThreadNamePrefix(String threadNamePrefix) {
      super.setThreadNamePrefix(threadNamePrefix);
      this.threadNamePrefixSet = true;
   }

   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
      this.rejectedExecutionHandler = (RejectedExecutionHandler)(rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
   }

   public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
      this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
   }

   public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
      this.awaitTerminationMillis = (long)awaitTerminationSeconds * 1000L;
   }

   public void setAwaitTerminationMillis(long awaitTerminationMillis) {
      this.awaitTerminationMillis = awaitTerminationMillis;
   }

   public void initialize() {
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Initializing ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
      }

      if (!this.threadNamePrefixSet && this.beanName != null) {
         this.setThreadNamePrefix(this.beanName + "-");
      }

      this.executor = this.initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
   }

   protected abstract ExecutorService initializeExecutor(ThreadFactory var1, RejectedExecutionHandler var2);

   public void shutdown() {
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
      }

      if (this.executor != null) {
         if (this.waitForTasksToCompleteOnShutdown) {
            this.executor.shutdown();
         } else {
            Iterator var1 = this.executor.shutdownNow().iterator();

            while(var1.hasNext()) {
               Runnable remainingTask = (Runnable)var1.next();
               this.cancelRemainingTask(remainingTask);
            }
         }

         this.awaitTerminationIfNecessary(this.executor);
      }

   }

   protected void cancelRemainingTask(Runnable task) {
      if (task instanceof Future) {
         ((Future)task).cancel(true);
      }

   }

   private void awaitTerminationIfNecessary(ExecutorService executor) {
      if (this.awaitTerminationMillis > 0L) {
         try {
            if (!executor.awaitTermination(this.awaitTerminationMillis, TimeUnit.MILLISECONDS) && this.logger.isWarnEnabled()) {
               this.logger.warn("Timed out while waiting for executor" + (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");
            }
         } catch (InterruptedException var3) {
            if (this.logger.isWarnEnabled()) {
               this.logger.warn("Interrupted while waiting for executor" + (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");
            }

            Thread.currentThread().interrupt();
         }
      }

   }
}
