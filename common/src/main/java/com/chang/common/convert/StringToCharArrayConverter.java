package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToCharArrayConverter implements StringConverter<char[]> {
   public char[] convert(String source) {
      return StringUtils.isNotEmpty(source) ? source.toCharArray() : null;
   }

   public int getPriority() {
      return 7;
   }
}
