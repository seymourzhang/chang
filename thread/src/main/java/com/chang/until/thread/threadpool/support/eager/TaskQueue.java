package com.chang.until.thread.threadpool.support.eager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class TaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {
   private EagerThreadPoolExecutor executor;

   public TaskQueue(int capacity) {
      super(capacity);
   }

   public void setExecutor(EagerThreadPoolExecutor exec) {
      this.executor = exec;
   }

   public boolean offer(Runnable runnable) {
      if (this.executor == null) {
         throw new RejectedExecutionException("The task queue does not have executor!");
      } else {
         int currentPoolThreadSize = this.executor.getPoolSize();
         if (this.executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
         } else {
            return currentPoolThreadSize < this.executor.getMaximumPoolSize() ? false : super.offer(runnable);
         }
      }
   }

   public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
      if (this.executor.isShutdown()) {
         throw new RejectedExecutionException("Executor is shutdown!");
      } else {
         return super.offer(o, timeout, unit);
      }
   }
}
