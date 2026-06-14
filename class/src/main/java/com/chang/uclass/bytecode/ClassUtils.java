package com.chang.uclass.bytecode;

import com.chang.uclass.bytecode.CollectionUtils;
import com.chang.uclass.bytecode.StringUtils;
import com.chang.uclass.bytecode.UtilClassloader;
import com.chang.uclass.bytecode.function.Streams;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtils {
   private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);
   public static final String CLASS_EXTENSION = ".class";
   public static final String JAVA_EXTENSION = ".java";
   private static final int JIT_LIMIT = 5120;
   public static final String ARRAY_SUFFIX = "[]";
   private static final String INTERNAL_ARRAY_PREFIX = "[L";
   private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new HashMap(32);
   private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new HashMap(16);
   public static final Set<Class<?>> SIMPLE_TYPES = CollectionUtils.ofSet(Void.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class, BigDecimal.class, BigInteger.class, Date.class, Object.class);
   private static final char PACKAGE_SEPARATOR_CHAR = '.';

   public static Class<?> forNameWithThreadContextClassLoader(String name) throws ClassNotFoundException {
      return forName(name, Thread.currentThread().getContextClassLoader());
   }

   public static Class<?> forNameWithCallerClassLoader(String name, Class<?> caller) throws ClassNotFoundException {
      return forName(name, caller.getClassLoader());
   }

   public static ClassLoader getCallerClassLoader(Class<?> caller) {
      return caller.getClassLoader();
   }

   public static ClassLoader getClassLoader(Class<?> clazz) {
      ClassLoader cl = null;

      try {
         cl = Thread.currentThread().getContextClassLoader();
      } catch (Throwable var4) {
      }

      if (cl == null) {
         cl = clazz.getClassLoader();
         if (cl == null) {
            try {
               cl = ClassLoader.getSystemClassLoader();
            } catch (Throwable var3) {
            }
         }
      }

      return cl;
   }

   public static ClassLoader getClassLoader() {
      return getClassLoader(ClassUtils.class);
   }

   public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
      Class<?> clazz = resolvePrimitiveClassName(name);
      if (clazz != null) {
         return clazz;
      } else if (name.endsWith("[]")) {
         String elementClassName = name.substring(0, name.length() - "[]".length());
         Class<?> elementClass = forName(elementClassName, classLoader);
         return Array.newInstance(elementClass, 0).getClass();
      } else {
         int internalArrayMarker = name.indexOf("[L");
         if (internalArrayMarker != -1 && name.endsWith(";")) {
            String elementClassName = null;
            if (internalArrayMarker == 0) {
               elementClassName = name.substring("[L".length(), name.length() - 1);
            } else if (name.startsWith("[")) {
               elementClassName = name.substring(1);
            }

            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
         } else {
            ClassLoader classLoaderToUse = classLoader;
            if (classLoader == null) {
               classLoaderToUse = getClassLoader();
            }

            return classLoaderToUse.loadClass(name);
         }
      }
   }

   public static Class<?> resolvePrimitiveClassName(String name) {
      Class<?> result = null;
      if (name != null && name.length() <= 8) {
         result = (Class)PRIMITIVE_TYPE_NAME_MAP.get(name);
      }

      return result;
   }

   public static String toShortString(Object obj) {
      return obj == null ? "null" : obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj);
   }

   public static String simpleClassName(Class<?> clazz) {
      if (clazz == null) {
         throw new NullPointerException("clazz");
      } else {
         String className = clazz.getName();
         int lastDotIdx = className.lastIndexOf(46);
         return lastDotIdx > -1 ? className.substring(lastDotIdx + 1) : className;
      }
   }

   /** @deprecated */
   public static boolean isPrimitive(Class<?> type) {
      return type != null && (type.isPrimitive() || isSimpleType(type));
   }

   public static boolean isSimpleType(Class<?> type) {
      return SIMPLE_TYPES.contains(type);
   }

   public static Object convertPrimitive(Class<?> type, String value) {
      if (value == null) {
         return null;
      } else if (type != Character.TYPE && type != Character.class) {
         if (type != Boolean.TYPE && type != Boolean.class) {
            try {
               if (type != Byte.TYPE && type != Byte.class) {
                  if (type != Short.TYPE && type != Short.class) {
                     if (type != Integer.TYPE && type != Integer.class) {
                        if (type != Long.TYPE && type != Long.class) {
                           if (type != Float.TYPE && type != Float.class) {
                              return type != Double.TYPE && type != Double.class ? value : Double.valueOf(value);
                           } else {
                              return Float.valueOf(value);
                           }
                        } else {
                           return Long.valueOf(value);
                        }
                     } else {
                        return Integer.valueOf(value);
                     }
                  } else {
                     return Short.valueOf(value);
                  }
               } else {
                  return Byte.valueOf(value);
               }
            } catch (NumberFormatException var3) {
               return null;
            }
         } else {
            return Boolean.valueOf(value);
         }
      } else {
         return value.length() > 0 ? value.charAt(0) : '\u0000';
      }
   }

   public static boolean isTypeMatch(Class<?> type, String value) {
      return type != Boolean.TYPE && type != Boolean.class || "true".equals(value) || "false".equals(value);
   }

   public static Set<Class<?>> getAllSuperClasses(Class<?> type, Predicate<Class<?>>... classFilters) {
      Set<Class<?>> allSuperClasses = new LinkedHashSet();

      for(Class<?> superClass = type.getSuperclass(); superClass != null; superClass = superClass.getSuperclass()) {
         allSuperClasses.add(superClass);
      }

      return Collections.unmodifiableSet((Set)Streams.filterAll(allSuperClasses, classFilters));
   }

   public static Set<Class<?>> getAllInterfaces(Class<?> type, Predicate<Class<?>>... interfaceFilters) {
      if (type != null && !type.isPrimitive()) {
         Set<Class<?>> allInterfaces = new LinkedHashSet();
         Set<Class<?>> resolved = new LinkedHashSet();
         Queue<Class<?>> waitResolve = new LinkedList();
         resolved.add(type);

         return (Set)Streams.filterAll(allInterfaces, interfaceFilters);
      } else {
         return Collections.emptySet();
      }
   }

   public static Set<Class<?>> getAllInheritedTypes(Class<?> type, Predicate<Class<?>>... typeFilters) {
      Set<Class<?>> types = new LinkedHashSet(getAllSuperClasses(type, typeFilters));
      types.addAll(getAllInterfaces(type, typeFilters));
      return Collections.unmodifiableSet(types);
   }

   public static boolean isAssignableFrom(Class<?> superType, Class<?> targetType) {
      if (superType != null && targetType != null) {
         return Objects.equals(superType, targetType) ? true : superType.isAssignableFrom(targetType);
      } else {
         return false;
      }
   }

   public static boolean isPresent(String className, ClassLoader classLoader) {
      try {
         forName(className, classLoader);
         return true;
      } catch (Throwable var3) {
         return false;
      }
   }

   public static Class<?> resolveClass(String className, ClassLoader classLoader) {
      Class<?> targetClass = null;

      try {
         targetClass = forName(className, classLoader);
      } catch (Throwable var4) {
      }

      return targetClass;
   }

   public static boolean isGenericClass(Class<?> type) {
      return type != null && !Void.TYPE.equals(type) && !Void.class.equals(type);
   }

   public static Object newInstance(String name) {
      try {
         return forName(name).newInstance();
      } catch (InstantiationException var2) {
         throw new IllegalStateException(var2.getMessage(), var2);
      } catch (IllegalAccessException var3) {
         throw new IllegalStateException(var3.getMessage(), var3);
      }
   }

   public static Object newInstance(Class<?> clazz) {
      try {
         return clazz.newInstance();
      } catch (InstantiationException var2) {
         throw new IllegalStateException(var2.getMessage(), var2);
      } catch (IllegalAccessException var3) {
         throw new IllegalStateException(var3.getMessage(), var3);
      }
   }

   public static Class<?> forName(String[] packages, String className) {
      try {
         return classForName(className);
      } catch (ClassNotFoundException var9) {
         if (packages != null && packages.length > 0) {
            String[] var3 = packages;
            int var4 = packages.length;
            int var5 = 0;

            while(var5 < var4) {
               String pkg = var3[var5];

               try {
                  return classForName(pkg + "." + className);
               } catch (ClassNotFoundException var8) {
                  ++var5;
               }
            }
         }

         throw new IllegalStateException(var9.getMessage(), var9);
      }
   }

   public static Class<?> forName(String className) {
      try {
         return classForName(className);
      } catch (ClassNotFoundException var2) {
         throw new IllegalStateException(var2.getMessage(), var2);
      }
   }

   public static Class<?> classForName(String className) throws ClassNotFoundException {
      switch (className) {
         case "boolean":
            return Boolean.TYPE;
         case "byte":
            return Byte.TYPE;
         case "char":
            return Character.TYPE;
         case "short":
            return Short.TYPE;
         case "int":
            return Integer.TYPE;
         case "long":
            return Long.TYPE;
         case "float":
            return Float.TYPE;
         case "double":
            return Double.TYPE;
         case "boolean[]":
            return boolean[].class;
         case "byte[]":
            return byte[].class;
         case "char[]":
            return char[].class;
         case "short[]":
            return short[].class;
         case "int[]":
            return int[].class;
         case "long[]":
            return long[].class;
         case "float[]":
            return float[].class;
         case "double[]":
            return double[].class;
         default:
            try {
               return arrayForName(className);
            } catch (ClassNotFoundException var4) {
               if (className.indexOf(46) == -1) {
                  try {
                     return arrayForName("java.lang." + className);
                  } catch (ClassNotFoundException var3) {
                  }
               }

               throw var4;
            }
      }
   }

   private static Class<?> arrayForName(String className) throws ClassNotFoundException {
      return Class.forName(className.endsWith("[]") ? "[L" + className.substring(0, className.length() - 2) + ";" : className, true, Thread.currentThread().getContextClassLoader());
   }

   public static Class<?> getBoxedClass(Class<?> type) {
      if (type == Boolean.TYPE) {
         return Boolean.class;
      } else if (type == Character.TYPE) {
         return Character.class;
      } else if (type == Byte.TYPE) {
         return Byte.class;
      } else if (type == Short.TYPE) {
         return Short.class;
      } else if (type == Integer.TYPE) {
         return Integer.class;
      } else if (type == Long.TYPE) {
         return Long.class;
      } else if (type == Float.TYPE) {
         return Float.class;
      } else {
         return type == Double.TYPE ? Double.class : type;
      }
   }

   public static Boolean boxed(boolean v) {
      return v;
   }

   public static Character boxed(char v) {
      return v;
   }

   public static Byte boxed(byte v) {
      return v;
   }

   public static Short boxed(short v) {
      return v;
   }

   public static Integer boxed(int v) {
      return v;
   }

   public static Long boxed(long v) {
      return v;
   }

   public static Float boxed(float v) {
      return v;
   }

   public static Double boxed(double v) {
      return v;
   }

   public static Object boxed(Object v) {
      return v;
   }

   public static boolean unboxed(Boolean v) {
      return v == null ? false : v;
   }

   public static char unboxed(Character v) {
      return v == null ? '\u0000' : v;
   }

   public static byte unboxed(Byte v) {
      return v == null ? 0 : v;
   }

   public static short unboxed(Short v) {
      return v == null ? 0 : v;
   }

   public static int unboxed(Integer v) {
      return v == null ? 0 : v;
   }

   public static long unboxed(Long v) {
      return v == null ? 0L : v;
   }

   public static float unboxed(Float v) {
      return v == null ? 0.0F : v;
   }

   public static double unboxed(Double v) {
      return v == null ? 0.0 : v;
   }

   public static Object unboxed(Object v) {
      return v;
   }

   public static boolean isNotEmpty(Object object) {
      return getSize(object) > 0;
   }

   public static int getSize(Object object) {
      if (object == null) {
         return 0;
      } else if (object instanceof Collection) {
         return ((Collection)object).size();
      } else if (object instanceof Map) {
         return ((Map)object).size();
      } else {
         return object.getClass().isArray() ? Array.getLength(object) : -1;
      }
   }

   public static URI toURI(String name) {
      try {
         return new URI(name);
      } catch (URISyntaxException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static Class<?> getGenericClass(Class<?> cls) {
      return getGenericClass(cls, 0);
   }

   public static Class<?> getGenericClass(Class<?> cls, int i) {
      try {
         ParameterizedType parameterizedType = (ParameterizedType)cls.getGenericInterfaces()[0];
         Object genericClass = parameterizedType.getActualTypeArguments()[i];
         if (genericClass instanceof ParameterizedType) {
            return (Class)((ParameterizedType)genericClass).getRawType();
         }

         if (genericClass instanceof GenericArrayType) {
            Type type = ((GenericArrayType)genericClass).getGenericComponentType();
            if (type instanceof TypeVariable) {
               return type.getClass();
            }

            return ((GenericArrayType)genericClass).getGenericComponentType() instanceof Class ? (Class)((GenericArrayType)genericClass).getGenericComponentType() : ((GenericArrayType)genericClass).getGenericComponentType().getClass();
         }

         if (genericClass != null) {
            if (genericClass instanceof TypeVariable) {
               return genericClass.getClass();
            }

            return (Class)genericClass;
         }
      } catch (Throwable var5) {
      }

      if (cls.getSuperclass() != null) {
         return getGenericClass(cls.getSuperclass(), i);
      } else {
         throw new IllegalArgumentException(cls.getName() + " generic type undefined!");
      }
   }

   public static boolean isBeforeJava5(String javaVersion) {
      return StringUtils.isEmpty(javaVersion) || "1.0".equals(javaVersion) || "1.1".equals(javaVersion) || "1.2".equals(javaVersion) || "1.3".equals(javaVersion) || "1.4".equals(javaVersion);
   }

   public static boolean isBeforeJava6(String javaVersion) {
      return isBeforeJava5(javaVersion) || "1.5".equals(javaVersion);
   }

   public static String toString(Throwable e) {
      StringWriter w = new StringWriter();
      PrintWriter p = new PrintWriter(w);
      p.print(e.getClass().getName() + ": ");
      if (e.getMessage() != null) {
         p.print(e.getMessage() + "\n");
      }

      p.println();

      String var3;
      try {
         e.printStackTrace(p);
         var3 = w.toString();
      } finally {
         p.close();
      }

      return var3;
   }

   public static void checkBytecode(String name, byte[] bytecode) {
      if (bytecode.length > 5120) {
         System.err.println("The template bytecode too long, may be affect the JIT compiler. template class: " + name);
      }

   }

   public static String getSizeMethod(Class<?> cls) {
      try {
         return cls.getMethod("size").getName() + "()";
      } catch (NoSuchMethodException var8) {
         try {
            return cls.getMethod("length").getName() + "()";
         } catch (NoSuchMethodException | java.lang.NoSuchMethodException var7) {
            try {
               return cls.getMethod("getSize").getName() + "()";
            } catch (NoSuchMethodException | java.lang.NoSuchMethodException var6) {
               try {
                  return cls.getMethod("getLength").getName() + "()";
               } catch (NoSuchMethodException | java.lang.NoSuchMethodException var5) {
                  return null;
               }
            }
         }
      } catch (java.lang.NoSuchMethodException e) {
          throw new RuntimeException(e);
      }
   }

   public static String getMethodName(Method method, Class<?>[] parameterClasses, String rightCode) {
      if (method.getParameterTypes().length > parameterClasses.length) {
         Class<?>[] types = method.getParameterTypes();
         StringBuilder buf = new StringBuilder(rightCode);

         for(int i = parameterClasses.length; i < types.length; ++i) {
            if (buf.length() > 0) {
               buf.append(",");
            }

            Class<?> type = types[i];
            String def;
            if (type == Boolean.TYPE) {
               def = "false";
            } else if (type == Character.TYPE) {
               def = "'\\0'";
            } else if (type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE && type != Float.TYPE && type != Double.TYPE) {
               def = "null";
            } else {
               def = "0";
            }

            buf.append(def);
         }
      }

      return method.getName() + "(" + rightCode + ")";
   }

   public static Method searchMethod(Class<?> currentClass, String name, Class<?>[] parameterTypes) throws NoSuchMethodException, java.lang.NoSuchMethodException {
      if (currentClass == null) {
         throw new NoSuchMethodException("class == null");
      } else {
         try {
            return currentClass.getMethod(name, parameterTypes);
         } catch (NoSuchMethodException | java.lang.NoSuchMethodException var11) {
            Method[] var4 = currentClass.getMethods();
            int var5 = var4.length;
            int var6 = 0;

            Method method;
            while(true) {
               if (var6 >= var5) {
                  throw var11;
               }

               method = var4[var6];
               if (method.getName().equals(name) && parameterTypes.length == method.getParameterTypes().length && Modifier.isPublic(method.getModifiers())) {
                  if (parameterTypes.length <= 0) {
                     break;
                  }

                  Class<?>[] types = method.getParameterTypes();
                  boolean match = true;

                  for(int i = 0; i < parameterTypes.length; ++i) {
                     if (!types[i].isAssignableFrom(parameterTypes[i])) {
                        match = false;
                        break;
                     }
                  }

                  if (match) {
                     break;
                  }
               }

               ++var6;
            }

            return method;
         }
      }
   }

   public static String getInitCode(Class<?> type) {
      if (!Byte.TYPE.equals(type) && !Short.TYPE.equals(type) && !Integer.TYPE.equals(type) && !Long.TYPE.equals(type) && !Float.TYPE.equals(type) && !Double.TYPE.equals(type)) {
         if (Character.TYPE.equals(type)) {
            return "'\\0'";
         } else {
            return Boolean.TYPE.equals(type) ? "false" : "null";
         }
      } else {
         return "0";
      }
   }

   public static <K, V> Map<K, V> toMap(Map.Entry<K, V>[] entries) {
      Map<K, V> map = new HashMap();
      if (entries != null && entries.length > 0) {
         Map.Entry[] var2 = entries;
         int var3 = entries.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Map.Entry<K, V> entry = var2[var4];
            map.put(entry.getKey(), entry.getValue());
         }
      }

      return map;
   }

   public static String getSimpleClassName(String qualifiedName) {
      if (null == qualifiedName) {
         return null;
      } else {
         int i = qualifiedName.lastIndexOf(46);
         return i < 0 ? qualifiedName : qualifiedName.substring(i + 1);
      }
   }

   public static String getGetName(String fieldName) {
      return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
   }

   public static String getSetName(String fieldName) {
      return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
   }

   public static URLClassLoader classLoadFromFile(String filePath, String fileName) throws MalformedURLException {
      File jarFile = new File(filePath, fileName);
      if (!jarFile.exists()) {
         throw new IllegalStateException("can not find file");
      } else {
         UtilClassloader classLoader = new UtilClassloader(new URL[]{jarFile.toURI().toURL()});
         return classLoader;
      }
   }

   private static String encodeArg(String arg) {
      try {
         return URLEncoder.encode(arg, "utf-8");
      } catch (UnsupportedEncodingException var2) {
         return arg;
      }
   }

   static {
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, Boolean.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, Byte.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, Character.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, Double.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, Float.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, Integer.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, Long.TYPE);
      PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, Short.TYPE);
      Set<Class<?>> primitiveTypeNames = new HashSet(32);
      primitiveTypeNames.addAll(PRIMITIVE_WRAPPER_TYPE_MAP.values());
      primitiveTypeNames.addAll(Arrays.asList(boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class));
      Iterator var1 = primitiveTypeNames.iterator();

      while(var1.hasNext()) {
         Class<?> primitiveTypeName = (Class)var1.next();
         PRIMITIVE_TYPE_NAME_MAP.put(primitiveTypeName.getName(), primitiveTypeName);
      }

   }
}
