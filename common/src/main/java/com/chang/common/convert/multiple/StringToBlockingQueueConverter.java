package com.chang.common.convert.multiple;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StringToBlockingQueueConverter extends StringToIterableConverter<BlockingQueue> {
   protected BlockingQueue createMultiValue(int size, Class<?> multiValueType) {
      return new ArrayBlockingQueue(size);
   }
}
