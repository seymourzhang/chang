package com.chang.until.thread.Demo;

import com.chang.until.thread.model.MapTask;

public class TestMapTask extends MapTask<Integer> {
   protected Integer task() throws Exception {
      Integer num = (int)(1.0 + Math.random() * 10.0);
      System.out.println(num);
      return num;
   }
}
