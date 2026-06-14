package com.chang.common.convert.multiple;

import com.chang.common.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public interface StringToMultiValueConverter extends MultiValueConverter<String> {
   default Object convert(String source, Class<?> multiValueType, Class<?> elementType) {
      if (StringUtils.isEmpty(source)) {
         return null;
      } else {
         String[] segments = StringUtils.split(source, ',');
         if (ArrayUtils.isEmpty(segments)) {
            segments = new String[]{source};
         }

         int size = segments.length;
         return this.convert(segments, size, multiValueType, elementType);
      }
   }

   Object convert(String[] var1, int var2, Class<?> var3, Class<?> var4);
}
