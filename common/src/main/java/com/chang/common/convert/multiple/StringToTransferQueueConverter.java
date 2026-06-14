package com.chang.common.convert.multiple;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class StringToTransferQueueConverter extends StringToIterableConverter<TransferQueue> {
   protected TransferQueue createMultiValue(int size, Class<?> multiValueType) {
      return new LinkedTransferQueue();
   }
}
