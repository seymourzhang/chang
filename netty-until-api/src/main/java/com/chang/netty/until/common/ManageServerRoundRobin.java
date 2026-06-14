package com.chang.netty.until.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageServerRoundRobin {
   private static final Map<String, RoundRobin> roundRobinMap = new ConcurrentHashMap(64);

   public static void add(String groupName, RoundRobin roundRobin) {
      roundRobinMap.put(groupName, roundRobin);
   }

   public static RoundRobin get(String groupName) {
      return (RoundRobin)roundRobinMap.get(groupName);
   }
}
