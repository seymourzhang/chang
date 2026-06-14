package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToBooleanConverter implements StringConverter<Boolean> {
   public Boolean convert(String source) {
      return StringUtils.isNotEmpty(source) ? Boolean.valueOf(source) : null;
   }

   public int getPriority() {
      return 5;
   }
}
