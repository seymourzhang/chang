package com.chang.common;

import java.util.Comparator;

public interface Prioritized extends Comparable<Prioritized> {
   Comparator<Object> COMPARATOR = (one, two) -> {
      boolean b1 = one instanceof Prioritized;
      boolean b2 = two instanceof Prioritized;
      if (b1 && !b2) {
         return -1;
      } else if (b2 && !b1) {
         return 1;
      } else {
         return b1 && b2 ? ((Prioritized)one).compareTo((Prioritized)two) : 0;
      }
   };
   int MAX_PRIORITY = Integer.MIN_VALUE;
   int MIN_PRIORITY = Integer.MAX_VALUE;
   int NORMAL_PRIORITY = 0;

   default int getPriority() {
      return 0;
   }

   default int compareTo(Prioritized that) {
      return Integer.compare(this.getPriority(), that.getPriority());
   }
}
