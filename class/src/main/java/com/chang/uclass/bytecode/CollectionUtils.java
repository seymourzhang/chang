package com.chang.uclass.bytecode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {
   private static final Comparator<String> SIMPLE_NAME_COMPARATOR = new Comparator<String>() {
      public int compare(String s1, String s2) {
         if (s1 == null && s2 == null) {
            return 0;
         } else if (s1 == null) {
            return -1;
         } else if (s2 == null) {
            return 1;
         } else {
            int i1 = s1.lastIndexOf(46);
            if (i1 >= 0) {
               s1 = s1.substring(i1 + 1);
            }

            int i2 = s2.lastIndexOf(46);
            if (i2 >= 0) {
               s2 = s2.substring(i2 + 1);
            }

            return s1.compareToIgnoreCase(s2);
         }
      }
   };

   private CollectionUtils() {
   }

   public static <T> List<T> sort(List<T> list) {

      return list;
   }

   public static List<String> sortSimpleName(List<String> list) {
      if (list != null && list.size() > 0) {
         Collections.sort(list, SIMPLE_NAME_COMPARATOR);
      }

      return list;
   }

   public static Map<String, Map<String, String>> splitAll(Map<String, List<String>> list, String separator) {
      if (list == null) {
         return null;
      } else {
         Map<String, Map<String, String>> result = new HashMap();
         Iterator var3 = list.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry)var3.next();
            result.put(entry.getKey(), split((List)entry.getValue(), separator));
         }

         return result;
      }
   }

   public static Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator) {
      if (map == null) {
         return null;
      } else {
         Map<String, List<String>> result = new HashMap();
         Iterator var3 = map.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry<String, Map<String, String>> entry = (Map.Entry)var3.next();
            result.put(entry.getKey(), join((Map)entry.getValue(), separator));
         }

         return result;
      }
   }

   public static Map<String, String> split(List<String> list, String separator) {
      if (list == null) {
         return null;
      } else {
         Map<String, String> map = new HashMap();
         if (list.isEmpty()) {
            return map;
         } else {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               String item = (String)var3.next();
               int index = item.indexOf(separator);
               if (index == -1) {
                  map.put(item, "");
               } else {
                  map.put(item.substring(0, index), item.substring(index + 1));
               }
            }

            return map;
         }
      }
   }

   public static List<String> join(Map<String, String> map, String separator) {
      if (map == null) {
         return null;
      } else {
         List<String> list = new ArrayList();
         if (map.size() == 0) {
            return list;
         } else {
            Iterator var3 = map.entrySet().iterator();

            while(var3.hasNext()) {
               Map.Entry<String, String> entry = (Map.Entry)var3.next();
               String key = (String)entry.getKey();
               String value = (String)entry.getValue();
               if (StringUtils.isEmpty(value)) {
                  list.add(key);
               } else {
                  list.add(key + separator + value);
               }
            }

            return list;
         }
      }
   }

   public static String join(List<String> list, String separator) {
      StringBuilder sb = new StringBuilder();

      String ele;
      for(Iterator var3 = list.iterator(); var3.hasNext(); sb.append(ele)) {
         ele = (String)var3.next();
         if (sb.length() > 0) {
            sb.append(separator);
         }
      }

      return sb.toString();
   }

   public static boolean mapEquals(Map<?, ?> map1, Map<?, ?> map2) {
      if (map1 == null && map2 == null) {
         return true;
      } else if (map1 != null && map2 != null) {
         if (map1.size() != map2.size()) {
            return false;
         } else {
            Iterator var2 = map1.entrySet().iterator();

            Object value1;
            Object value2;
            do {
               if (!var2.hasNext()) {
                  return true;
               }

               Map.Entry<?, ?> entry = (Map.Entry)var2.next();
               Object key = entry.getKey();
               value1 = entry.getValue();
               value2 = map2.get(key);
            } while(objectEquals(value1, value2));

            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean objectEquals(Object obj1, Object obj2) {
      if (obj1 == null && obj2 == null) {
         return true;
      } else {
         return obj1 != null && obj2 != null ? obj1.equals(obj2) : false;
      }
   }

   public static Map<String, String> toStringMap(String... pairs) {
      Map<String, String> parameters = new HashMap();
      if (ArrayUtils.isEmpty(pairs)) {
         return parameters;
      } else {
         if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
               throw new IllegalArgumentException("pairs must be even.");
            }

            for(int i = 0; i < pairs.length; i += 2) {
               parameters.put(pairs[i], pairs[i + 1]);
            }
         }

         return parameters;
      }
   }

   public static <K, V> Map<K, V> toMap(Object... pairs) {
      Map<K, V> ret = new HashMap();
      if (pairs != null && pairs.length != 0) {
         if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
         } else {
            int len = pairs.length / 2;

            for(int i = 0; i < len; ++i) {
               ret.put((K) pairs[2 * i], (V) pairs[2 * i + 1]);
            }

            return ret;
         }
      } else {
         return ret;
      }
   }

   public static boolean isEmpty(Collection<?> collection) {
      return collection == null || collection.isEmpty();
   }

   public static boolean isNotEmpty(Collection<?> collection) {
      return !isEmpty(collection);
   }

   public static boolean isEmptyMap(Map map) {
      return map == null || map.size() == 0;
   }

   public static boolean isNotEmptyMap(Map map) {
      return !isEmptyMap(map);
   }

   public static <T> Set<T> ofSet(T... values) {
      int size = values == null ? 0 : values.length;
      if (size < 1) {
         return Collections.emptySet();
      } else {
         float loadFactor = 1.0F / ((float)(size + 1) * 1.0F);
         if (loadFactor > 0.75F) {
            loadFactor = 0.75F;
         }

         Set<T> elements = new LinkedHashSet(size, loadFactor);

         for(int i = 0; i < size; ++i) {
            elements.add(values[i]);
         }

         return Collections.unmodifiableSet(elements);
      }
   }

   public static int size(Collection<?> collection) {
      return collection == null ? 0 : collection.size();
   }

   public static boolean equals(Collection<?> one, Collection<?> another) {
      if (one == another) {
         return true;
      } else if (isEmpty(one) && isEmpty(another)) {
         return true;
      } else if (size(one) != size(another)) {
         return false;
      } else {
         try {
            return one.containsAll(another);
         } catch (ClassCastException var3) {
            return false;
         } catch (NullPointerException var4) {
            return false;
         }
      }
   }

   public static <T> int addAll(Collection<T> collection, T... values) {
      int size = values == null ? 0 : values.length;
      if (collection != null && size >= 1) {
         int effectedCount = 0;

         for(int i = 0; i < size; ++i) {
            if (collection.add(values[i])) {
               ++effectedCount;
            }
         }

         return effectedCount;
      } else {
         return 0;
      }
   }

   public static <T> T first(Collection<T> values) {
      if (isEmpty(values)) {
         return null;
      } else if (values instanceof List) {
         List<T> list = (List)values;
         return list.get(0);
      } else {
         return values.iterator().next();
      }
   }
}
