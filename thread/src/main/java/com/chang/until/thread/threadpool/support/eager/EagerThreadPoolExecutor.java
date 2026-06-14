package com.chang.until.thread.threadpool.support.eager;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EagerThreadPoolExecutor extends ThreadPoolExecutor {
   private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

   public EagerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, TaskQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
   }

   public int getSubmittedTaskCount() {
      return this.submittedTaskCount.get();
   }

   public void execute(Runnable command) {
      if (command == null) {
         throw new NullPointerException();
      } else {
         this.submittedTaskCount.incrementAndGet();

         try {
            super.execute(command);
         } catch (RejectedExecutionException var6) {
            RejectedExecutionException rx = var6;
            TaskQueue queue = (TaskQueue)super.getQueue();

            try {
               if (!queue.retryOffer(command, 0L, TimeUnit.MILLISECONDS)) {
                  this.submittedTaskCount.decrementAndGet();
                  throw new RejectedExecutionException("Queue capacity is full.", rx);
               }
            } catch (InterruptedException var5) {
               this.submittedTaskCount.decrementAndGet();
               throw new RejectedExecutionException(var5);
            }
         } catch (Throwable var7) {
            this.submittedTaskCount.decrementAndGet();
            throw var7;
         }

      }
   }
}
