package com.chang.until.thread.model;

import java.util.concurrent.Callable;

public abstract class MapTask<T> implements Callable<T> {
   public T call() throws Exception {
      return this.task();
   }

   protected abstract T task() throws Exception;
}
