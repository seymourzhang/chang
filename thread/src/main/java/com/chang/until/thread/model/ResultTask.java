package com.chang.until.thread.model;

import com.google.common.util.concurrent.FutureCallback;
import java.util.concurrent.ExecutorService;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ResultTask<T> implements FutureCallback<T> {
   private ExecutorService es = null;

   public void setEs(ExecutorService es) {
      this.es = es;
   }

   public void onSuccess(@Nullable T t) {
      this.task(t);
      if (this.es != null) {
         this.es.shutdown();
      }

   }

   protected abstract void task(@Nullable T var1);
}
