package com.chang.common.convert;

import com.chang.common.StringUtils;

public class StringToCharacterConverter implements StringConverter<Character> {
   public Character convert(String source) {
      int length = StringUtils.length(source);
      if (length == 0) {
         return null;
      } else if (length > 1) {
         throw new IllegalArgumentException("The source String is more than one character!");
      } else {
         return source.charAt(0);
      }
   }

   public int getPriority() {
      return 8;
   }
}
