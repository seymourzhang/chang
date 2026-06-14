package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToFloatConverter implements StringConverter<Float> {
   public Float convert(String source) {
      return StringUtils.isNotEmpty(source) ? Float.valueOf(source) : null;
   }

   public int getPriority() {
      return 4;
   }
}
