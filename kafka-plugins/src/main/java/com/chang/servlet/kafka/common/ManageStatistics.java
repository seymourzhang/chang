package com.chang.servlet.kafka.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ManageStatistics {
   private static final Map<String, AtomicLong> inStatistics = new ConcurrentHashMap();
   private static final Map<String, AtomicLong> outStatistics = new ConcurrentHashMap();

   public static Long inMessageAdd(String name) {
      if (!inStatistics.containsKey(name)) {
         inStatistics.put(name, new AtomicLong(0L));
      }

      return ((AtomicLong)inStatistics.get(name)).addAndGet(1L);
   }

   public static Long outMessageAdd(String name) {
      if (!outStatistics.containsKey(name)) {
         outStatistics.put(name, new AtomicLong(0L));
      }

      return ((AtomicLong)outStatistics.get(name)).addAndGet(1L);
   }

   public static Long getInMessageCount(String name) {
      return !inStatistics.containsKey(name) ? 0L : ((AtomicLong)inStatistics.get(name)).get();
   }

   public static Long getOutMessageCount(String name) {
      return !outStatistics.containsKey(name) ? 0L : ((AtomicLong)outStatistics.get(name)).get();
   }
}
