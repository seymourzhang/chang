package com.chang.common.convert.multiple;

import com.chang.common.convert.Converter;
import com.chang.common.convert.StringConverter;

import java.util.Collection;
import java.util.Optional;

public abstract class StringToIterableConverter<T extends Iterable> implements StringToMultiValueConverter {
   public final Object convert(String[] segments, int size, Class<?> multiValueType, Class<?> elementType) {
      Optional<StringConverter> stringConverter = this.getStringConverter(elementType);
      return stringConverter.map((converter) -> {
         T convertedObject = this.createMultiValue(size, multiValueType);
         if (!(convertedObject instanceof Collection)) {
            return convertedObject;
         } else {
            Collection collection = (Collection)convertedObject;

            for(int i = 0; i < size; ++i) {
               String segment = segments[i];
               Object element = converter.convert(segment);
               collection.add(element);
            }

            return collection;
         }
      }).orElse((Iterable) null);
   }

   protected abstract T createMultiValue(int var1, Class<?> var2);

   protected Optional<StringConverter> getStringConverter(Class<?> elementType) {
      StringConverter converter = (StringConverter)Converter.getConverter(String.class, elementType);
      return Optional.ofNullable(converter);
   }
}
