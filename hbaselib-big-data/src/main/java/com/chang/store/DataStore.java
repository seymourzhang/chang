package com.chang.store;

import java.util.List;

public interface DataStore {
   void add(String var1, String var2, byte[] var3);

   void add(String var1, String var2, String var3);

   Object get(String var1, String var2);

   void remove(String var1, String var2);

   <T> T get(String var1, Class<T> var2);

   <T> List<T> get(Class<T> var1);
}
