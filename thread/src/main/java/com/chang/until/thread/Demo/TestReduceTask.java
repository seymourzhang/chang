package com.chang.until.thread.Demo;

import com.chang.until.thread.model.ReduceTask;
import java.util.Iterator;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TestReduceTask extends ReduceTask<List<Integer>, Integer> {
   protected Integer task(@Nullable List<Integer> integers) {
      Integer data = 0;

      Integer integer;
      for(Iterator var3 = integers.iterator(); var3.hasNext(); data = data + integer) {
         integer = (Integer)var3.next();
      }

      return data;
   }
}
