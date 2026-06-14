package com.chang.until.thread.Demo;

import com.chang.until.thread.model.ManageThread;

public class Test {
   public static void main(String[] args) throws InterruptedException {
      ManageThread mt = new ManageThread(new TestResultTask(), new TestReduceTask(), new TestMapTask());
      mt.Run(10, 100);
   }
}
