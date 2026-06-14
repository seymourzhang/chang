package com.chang.common.store;

import java.util.Map;

public interface DataStore {
   Map<String, Object> get(String var1);

   Object get(String var1, String var2);

   void put(String var1, String var2, Object var3);

   void remove(String var1, String var2);
}
