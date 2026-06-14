package com.chang.common.convert.multiple;

import java.util.NavigableSet;
import java.util.TreeSet;

public class StringToNavigableSetConverter extends StringToIterableConverter<NavigableSet> {
   protected NavigableSet createMultiValue(int size, Class<?> multiValueType) {
      return new TreeSet();
   }
}
