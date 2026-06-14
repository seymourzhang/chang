package com.chang.uclass.bytecode;

public class CheckUtils {
   public static <E> boolean isIn(E e, E... s) {
      if (null != s) {
         Object[] var2 = s;
         int var3 = s.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            E es = (E) var2[var4];
            if (isEquals(e, es)) {
               return true;
            }
         }
      }

      return false;
   }

   public static <E> boolean isEquals(E src, E target) {
      return null == src && null == target || null != src && null != target && src.equals(target);
   }
}
