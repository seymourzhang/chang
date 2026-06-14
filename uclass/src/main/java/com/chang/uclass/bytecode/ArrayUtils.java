package com.chang.uclass.bytecode;

public class ArrayUtils {
   private ArrayUtils() {
   }

   public static boolean isEmpty(Object[] array) {
      return array == null || array.length == 0;
   }

   public static boolean isNotEmpty(Object[] array) {
      return !isEmpty(array);
   }

   public static boolean contains(String[] array, String valueToFind) {
      return indexOf(array, valueToFind, 0) != -1;
   }

   public static int indexOf(String[] array, String valueToFind, int startIndex) {
      if (!isEmpty(array) && valueToFind != null) {
         if (startIndex < 0) {
            startIndex = 0;
         }

         for(int i = startIndex; i < array.length; ++i) {
            if (valueToFind.equals(array[i])) {
               return i;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }
}
