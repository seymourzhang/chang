package com.chang.common.ExCollector;

import com.alibaba.fastjson.JSONArray;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class JSONArrayCollector implements Collector<Object, JSONArray, JSONArray> {
   public Supplier<JSONArray> supplier() {
      return JSONArray::new;
   }

   public BiConsumer<JSONArray, Object> accumulator() {
      return (jsona, item) -> {
         jsona.add(item);
      };
   }

   public BinaryOperator<JSONArray> combiner() {
      return (list1, list2) -> {
         list1.addAll(list2);
         return list1;
      };
   }

   public Function<JSONArray, JSONArray> finisher() {
      return Function.identity();
   }

   public Set<Characteristics> characteristics() {
      return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
   }
}
