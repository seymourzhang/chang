package com.chang.until.timeTaskApi.lib;

import java.util.LinkedList;
import java.util.Queue;

public class ListenableFutureCallbackRegistry<T> {
   private final Queue<SuccessCallback<? super T>> successCallbacks = new LinkedList();
   private final Queue<FailureCallback> failureCallbacks = new LinkedList();
   private final Object mutex = new Object();
   private State state;
   private Object result;

   public ListenableFutureCallbackRegistry() {
      this.state = State.NEW;
   }

   public void addCallback(ListenableFutureCallback<? super T> callback) {
      synchronized(this.mutex) {
         switch (this.state) {
            case NEW:
               this.successCallbacks.add(callback);
               this.failureCallbacks.add(callback);
               break;
            case SUCCESS:
               this.notifySuccess(callback);
               break;
            case FAILURE:
               this.notifyFailure(callback);
         }

      }
   }

   private void notifySuccess(SuccessCallback<? super T> callback) {
      try {
         callback.onSuccess((T) this.result);
      } catch (Throwable var3) {
      }

   }

   private void notifyFailure(FailureCallback callback) {
      try {
         callback.onFailure((Throwable)this.result);
      } catch (Throwable var3) {
      }

   }

   public void addSuccessCallback(SuccessCallback<? super T> callback) {
      synchronized(this.mutex) {
         switch (this.state) {
            case NEW:
               this.successCallbacks.add(callback);
               break;
            case SUCCESS:
               this.notifySuccess(callback);
         }

      }
   }

   public void addFailureCallback(FailureCallback callback) {
      synchronized(this.mutex) {
         switch (this.state) {
            case NEW:
               this.failureCallbacks.add(callback);
               break;
            case FAILURE:
               this.notifyFailure(callback);
         }

      }
   }

   public void success(T result) {
      synchronized(this.mutex) {
         this.state = State.SUCCESS;
         this.result = result;

         SuccessCallback callback;
         while((callback = (SuccessCallback)this.successCallbacks.poll()) != null) {
            this.notifySuccess(callback);
         }

      }
   }

   public void failure(Throwable ex) {
      synchronized(this.mutex) {
         this.state = State.FAILURE;
         this.result = ex;

         FailureCallback callback;
         while((callback = (FailureCallback)this.failureCallbacks.poll()) != null) {
            this.notifyFailure(callback);
         }

      }
   }

   private static enum State {
      NEW,
      SUCCESS,
      FAILURE;
   }
}
