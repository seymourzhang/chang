package com.chang.util.source.common;

public enum HbaseFindType {
   START_AND_END("START_AND_END"),
   ROW_KEY_PREFIX("ROW_KEY_PREFIX");

   private final String hbaseFindType;

   public String getHbaseFindType() {
      return this.hbaseFindType;
   }

   private HbaseFindType(String hbaseFindType) {
      this.hbaseFindType = hbaseFindType;
   }
}
