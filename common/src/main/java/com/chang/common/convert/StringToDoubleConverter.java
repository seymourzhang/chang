package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToDoubleConverter implements StringConverter<Double> {
   public Double convert(String source) {
      return StringUtils.isNotEmpty(source) ? Double.valueOf(source) : null;
   }

   public int getPriority() {
      return 3;
   }
}
