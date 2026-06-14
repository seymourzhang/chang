package com.chang.until.thread.forkmodel;

import java.util.concurrent.RecursiveTask;

public abstract class RunTask<T> extends RecursiveTask<T> {
   private static final long serialVersionUID = 6620176946858179415L;

   protected T compute() {
      return this.task();
   }

   protected abstract T task();
}
