package com.chang.uclass.bytecode;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Predicates {
   Predicate[] EMPTY_ARRAY = new Predicate[0];

   static <T> Predicate<T> alwaysTrue() {
      return (e) -> {
         return true;
      };
   }

   static <T> Predicate<T> alwaysFalse() {
      return (e) -> {
         return false;
      };
   }

   static <T> Predicate<T> and(Predicate<T>... predicates) {
      return (Predicate)Stream.of(predicates).reduce((a, b) -> {
         return a.and(b);
      }).orElseGet(Predicates::alwaysTrue);
   }

   static <T> Predicate<T> or(Predicate<T>... predicates) {
      return (Predicate)Stream.of(predicates).reduce((a, b) -> {
         return a.or(b);
      }).orElse((e) -> {
         return true;
      });
   }
}
