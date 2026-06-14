package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToIntegerConverter implements StringConverter<Integer> {
   public Integer convert(String source) {
      return StringUtils.isNotEmpty(source) ? Integer.valueOf(source) : null;
   }

   public int getPriority() {
      return 0;
   }
}
