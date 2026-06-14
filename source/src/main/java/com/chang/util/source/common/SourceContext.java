package com.chang.util.source.common;

import java.util.HashMap;
import java.util.Map;

public class SourceContext {
   private static final ThreadLocal<Map<String, Object>> keyNameContext = new ThreadLocal();

   public static Map<String, Object> getExParm() {
      return (Map)keyNameContext.get();
   }

   public static void setExParm(Map<String, Object> parm) {
      keyNameContext.set(new HashMap(parm));
   }

   public static void clearExParm() {
      keyNameContext.remove();
   }
}
