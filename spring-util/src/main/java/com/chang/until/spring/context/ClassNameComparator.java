package com.chang.until.spring.context;

import java.util.Comparator;

public class ClassNameComparator implements Comparator<Class<?>> {
   public int compare(Class<?> o1, Class<?> o2) {
      if (o1 == null) {
         return -1;
      } else {
         return o2 == null ? 1 : o1.getName().compareToIgnoreCase(o2.getName());
      }
   }
}
