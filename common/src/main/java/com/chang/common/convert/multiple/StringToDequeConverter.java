package com.chang.common.convert.multiple;

import java.util.ArrayDeque;
import java.util.Deque;

public class StringToDequeConverter extends StringToIterableConverter<Deque> {
   protected Deque createMultiValue(int size, Class<?> multiValueType) {
      return new ArrayDeque(size);
   }
}
