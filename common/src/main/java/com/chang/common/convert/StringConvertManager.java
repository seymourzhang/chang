package com.chang.common.convert;

import com.chang.common.convert.multiple.MultiValueConverter;
import com.chang.common.convert.multiple.StringToArrayConverter;
import com.chang.common.convert.multiple.StringToBlockingDequeConverter;
import com.chang.common.convert.multiple.StringToBlockingQueueConverter;
import com.chang.common.convert.multiple.StringToCollectionConverter;
import com.chang.common.convert.multiple.StringToDequeConverter;
import com.chang.common.convert.multiple.StringToListConverter;
import com.chang.common.convert.multiple.StringToNavigableSetConverter;
import com.chang.common.convert.multiple.StringToQueueConverter;
import com.chang.common.convert.multiple.StringToSetConverter;
import com.chang.common.convert.multiple.StringToSortedSetConverter;
import com.chang.common.convert.multiple.StringToTransferQueueConverter;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TransferQueue;

public class StringConvertManager {
   public static Object getConverter(String source, Class<?> targetType) {
      Converter converter = null;
      if (targetType.isAssignableFrom(Boolean.class)) {
         converter = new StringToBooleanConverter();
      } else if (targetType.isAssignableFrom(String.class)) {
         converter = new StringToStringConverter();
      } else if (targetType.isAssignableFrom(Character.class)) {
         converter = new StringToCharacterConverter();
      } else if (targetType.isAssignableFrom(char[].class)) {
         converter = new StringToCharArrayConverter();
      } else if (targetType.isAssignableFrom(Double.class)) {
         converter = new StringToDoubleConverter();
      } else if (targetType.isAssignableFrom(Float.class)) {
         converter = new StringToFloatConverter();
      } else if (targetType.isAssignableFrom(Integer.class)) {
         converter = new StringToIntegerConverter();
      } else if (targetType.isAssignableFrom(Long.class)) {
         converter = new StringToLongConverter();
      } else if (targetType.isAssignableFrom(Short.class)) {
         converter = new StringToShortConverter();
      } else if (targetType.isAssignableFrom(Optional.class)) {
         converter = new StringToOptionalConverter();
      }

      return ((Converter)converter).convert(source);
   }

   public static Object getMultiConverter(String source, Class<?> targetType, Class<?> elementType) {
      MultiValueConverter converter = null;
      if (targetType.isArray()) {
         converter = new StringToArrayConverter();
      } else if (targetType.isAssignableFrom(Collection.class)) {
         converter = new StringToCollectionConverter();
      } else if (targetType.isAssignableFrom(BlockingQueue.class)) {
         converter = new StringToBlockingQueueConverter();
      } else if (targetType.isAssignableFrom(BlockingDeque.class)) {
         converter = new StringToBlockingDequeConverter();
      } else if (targetType.isAssignableFrom(Deque.class)) {
         converter = new StringToDequeConverter();
      } else if (targetType.isAssignableFrom(List.class)) {
         converter = new StringToListConverter();
      } else if (targetType.isAssignableFrom(NavigableSet.class)) {
         converter = new StringToNavigableSetConverter();
      } else if (targetType.isAssignableFrom(Queue.class)) {
         converter = new StringToQueueConverter();
      } else if (targetType.isAssignableFrom(Set.class)) {
         converter = new StringToSetConverter();
      } else if (targetType.isAssignableFrom(SortedSet.class)) {
         converter = new StringToSortedSetConverter();
      } else if (targetType.isAssignableFrom(TransferQueue.class)) {
         converter = new StringToTransferQueueConverter();
      }

      return ((MultiValueConverter)converter).convert(source, targetType, elementType);
   }
}
