package com.chang.until.thread.threadpool.support.cached;

import com.chang.until.thread.threadlocal.NamedInternalThreadFactory;
import com.chang.until.thread.threadpool.support.AbortPolicyWithReport;
import com.chang.until.thread.threadpool.support.ThreadConfig;
import com.chang.until.thread.threadpool.support.ThreadPool;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CachedThreadPool implements ThreadPool {
   public Executor getExecutor(ThreadConfig threadConfig) {
      String name = threadConfig.getName();
      String dumpPath = threadConfig.getDumpPath();
      int cores = threadConfig.getCores();
      int threads = threadConfig.getThreads();
      int queues = threadConfig.getQueues();
      int alive = threadConfig.getAlive();
      return new ThreadPoolExecutor(cores, threads, (long)alive, TimeUnit.MILLISECONDS, (BlockingQueue)(queues == 0 ? new SynchronousQueue() : (queues < 0 ? new LinkedBlockingQueue() : new LinkedBlockingQueue(queues))), new NamedInternalThreadFactory(name, true), new AbortPolicyWithReport(name, dumpPath));
   }
}
