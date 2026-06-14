package com.chang.common;

import java.util.Map;
import java.util.Objects;

@FunctionalInterface
public interface DataConsumer<T> {
   void accept(Map<String, Object> var1, String var2, T var3);

   default DataConsumer<T> andThen(DataConsumer<? super T> after) {
      Objects.requireNonNull(after);
      return (content, key, t) -> {
         this.accept(content, key, t);
         after.accept(content, key, t);
      };
   }
}
