package com.chang.common.convert.multiple;

import java.util.ArrayDeque;
import java.util.Queue;

public class StringToQueueConverter extends StringToIterableConverter<Queue> {
   protected Queue createMultiValue(int size, Class<?> multiValueType) {
      return new ArrayDeque(size);
   }
}
