package com.chang.until.thread.threadpool.support;

import java.util.concurrent.Executor;

public interface ThreadPool {
   Executor getExecutor(ThreadConfig var1);
}
