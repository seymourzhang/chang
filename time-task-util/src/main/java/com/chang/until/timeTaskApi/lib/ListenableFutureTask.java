package com.chang.until.timeTaskApi.lib;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<T> extends FutureTask<T> implements ListenableFuture<T> {
   private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry();

   public ListenableFutureTask(Callable<T> callable) {
      super(callable);
   }

   public ListenableFutureTask(Runnable runnable, T result) {
      super(runnable, result);
   }

   public void addCallback(ListenableFutureCallback<? super T> callback) {
      this.callbacks.addCallback(callback);
   }

   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
      this.callbacks.addSuccessCallback(successCallback);
      this.callbacks.addFailureCallback(failureCallback);
   }

   public CompletableFuture<T> completable() {
      CompletableFuture<T> completable = new DelegatingCompletableFuture(this);
      this.callbacks.addSuccessCallback(completable::complete);
      this.callbacks.addFailureCallback(completable::completeExceptionally);
      return completable;
   }

   protected void done() {
      Object cause;
      try {
         T result = this.get();
         this.callbacks.success(result);
         return;
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         return;
      } catch (ExecutionException var4) {
         cause = var4.getCause();
         if (cause == null) {
            cause = var4;
         }
      } catch (Throwable var5) {
         cause = var5;
      }

      this.callbacks.failure((Throwable)cause);
   }
}
