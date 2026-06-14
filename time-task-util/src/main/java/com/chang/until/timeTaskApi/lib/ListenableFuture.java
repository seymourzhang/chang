package com.chang.until.timeTaskApi.lib;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface ListenableFuture<T> extends Future<T> {
   void addCallback(ListenableFutureCallback<? super T> var1);

   void addCallback(SuccessCallback<? super T> var1, FailureCallback var2);

   default CompletableFuture<T> completable() {
      CompletableFuture<T> completable = new DelegatingCompletableFuture(this);
      this.addCallback(completable::complete, completable::completeExceptionally);
      return completable;
   }
}
