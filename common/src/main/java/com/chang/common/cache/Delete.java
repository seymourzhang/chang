package com.chang.common.cache;

public interface Delete<K, V> {
   void expired(K var1, V var2);

   void explicit(K var1, V var2);

   void replaced(K var1, V var2);

   void size(K var1, V var2);
}
