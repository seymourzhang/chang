package com.chang.common.convert;

import com.chang.common.Prioritized;

import java.util.Optional;

@FunctionalInterface
public interface Converter<S, T> extends Prioritized {
   T convert(String var1);

   static Converter<?, ?> getConverter(Class<?> sourceType, Class<?> targetType) {
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
         converter = new StringToOptionalConverter();
      } else if (targetType.isAssignableFrom(Optional.class)) {
         converter = new StringToOptionalConverter();
      }

      return (Converter)converter;
   }
}
