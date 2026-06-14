package com.chang.until.timeTaskApi.lib;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

class DelegatingCompletableFuture<T> extends CompletableFuture<T> {
   private final Future<T> delegate;

   public DelegatingCompletableFuture(Future<T> delegate) {
      this.delegate = delegate;
   }

   public boolean cancel(boolean mayInterruptIfRunning) {
      boolean result = this.delegate.cancel(mayInterruptIfRunning);
      super.cancel(mayInterruptIfRunning);
      return result;
   }
}
