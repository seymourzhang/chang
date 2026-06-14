package com.chang.util.source.link;

import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import java.util.function.Function;

public class LinkSource<T, R> implements LinkSourceInterface {
   private final InputSource<T, R> from;
   private final OutputSource to;
   private final Function<T, R> function;

   public LinkSource(InputSource<T, R> from, OutputSource to, Function<T, R> function) {
      this.from = from;
      this.to = to;
      this.function = function;
   }

   public void runLink() throws Exception {
      try {
         this.from.InPut(this.to, this.function);
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public InputSource<T, R> getFrom() {
      return this.from;
   }

   public OutputSource getTo() {
      return this.to;
   }

   public Function<T, R> getFunction() {
      return this.function;
   }
}
