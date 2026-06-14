package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToShortConverter implements StringConverter<Short> {
   public Short convert(String source) {
      return StringUtils.isNotEmpty(source) ? Short.valueOf(source) : null;
   }

   public int getPriority() {
      return 2;
   }
}
