package com.chang.common.convert;

import java.util.Optional;

public class StringToOptionalConverter implements StringConverter<Optional> {
   public Optional convert(String source) {
      return Optional.ofNullable(source);
   }

   public int getPriority() {
      return Integer.MAX_VALUE;
   }
}
