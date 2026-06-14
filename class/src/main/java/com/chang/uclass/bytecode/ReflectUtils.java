package com.chang.uclass.bytecode;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ReflectUtils {
   public static final char JVM_VOID = 'V';
   public static final char JVM_BOOLEAN = 'Z';
   public static final char JVM_BYTE = 'B';
   public static final char JVM_CHAR = 'C';
   public static final char JVM_DOUBLE = 'D';
   public static final char JVM_FLOAT = 'F';
   public static final char JVM_INT = 'I';
   public static final char JVM_LONG = 'J';
   public static final char JVM_SHORT = 'S';
   public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
   public static final String JAVA_IDENT_REGEX = "(?:[_$a-zA-Z][_$a-zA-Z0-9]*)";
   public static final String JAVA_NAME_REGEX = "(?:(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\.(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*)";
   public static final String CLASS_DESC = "(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)";
   public static final String ARRAY_DESC = "(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)))";
   public static final String DESC_REGEX = "(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;))))";
   public static final Pattern DESC_PATTERN = Pattern.compile("(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;))))");
   public static final String METHOD_DESC_REGEX = "(?:((?:[_$a-zA-Z][_$a-zA-Z0-9]*))?\\(((?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;))))*)\\)((?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)))))?)";
   public static final Pattern METHOD_DESC_PATTERN = Pattern.compile("(?:((?:[_$a-zA-Z][_$a-zA-Z0-9]*))?\\(((?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;))))*)\\)((?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)))))?)");
   public static final Pattern GETTER_METHOD_DESC_PATTERN = Pattern.compile("get([A-Z][_a-zA-Z0-9]*)\\(\\)((?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)))))");
   public static final Pattern SETTER_METHOD_DESC_PATTERN = Pattern.compile("set([A-Z][_a-zA-Z0-9]*)\\(((?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)))))\\)V");
   public static final Pattern IS_HAS_CAN_METHOD_DESC_PATTERN = Pattern.compile("(?:is|has|can)([A-Z][_a-zA-Z0-9]*)\\(\\)Z");
   private static final ConcurrentMap<String, Class<?>> DESC_CLASS_CACHE = new ConcurrentHashMap();
   private static final ConcurrentMap<String, Class<?>> NAME_CLASS_CACHE = new ConcurrentHashMap();
   private static final ConcurrentMap<String, Method> SIGNATURE_METHODS_CACHE = new ConcurrentHashMap();
   private static Map<Class<?>, Object> primitiveDefaults = new HashMap();

   private ReflectUtils() {
   }

   public static boolean isPrimitives(Class<?> cls) {
      return cls.isArray() ? isPrimitive(cls.getComponentType()) : isPrimitive(cls);
   }

   public static boolean isPrimitive(Class<?> cls) {
      return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
   }

   public static Class<?> getBoxedClass(Class<?> c) {
      if (c == Integer.TYPE) {
         c = Integer.class;
      } else if (c == Boolean.TYPE) {
         c = Boolean.class;
      } else if (c == Long.TYPE) {
         c = Long.class;
      } else if (c == Float.TYPE) {
         c = Float.class;
      } else if (c == Double.TYPE) {
         c = Double.class;
      } else if (c == Character.TYPE) {
         c = Character.class;
      } else if (c == Byte.TYPE) {
         c = Byte.class;
      } else if (c == Short.TYPE) {
         c = Short.class;
      }

      return c;
   }

   public static boolean isCompatible(Class<?> c, Object o) {
      boolean pt = c.isPrimitive();
      if (o == null) {
         return !pt;
      } else {
         if (pt) {
            c = getBoxedClass(c);
         }

         return c == o.getClass() || c.isInstance(o);
      }
   }

   public static boolean isCompatible(Class<?>[] cs, Object[] os) {
      int len = cs.length;
      if (len != os.length) {
         return false;
      } else if (len == 0) {
         return true;
      } else {
         for(int i = 0; i < len; ++i) {
            if (!isCompatible(cs[i], os[i])) {
               return false;
            }
         }

         return true;
      }
   }

   public static String getCodeBase(Class<?> cls) {
      if (cls == null) {
         return null;
      } else {
         ProtectionDomain domain = cls.getProtectionDomain();
         if (domain == null) {
            return null;
         } else {
            CodeSource source = domain.getCodeSource();
            if (source == null) {
               return null;
            } else {
               URL location = source.getLocation();
               return location == null ? null : location.getFile();
            }
         }
      }
   }

   public static String getName(Class<?> c) {
      if (!c.isArray()) {
         return c.getName();
      } else {
         StringBuilder sb = new StringBuilder();

         do {
            sb.append("[]");
            c = c.getComponentType();
         } while(c.isArray());

         return c.getName() + sb.toString();
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
         } else if (genericClass instanceof GenericArrayType) {
            return (Class)((GenericArrayType)genericClass).getGenericComponentType();
         } else {
            return ((Class)genericClass).isArray() ? ((Class)genericClass).getComponentType() : (Class)genericClass;
         }
      } catch (Throwable var4) {
         throw new IllegalArgumentException(cls.getName() + " generic type undefined!", var4);
      }
   }

   public static String getName(Method m) {
      StringBuilder ret = new StringBuilder();
      ret.append(getName(m.getReturnType())).append(' ');
      ret.append(m.getName()).append('(');
      Class<?>[] parameterTypes = m.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         if (i > 0) {
            ret.append(',');
         }

         ret.append(getName(parameterTypes[i]));
      }

      ret.append(')');
      return ret.toString();
   }

   public static String getSignature(String methodName, Class<?>[] parameterTypes) {
      StringBuilder sb = new StringBuilder(methodName);
      sb.append("(");
      if (parameterTypes != null && parameterTypes.length > 0) {
         boolean first = true;
         Class[] var4 = parameterTypes;
         int var5 = parameterTypes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class<?> type = var4[var6];
            if (first) {
               first = false;
            } else {
               sb.append(",");
            }

            sb.append(type.getName());
         }
      }

      sb.append(")");
      return sb.toString();
   }

   public static String getName(Constructor<?> c) {
      StringBuilder ret = new StringBuilder("(");
      Class<?>[] parameterTypes = c.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         if (i > 0) {
            ret.append(',');
         }

         ret.append(getName(parameterTypes[i]));
      }

      ret.append(')');
      return ret.toString();
   }

   public static String getDesc(Class<?> c) {
      StringBuilder ret;
      for(ret = new StringBuilder(); c.isArray(); c = c.getComponentType()) {
         ret.append('[');
      }

      if (c.isPrimitive()) {
         String t = c.getName();
         if ("void".equals(t)) {
            ret.append('V');
         } else if ("boolean".equals(t)) {
            ret.append('Z');
         } else if ("byte".equals(t)) {
            ret.append('B');
         } else if ("char".equals(t)) {
            ret.append('C');
         } else if ("double".equals(t)) {
            ret.append('D');
         } else if ("float".equals(t)) {
            ret.append('F');
         } else if ("int".equals(t)) {
            ret.append('I');
         } else if ("long".equals(t)) {
            ret.append('J');
         } else if ("short".equals(t)) {
            ret.append('S');
         }
      } else {
         ret.append('L');
         ret.append(c.getName().replace('.', '/'));
         ret.append(';');
      }

      return ret.toString();
   }

   public static String getDesc(Class<?>[] cs) {
      if (cs.length == 0) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder(64);
         Class[] var2 = cs;
         int var3 = cs.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> c = var2[var4];
            sb.append(getDesc(c));
         }

         return sb.toString();
      }
   }

   public static String getDesc(Method m) {
      StringBuilder ret = (new StringBuilder(m.getName())).append('(');
      Class<?>[] parameterTypes = m.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         ret.append(getDesc(parameterTypes[i]));
      }

      ret.append(')').append(getDesc(m.getReturnType()));
      return ret.toString();
   }

   public static String[] getDescArray(Method m) {
      Class<?>[] parameterTypes = m.getParameterTypes();
      String[] arr = new String[parameterTypes.length];

      for(int i = 0; i < parameterTypes.length; ++i) {
         arr[i] = getDesc(parameterTypes[i]);
      }

      return arr;
   }

   public static String getDesc(Constructor<?> c) {
      StringBuilder ret = new StringBuilder("(");
      Class<?>[] parameterTypes = c.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         ret.append(getDesc(parameterTypes[i]));
      }

      ret.append(')').append('V');
      return ret.toString();
   }

   public static String getDescWithoutMethodName(Method m) {
      StringBuilder ret = new StringBuilder();
      ret.append('(');
      Class<?>[] parameterTypes = m.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         ret.append(getDesc(parameterTypes[i]));
      }

      ret.append(')').append(getDesc(m.getReturnType()));
      return ret.toString();
   }

   public static String getDesc(CtClass c) throws NotFoundException {
      StringBuilder ret = new StringBuilder();
      if (c.isArray()) {
         ret.append('[');
         ret.append(getDesc(c.getComponentType()));
      } else if (c.isPrimitive()) {
         String t = c.getName();
         if ("void".equals(t)) {
            ret.append('V');
         } else if ("boolean".equals(t)) {
            ret.append('Z');
         } else if ("byte".equals(t)) {
            ret.append('B');
         } else if ("char".equals(t)) {
            ret.append('C');
         } else if ("double".equals(t)) {
            ret.append('D');
         } else if ("float".equals(t)) {
            ret.append('F');
         } else if ("int".equals(t)) {
            ret.append('I');
         } else if ("long".equals(t)) {
            ret.append('J');
         } else if ("short".equals(t)) {
            ret.append('S');
         }
      } else {
         ret.append('L');
         ret.append(c.getName().replace('.', '/'));
         ret.append(';');
      }

      return ret.toString();
   }

   public static String getDesc(CtMethod m) throws NotFoundException {
      StringBuilder ret = (new StringBuilder(m.getName())).append('(');
      CtClass[] parameterTypes = m.getParameterTypes();
      CtClass[] var3 = parameterTypes;
      int var4 = parameterTypes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CtClass parameterType = var3[var5];
         ret.append(getDesc(parameterType));
      }

      ret.append(')').append(getDesc(m.getReturnType()));
      return ret.toString();
   }

   public static String getDesc(CtConstructor c) throws NotFoundException {
      StringBuilder ret = new StringBuilder("(");
      CtClass[] parameterTypes = c.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         ret.append(getDesc(parameterTypes[i]));
      }

      ret.append(')').append('V');
      return ret.toString();
   }

   public static String getDescWithoutMethodName(CtMethod m) throws NotFoundException {
      StringBuilder ret = new StringBuilder();
      ret.append('(');
      CtClass[] parameterTypes = m.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         ret.append(getDesc(parameterTypes[i]));
      }

      ret.append(')').append(getDesc(m.getReturnType()));
      return ret.toString();
   }

   public static String name2desc(String name) {
      StringBuilder sb = new StringBuilder();
      int c = 0;
      int index = name.indexOf(91);
      if (index > 0) {
         c = (name.length() - index) / 2;
         name = name.substring(0, index);
      }

      while(c-- > 0) {
         sb.append("[");
      }

      if ("void".equals(name)) {
         sb.append('V');
      } else if ("boolean".equals(name)) {
         sb.append('Z');
      } else if ("byte".equals(name)) {
         sb.append('B');
      } else if ("char".equals(name)) {
         sb.append('C');
      } else if ("double".equals(name)) {
         sb.append('D');
      } else if ("float".equals(name)) {
         sb.append('F');
      } else if ("int".equals(name)) {
         sb.append('I');
      } else if ("long".equals(name)) {
         sb.append('J');
      } else if ("short".equals(name)) {
         sb.append('S');
      } else {
         sb.append('L').append(name.replace('.', '/')).append(';');
      }

      return sb.toString();
   }

   public static String desc2name(String desc) {
      StringBuilder sb = new StringBuilder();
      int c = desc.lastIndexOf(91) + 1;
      if (desc.length() == c + 1) {
         switch (desc.charAt(c)) {
            case 'B':
               sb.append("byte");
               break;
            case 'C':
               sb.append("char");
               break;
            case 'D':
               sb.append("double");
               break;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
               throw new RuntimeException();
            case 'F':
               sb.append("float");
               break;
            case 'I':
               sb.append("int");
               break;
            case 'J':
               sb.append("long");
               break;
            case 'S':
               sb.append("short");
               break;
            case 'V':
               sb.append("void");
               break;
            case 'Z':
               sb.append("boolean");
         }
      } else {
         sb.append(desc.substring(c + 1, desc.length() - 1).replace('/', '.'));
      }

      while(c-- > 0) {
         sb.append("[]");
      }

      return sb.toString();
   }

   public static Class<?> forName(String name) {
      try {
         return name2class(name);
      } catch (ClassNotFoundException var2) {
         throw new IllegalStateException("Not found class " + name + ", cause: " + var2.getMessage(), var2);
      }
   }

   public static Class<?> forName(ClassLoader cl, String name) {
      try {
         return name2class(cl, name);
      } catch (ClassNotFoundException var3) {
         throw new IllegalStateException("Not found class " + name + ", cause: " + var3.getMessage(), var3);
      }
   }

   public static Class<?> name2class(String name) throws ClassNotFoundException {
      return name2class(ClassUtils.getClassLoader(), name);
   }

   private static Class<?> name2class(ClassLoader cl, String name) throws ClassNotFoundException {
      int c = 0;
      int index = name.indexOf(91);
      if (index > 0) {
         c = (name.length() - index) / 2;
         name = name.substring(0, index);
      }

      if (c > 0) {
         StringBuilder sb = new StringBuilder();

         while(c-- > 0) {
            sb.append("[");
         }

         if ("void".equals(name)) {
            sb.append('V');
         } else if ("boolean".equals(name)) {
            sb.append('Z');
         } else if ("byte".equals(name)) {
            sb.append('B');
         } else if ("char".equals(name)) {
            sb.append('C');
         } else if ("double".equals(name)) {
            sb.append('D');
         } else if ("float".equals(name)) {
            sb.append('F');
         } else if ("int".equals(name)) {
            sb.append('I');
         } else if ("long".equals(name)) {
            sb.append('J');
         } else if ("short".equals(name)) {
            sb.append('S');
         } else {
            sb.append('L').append(name).append(';');
         }

         name = sb.toString();
      } else {
         if ("void".equals(name)) {
            return Void.TYPE;
         }

         if ("boolean".equals(name)) {
            return Boolean.TYPE;
         }

         if ("byte".equals(name)) {
            return Byte.TYPE;
         }

         if ("char".equals(name)) {
            return Character.TYPE;
         }

         if ("double".equals(name)) {
            return Double.TYPE;
         }

         if ("float".equals(name)) {
            return Float.TYPE;
         }

         if ("int".equals(name)) {
            return Integer.TYPE;
         }

         if ("long".equals(name)) {
            return Long.TYPE;
         }

         if ("short".equals(name)) {
            return Short.TYPE;
         }
      }

      if (cl == null) {
         cl = ClassUtils.getClassLoader();
      }

      Class<?> clazz = (Class)NAME_CLASS_CACHE.get(name);
      if (clazz == null) {
         clazz = Class.forName(name, true, cl);
         NAME_CLASS_CACHE.put(name, clazz);
      }

      return clazz;
   }

   public static Class<?> desc2class(String desc) throws ClassNotFoundException {
      return desc2class(ClassUtils.getClassLoader(), desc);
   }

   private static Class<?> desc2class(ClassLoader cl, String desc) throws ClassNotFoundException {
      switch (desc.charAt(0)) {
         case 'B':
            return Byte.TYPE;
         case 'C':
            return Character.TYPE;
         case 'D':
            return Double.TYPE;
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'W':
         case 'X':
         case 'Y':
         default:
            throw new ClassNotFoundException("Class not found: " + desc);
         case 'F':
            return Float.TYPE;
         case 'I':
            return Integer.TYPE;
         case 'J':
            return Long.TYPE;
         case 'L':
            desc = desc.substring(1, desc.length() - 1).replace('/', '.');
            break;
         case 'S':
            return Short.TYPE;
         case 'V':
            return Void.TYPE;
         case 'Z':
            return Boolean.TYPE;
         case '[':
            desc = desc.replace('/', '.');
      }

      if (cl == null) {
         cl = ClassUtils.getClassLoader();
      }

      Class<?> clazz = (Class)DESC_CLASS_CACHE.get(desc);
      if (clazz == null) {
         clazz = Class.forName(desc, true, cl);
         DESC_CLASS_CACHE.put(desc, clazz);
      }

      return clazz;
   }

   public static Class<?>[] desc2classArray(String desc) throws ClassNotFoundException {
      Class<?>[] ret = desc2classArray(ClassUtils.getClassLoader(), desc);
      return ret;
   }

   private static Class<?>[] desc2classArray(ClassLoader cl, String desc) throws ClassNotFoundException {
      if (desc.length() == 0) {
         return EMPTY_CLASS_ARRAY;
      } else {
         List<Class<?>> cs = new ArrayList();
         Matcher m = DESC_PATTERN.matcher(desc);

         while(m.find()) {
            cs.add(desc2class(cl, m.group()));
         }

         return (Class[])cs.toArray(EMPTY_CLASS_ARRAY);
      }
   }

   public static Method findMethodByMethodSignature(Class<?> clazz, String methodName, String[] parameterTypes) throws NoSuchMethodException, ClassNotFoundException, java.lang.NoSuchMethodException {
      String signature = clazz.getName() + "." + methodName;
      if (parameterTypes != null && parameterTypes.length > 0) {
         signature = signature + StringUtils.join(parameterTypes);
      }

      Method method = (Method)SIGNATURE_METHODS_CACHE.get(signature);
      if (method != null) {
         return method;
      } else {
         if (parameterTypes == null) {
            List<Method> finded = new ArrayList();
            Method[] var6 = clazz.getMethods();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Method m = var6[var8];
               if (m.getName().equals(methodName)) {
                  finded.add(m);
               }
            }

            if (finded.isEmpty()) {
               throw new NoSuchMethodException("No such method " + methodName + " in class " + clazz);
            }

            if (finded.size() > 1) {
               String msg = String.format("Not unique method for method name(%s) in class(%s), find %d methods.", methodName, clazz.getName(), finded.size());
               throw new IllegalStateException(msg);
            }

            method = (Method)finded.get(0);
         } else {
            Class<?>[] types = new Class[parameterTypes.length];

            for(int i = 0; i < parameterTypes.length; ++i) {
               types[i] = name2class(parameterTypes[i]);
            }

            method = clazz.getMethod(methodName, types);
         }

         SIGNATURE_METHODS_CACHE.put(signature, method);
         return method;
      }
   }

   public static Method findMethodByMethodName(Class<?> clazz, String methodName) throws NoSuchMethodException, ClassNotFoundException, java.lang.NoSuchMethodException {
      return findMethodByMethodSignature(clazz, methodName, (String[])null);
   }

   public static Constructor<?> findConstructor(Class<?> clazz, Class<?> paramType) throws NoSuchMethodException, java.lang.NoSuchMethodException {
      Constructor targetConstructor;
      try {
         targetConstructor = clazz.getConstructor(paramType);
      } catch (NoSuchMethodException | java.lang.NoSuchMethodException var9) {
         targetConstructor = null;
         Constructor<?>[] constructors = clazz.getConstructors();
         Constructor[] var5 = constructors;
         int var6 = constructors.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Constructor<?> constructor = var5[var7];
            if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].isAssignableFrom(paramType)) {
               targetConstructor = constructor;
               break;
            }
         }

         if (targetConstructor == null) {
            throw var9;
         }
      }

      return targetConstructor;
   }

   public static boolean isInstance(Object obj, String interfaceClazzName) {
      for(Class<?> clazz = obj.getClass(); clazz != null && !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
         Class<?>[] interfaces = clazz.getInterfaces();
         Class[] var4 = interfaces;
         int var5 = interfaces.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class<?> itf = var4[var6];
            if (itf.getName().equals(interfaceClazzName)) {
               return true;
            }
         }
      }

      return false;
   }

   public static Object getEmptyObject(Class<?> returnType) {
      return getEmptyObject(returnType, new HashMap(), 0);
   }

   private static Object getEmptyObject(Class<?> returnType, Map<Class<?>, Object> emptyInstances, int level) {
      if (level > 2) {
         return null;
      } else if (returnType == null) {
         return null;
      } else if (returnType != Boolean.TYPE && returnType != Boolean.class) {
         if (returnType != Character.TYPE && returnType != Character.class) {
            if (returnType != Byte.TYPE && returnType != Byte.class) {
               if (returnType != Short.TYPE && returnType != Short.class) {
                  if (returnType != Integer.TYPE && returnType != Integer.class) {
                     if (returnType != Long.TYPE && returnType != Long.class) {
                        if (returnType != Float.TYPE && returnType != Float.class) {
                           if (returnType != Double.TYPE && returnType != Double.class) {
                              if (returnType.isArray()) {
                                 return Array.newInstance(returnType.getComponentType(), 0);
                              } else if (returnType.isAssignableFrom(ArrayList.class)) {
                                 return new ArrayList(0);
                              } else if (returnType.isAssignableFrom(HashSet.class)) {
                                 return new HashSet(0);
                              } else if (returnType.isAssignableFrom(HashMap.class)) {
                                 return new HashMap(0);
                              } else if (String.class.equals(returnType)) {
                                 return "";
                              } else if (returnType.isInterface()) {
                                 return null;
                              } else {
                                 try {
                                    Object value = emptyInstances.get(returnType);
                                    if (value == null) {
                                       value = returnType.newInstance();
                                       emptyInstances.put(returnType, value);
                                    }

                                    for(Class<?> cls = value.getClass(); cls != null && cls != Object.class; cls = cls.getSuperclass()) {
                                       Field[] fields = cls.getDeclaredFields();
                                       Field[] var6 = fields;
                                       int var7 = fields.length;

                                       for(int var8 = 0; var8 < var7; ++var8) {
                                          Field field = var6[var8];
                                          if (!field.isSynthetic()) {
                                             Object property = getEmptyObject(field.getType(), emptyInstances, level + 1);
                                             if (property != null) {
                                                try {
                                                   if (!field.isAccessible()) {
                                                      field.setAccessible(true);
                                                   }

                                                   field.set(value, property);
                                                } catch (Throwable var12) {
                                                }
                                             }
                                          }
                                       }
                                    }

                                    return value;
                                 } catch (Throwable var13) {
                                    return null;
                                 }
                              }
                           } else {
                              return 0.0;
                           }
                        } else {
                           return 0.0F;
                        }
                     } else {
                        return 0L;
                     }
                  } else {
                     return 0;
                  }
               } else {
                  return Short.valueOf((short)0);
               }
            } else {
               return 0;
            }
         } else {
            return '\u0000';
         }
      } else {
         return false;
      }
   }

   public static Object defaultReturn(Method m) {
      return m.getReturnType().isPrimitive() ? primitiveDefaults.get(m.getReturnType()) : null;
   }

   public static Object defaultReturn(Class<?> classType) {
      return classType != null && classType.isPrimitive() ? primitiveDefaults.get(classType) : null;
   }

   public static boolean isBeanPropertyReadMethod(Method method) {
      return method != null && Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && method.getReturnType() != Void.TYPE && method.getDeclaringClass() != Object.class && method.getParameterTypes().length == 0 && (method.getName().startsWith("get") && method.getName().length() > 3 || method.getName().startsWith("is") && method.getName().length() > 2);
   }

   public static String getPropertyNameFromBeanReadMethod(Method method) {
      if (isBeanPropertyReadMethod(method)) {
         if (method.getName().startsWith("get")) {
            return method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
         }

         if (method.getName().startsWith("is")) {
            return method.getName().substring(2, 3).toLowerCase() + method.getName().substring(3);
         }
      }

      return null;
   }

   public static boolean isBeanPropertyWriteMethod(Method method) {
      return method != null && Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass() != Object.class && method.getParameterTypes().length == 1 && method.getName().startsWith("set") && method.getName().length() > 3;
   }

   public static String getPropertyNameFromBeanWriteMethod(Method method) {
      return isBeanPropertyWriteMethod(method) ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : null;
   }

   public static boolean isPublicInstanceField(Field field) {
      return Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && !field.isSynthetic();
   }

   public static Map<String, Field> getBeanPropertyFields(Class cl) {
      HashMap properties;
      for(properties = new HashMap(); cl != null; cl = cl.getSuperclass()) {
         Field[] fields = cl.getDeclaredFields();
         Field[] var3 = fields;
         int var4 = fields.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (!Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
               field.setAccessible(true);
               properties.put(field.getName(), field);
            }
         }
      }

      return properties;
   }

   public static Map<String, Method> getBeanPropertyReadMethods(Class cl) {
      HashMap properties;
      for(properties = new HashMap(); cl != null; cl = cl.getSuperclass()) {
         Method[] methods = cl.getDeclaredMethods();
         Method[] var3 = methods;
         int var4 = methods.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];
            if (isBeanPropertyReadMethod(method)) {
               method.setAccessible(true);
               String property = getPropertyNameFromBeanReadMethod(method);
               properties.put(property, method);
            }
         }
      }

      return properties;
   }

   public static Type[] getReturnTypes(Method method) {
      Class<?> returnType = method.getReturnType();
      Type genericReturnType = method.getGenericReturnType();
      if (Future.class.isAssignableFrom(returnType)) {
         if (genericReturnType instanceof ParameterizedType) {
            Type actualArgType = ((ParameterizedType)genericReturnType).getActualTypeArguments()[0];
            if (actualArgType instanceof ParameterizedType) {
               returnType = (Class)((ParameterizedType)actualArgType).getRawType();
               genericReturnType = actualArgType;
            } else {
               returnType = (Class)actualArgType;
               genericReturnType = returnType;
            }
         } else {
            returnType = null;
            genericReturnType = null;
         }
      }

      return new Type[]{returnType, (Type)genericReturnType};
   }

   public static Set<ParameterizedType> findParameterizedTypes(Class<?> sourceClass) {
      List<Type> genericTypes = new LinkedList(Arrays.asList(sourceClass.getGenericInterfaces()));
      genericTypes.add(sourceClass.getGenericSuperclass());
      Set<ParameterizedType> parameterizedTypes = (Set)genericTypes.stream().filter((type) -> {
         return type instanceof ParameterizedType;
      }).map((type) -> {
         return (ParameterizedType)ParameterizedType.class.cast(type);
      }).collect(Collectors.toSet());
      if (parameterizedTypes.isEmpty()) {
         genericTypes.stream().filter((type) -> {
            return type instanceof Class;
         }).map((type) -> {
            return (Class)Class.class.cast(type);
         }).forEach((superClass) -> {
            parameterizedTypes.addAll(findParameterizedTypes(superClass));
         });
      }

      return Collections.unmodifiableSet(parameterizedTypes);
   }

   public static <T> Set<Class<T>> findHierarchicalTypes(Class<?> sourceClass, Class<T> matchType) {
      if (sourceClass == null) {
         return Collections.emptySet();
      } else {
         Set<Class<T>> hierarchicalTypes = new LinkedHashSet();
         if (matchType.isAssignableFrom(sourceClass)) {
            hierarchicalTypes.add((Class<T>) sourceClass);
         }

         hierarchicalTypes.addAll(findHierarchicalTypes(sourceClass.getSuperclass(), matchType));
         return Collections.unmodifiableSet(hierarchicalTypes);
      }
   }

   public static <T> T getProperty(Object bean, String methodName) {
      Class<?> beanClass = bean.getClass();
      BeanInfo beanInfo = null;
      T propertyValue = null;

      try {
         beanInfo = Introspector.getBeanInfo(beanClass);
         propertyValue = (T) Stream.of(beanInfo.getMethodDescriptors()).filter((methodDescriptor) -> {
            return methodName.equals(methodDescriptor.getName());
         }).findFirst().map((method) -> {
            try {
               return method.getMethod().invoke(bean);
            } catch (Exception var3) {
               return null;
            }
         }).get();
      } catch (Exception var6) {
      }

      return propertyValue;
   }

   public static Class[] resolveTypes(Object... values) {
      if (ArrayUtils.isEmpty(values)) {
         return EMPTY_CLASS_ARRAY;
      } else {
         int size = values.length;
         Class[] types = new Class[size];

         for(int i = 0; i < size; ++i) {
            Object value = values[i];
            types[i] = value == null ? null : value.getClass();
         }

         return types;
      }
   }

   public static Set<Class<?>> getClasses(ClassLoader loader, String packname) {
      Set<Class<?>> classes = new LinkedHashSet();
      String packageName = packname;
      String packageDirName = packname.replace('.', '/');

      try {
         Enumeration<URL> dirs = loader.getResources(packageDirName);

         while(true) {
            while(dirs.hasMoreElements()) {
               URL url = (URL)dirs.nextElement();
               String protocol = url.getProtocol();
               if ("file".equals(protocol)) {
                  String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                  findAndAddClassesInPackageByFile(packageName, filePath, true, classes);
               } else if ("jar".equals(protocol)) {
                  try {
                     JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                     Enumeration<JarEntry> entries = jar.entries();

                     while(entries.hasMoreElements()) {
                        JarEntry entry = (JarEntry)entries.nextElement();
                        String name = entry.getName();
                        if (name.charAt(0) == '/') {
                           name = name.substring(1);
                        }

                        if (name.startsWith(packageDirName)) {
                           int idx = name.lastIndexOf(47);
                           if (idx != -1) {
                              packageName = name.substring(0, idx).replace('/', '.');
                           }

                           if (name.endsWith(".class") && !entry.isDirectory()) {
                              String className = name.substring(packageName.length() + 1, name.length() - 6);

                              try {
                                 classes.add(Class.forName(packageName + '.' + className));
                              } catch (ClassNotFoundException var15) {
                              }
                           }
                        }
                     }
                  } catch (IOException var16) {
                  }
               }
            }

            return classes;
         }
      } catch (IOException var17) {
         return classes;
      }
   }

   private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
      File dir = new File(packagePath);
      if (dir.exists() && dir.isDirectory()) {
         File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
               return recursive && file.isDirectory() || file.getName().endsWith(".class");
            }
         });
         if (dirfiles != null) {
            File[] var6 = dirfiles;
            int var7 = dirfiles.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               File file = var6[var8];
               if (file.isDirectory()) {
                  findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
               } else {
                  String className = file.getName().substring(0, file.getName().length() - 6);

                  try {
                     classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                  } catch (ClassNotFoundException var12) {
                  }
               }
            }
         }

      }
   }

   public static void set(Field field, Object value, Object target) throws IllegalArgumentException, IllegalAccessException {
      boolean isAccessible = field.isAccessible();

      try {
         field.setAccessible(true);
         field.set(target, value);
      } finally {
         field.setAccessible(isAccessible);
      }

   }

   public static Set<Field> getFields(Class<?> clazz) {
      Set<Field> fields = new LinkedHashSet();
      Class<?> parentClazz = clazz.getSuperclass();
      Collections.addAll(fields, clazz.getDeclaredFields());
      if (null != parentClazz) {
         fields.addAll(getFields(parentClazz));
      }

      return fields;
   }

   public static Field getField(Class<?> clazz, String name) {
      Iterator var2 = getFields(clazz).iterator();

      Field field;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         field = (Field)var2.next();
      } while(!CheckUtils.isEquals(field.getName(), name));

      return field;
   }

   public static <T> T getFieldValueByField(Object target, Field field) throws IllegalArgumentException, IllegalAccessException {
      boolean isAccessible = field.isAccessible();

      Object var3;
      try {
         field.setAccessible(true);
         var3 = field.get(target);
      } finally {
         field.setAccessible(isAccessible);
      }

      return (T) var3;
   }

   public static <T> T valueOf(Class<T> t, String value) {
      if (CheckUtils.isIn(t, Integer.TYPE, Integer.class)) {
         return (T) Integer.valueOf(value);
      } else if (CheckUtils.isIn(t, Long.TYPE, Long.class)) {
         return (T) Long.valueOf(value);
      } else if (CheckUtils.isIn(t, Double.TYPE, Double.class)) {
         return (T) Double.valueOf(value);
      } else if (CheckUtils.isIn(t, Float.TYPE, Float.class)) {
         return (T) Float.valueOf(value);
      } else if (CheckUtils.isIn(t, Byte.TYPE, Byte.class)) {
         return (T) Byte.valueOf(value);
      } else if (CheckUtils.isIn(t, Boolean.TYPE, Boolean.class)) {
         return (T) Boolean.valueOf(value);
      } else if (CheckUtils.isIn(t, Short.TYPE, Short.class)) {
         return (T) Short.valueOf(value);
      } else {
         return CheckUtils.isIn(t, String.class) ? (T) value : null;
      }
   }

   public static Class<?> defineClass(ClassLoader targetClassLoader, String className, byte[] classByteArray) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, java.lang.NoSuchMethodException {
      Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
      synchronized(defineClassMethod) {
         boolean acc = defineClassMethod.isAccessible();

         Class var6;
         try {
            defineClassMethod.setAccessible(true);
            var6 = (Class)defineClassMethod.invoke(targetClassLoader, className, classByteArray, 0, classByteArray.length);
         } finally {
            defineClassMethod.setAccessible(acc);
         }

         return var6;
      }
   }

   static {
      primitiveDefaults.put(Integer.TYPE, 0);
      primitiveDefaults.put(Long.TYPE, 0L);
      primitiveDefaults.put(Byte.TYPE, (byte)0);
      primitiveDefaults.put(Character.TYPE, '\u0000');
      primitiveDefaults.put(Short.TYPE, Short.valueOf((short)0));
      primitiveDefaults.put(Float.TYPE, 0.0F);
      primitiveDefaults.put(Double.TYPE, 0.0);
      primitiveDefaults.put(Boolean.TYPE, false);
      primitiveDefaults.put(Void.TYPE, (Object)null);
   }
}
