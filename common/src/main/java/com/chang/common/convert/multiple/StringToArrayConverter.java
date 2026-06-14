package com.chang.common.convert.multiple;

import com.chang.common.convert.Converter;

import java.lang.reflect.Array;

public class StringToArrayConverter implements StringToMultiValueConverter {
   public Object convert(String[] segments, int size, Class<?> targetType, Class<?> elementType) {
      Class<?> componentType = targetType.getComponentType();
      Converter converter = Converter.getConverter(String.class, componentType);
      Object array = Array.newInstance(componentType, size);

      for(int i = 0; i < size; ++i) {
         Array.set(array, i, converter.convert(segments[i]));
      }

      return array;
   }

   public int getPriority() {
      return Integer.MAX_VALUE;
   }
}
