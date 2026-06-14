package com.chang.until.thread.model;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManageThread {
   private ResultTask resultTask;
   private ReduceTask reduceTask;
   private MapTask mapTask;

   public ManageThread(ResultTask resultTask, ReduceTask reduceTask, MapTask mapTask) {
      this.resultTask = resultTask;
      this.reduceTask = reduceTask;
      this.mapTask = mapTask;
   }

   public void Run(int size, int nThreads) throws InterruptedException {
      ExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(nThreads));
      List<ListenableFuture<?>> listenableFutures = new ArrayList();

      ListenableFuture transform;
      for(int i = 0; i < size; ++i) {
         transform = (ListenableFuture)executorService.submit(this.mapTask);
         listenableFutures.add(transform);
      }

      ListenableFuture allFutures = Futures.allAsList(listenableFutures);
      transform = Futures.transform(allFutures, this.reduceTask, executorService);
      this.resultTask.setEs(executorService);
      Futures.addCallback(transform, this.resultTask, executorService);

      while(!executorService.awaitTermination(1L, TimeUnit.SECONDS)) {
         SleepUtils.millisecond(100L);
      }

   }
}
