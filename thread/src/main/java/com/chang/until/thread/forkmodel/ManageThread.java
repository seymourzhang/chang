package com.chang.until.thread.forkmodel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class ManageThread {
   public static Future<?> Run(RunTask<?> runTask) {
      ForkJoinPool forkJoinPool = new ForkJoinPool();
      return forkJoinPool.submit(runTask);
   }
}
