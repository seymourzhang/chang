package com.chang.until.thread.threadpool.support.eager;

import com.chang.until.thread.threadlocal.NamedInternalThreadFactory;
import com.chang.until.thread.threadpool.support.AbortPolicyWithReport;
import com.chang.until.thread.threadpool.support.ThreadConfig;
import com.chang.until.thread.threadpool.support.ThreadPool;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class EagerThreadPool implements ThreadPool {
   public Executor getExecutor(ThreadConfig threadConfig) {
      String name = threadConfig.getName();
      String dumpPath = threadConfig.getDumpPath();
      int cores = threadConfig.getCores();
      int threads = threadConfig.getThreads();
      int queues = threadConfig.getQueues();
      int alive = threadConfig.getAlive();
      TaskQueue<Runnable> taskQueue = new TaskQueue(queues <= 0 ? 1 : queues);
      EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(cores, threads, (long)alive, TimeUnit.MILLISECONDS, taskQueue, new NamedInternalThreadFactory(name, true), new AbortPolicyWithReport(name, dumpPath));
      taskQueue.setExecutor(executor);
      return executor;
   }
}
