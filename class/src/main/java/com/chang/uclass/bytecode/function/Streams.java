package com.chang.uclass.bytecode.function;

import com.chang.uclass.bytecode.Predicates;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Streams {
   static <T, S extends Iterable<T>> Stream<T> filterStream(S values, Predicate<T> predicate) {
      return StreamSupport.stream(values.spliterator(), false).filter(predicate);
   }

   static <T, S extends Iterable<T>> List<T> filterList(S values, Predicate<T> predicate) {
      return (List)filterStream(values, predicate).collect(Collectors.toList());
   }

   static <T, S extends Iterable<T>> Set<T> filterSet(S values, Predicate<T> predicate) {
      return (Set)filterStream(values, predicate).collect(LinkedHashSet::new, Set::add, Set::addAll);
   }

   static <T, S extends Iterable<T>> S filter(S values, Predicate<T> predicate) {
      boolean isSet = Set.class.isAssignableFrom(values.getClass());
      return (S) (isSet ? filterSet(values, predicate) : filterList(values, predicate));
   }

   static <T, S extends Iterable<T>> S filterAll(S values, Predicate<T>... predicates) {
      return filter(values, Predicates.and(predicates));
   }

   static <T, S extends Iterable<T>> S filterAny(S values, Predicate<T>... predicates) {
      return filter(values, Predicates.or(predicates));
   }

   static <T> T filterFirst(Iterable<T> values, Predicate<T>... predicates) {
      return StreamSupport.stream(values.spliterator(), false).filter(Predicates.and(predicates)).findFirst().orElse((T) null);
   }
}
