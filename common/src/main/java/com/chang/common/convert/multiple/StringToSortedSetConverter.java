package com.chang.common.convert.multiple;

import java.util.SortedSet;
import java.util.TreeSet;

public class StringToSortedSetConverter extends StringToIterableConverter<SortedSet> {
   protected SortedSet createMultiValue(int size, Class<?> multiValueType) {
      return new TreeSet();
   }
}
