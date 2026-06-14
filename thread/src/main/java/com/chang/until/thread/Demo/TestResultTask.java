package com.chang.until.thread.Demo;

import com.chang.until.thread.model.ResultTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TestResultTask extends ResultTask<Integer> {
   protected void task(@Nullable Integer integer) {
      System.out.println("TestResultTask: " + integer);
   }

   public void onFailure(Throwable throwable) {
      throwable.printStackTrace();
   }
}
