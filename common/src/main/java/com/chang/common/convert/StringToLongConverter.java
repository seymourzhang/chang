package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToLongConverter implements StringConverter<Long> {
   public Long convert(String source) {
      return StringUtils.isNotEmpty(source) ? Long.valueOf(source) : null;
   }

   public int getPriority() {
      return 1;
   }
}
