package com.chang.common.convert.multiple;

import java.util.ArrayList;
import java.util.List;

public class StringToListConverter extends StringToIterableConverter<List> {
   protected List createMultiValue(int size, Class<?> multiValueType) {
      return new ArrayList(size);
   }
}
