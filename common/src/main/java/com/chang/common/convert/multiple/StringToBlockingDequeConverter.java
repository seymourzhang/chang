package com.chang.common.convert.multiple;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class StringToBlockingDequeConverter extends StringToIterableConverter<BlockingDeque> {
   protected BlockingDeque createMultiValue(int size, Class<?> multiValueType) {
      return new LinkedBlockingDeque(size);
   }
}
