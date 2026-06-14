package com.chang.until.reflect;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chang.fileutil.FileUtil;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class ReflectionUtils extends ClassLoader {
   public static String getPackage(Class<?> clazz) {
      Package pck = clazz.getPackage();
      return null != pck ? pck.getName() : null;
   }

   public static String getSuperClassName(Class<?> clazz) {
      Class<?> superClass = clazz.getSuperclass();
      return null != superClass ? superClass.getName() : null;
   }

   public static List<String> getInterfaces(Class<?> clazz) {
      Class<?>[] iinterfaces = clazz.getInterfaces();
      List<String> list = new ArrayList();
      Class[] var3 = iinterfaces;
      int var4 = iinterfaces.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class<?> iinterface = var3[var5];
         String interfaceName = iinterface.getSimpleName();
         list.add(interfaceName);
      }

      return list;
   }

   public static JSONArray getFields(Class<?> clazz) {
      Field[] fields = clazz.getDeclaredFields();
      JSONArray fieldsinfo = new JSONArray();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         JSONObject jfield = new JSONObject();
         int modifier = field.getModifiers();
         jfield.put("modifier", modifier);
         String typename = field.getType().getSimpleName();
         jfield.put("typename", typename);
         String fieldname = field.getName();
         jfield.put("fieldname", fieldname);
         fieldsinfo.add(jfield);
      }

      return fieldsinfo;
   }

   public static Map<String, Class> getFieldsMap(Class<?> clazz) {
      Field[] fields = clazz.getDeclaredFields();
      Map<String, Class> fieldsinfo = new HashMap();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         Class typename = field.getType();
         String fieldname = field.getName();
         fieldsinfo.put(fieldname, typename);
      }

      return fieldsinfo;
   }

   public static Map<String, Field> getFieldsMapBackField(Class<?> clazz) {
      Field[] fields = clazz.getDeclaredFields();
      Map<String, Field> fieldsinfo = new HashMap();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         String fieldname = field.getName();
         fieldsinfo.put(fieldname, field);
      }

      return fieldsinfo;
   }

   public static List<String> getFieldsArray(Class<?> clazz) {
      Field[] fields = clazz.getDeclaredFields();
      List<String> fieldsArray = new ArrayList();
      new HashMap();
      Field[] var4 = fields;
      int var5 = fields.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field field = var4[var6];
         fieldsArray.add(field.getName());
      }

      return fieldsArray;
   }

   public static <T extends Annotation> T getFieldAnnotation(String fieldname, Class<?> clazz, Class<T> Annotation) {
      Field[] fields = clazz.getDeclaredFields();
      Field[] var4 = fields;
      int var5 = fields.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field field = var4[var6];
         if (fieldname.equals(field.getName())) {
            return field.getAnnotation(Annotation);
         }
      }

      return null;
   }

   public static boolean isBaseType(Object object) {
      Class className = object.getClass();
      return className.equals(Integer.class) || className.equals(Byte.class) || className.equals(Long.class) || className.equals(Double.class) || className.equals(Float.class) || className.equals(Character.class) || className.equals(String.class) || className.equals(Short.class) || className.equals(Boolean.class);
   }

   public static boolean isBaseType(Class clazz) {
      return clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(String.class) || clazz.equals(Short.class) || clazz.equals(Boolean.class);
   }

   public static Class<?> getFieldType(String fieldname, Class<?> clazz) {
      Field[] fields = clazz.getDeclaredFields();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (fieldname.equals(field.getName())) {
            return field.getType();
         }
      }

      return null;
   }

   public static void getAllSuperClasses(Class<?> clazz, List<Class<?>> classList) {
      Class<?> superclass = clazz.getSuperclass();
      if (ObjectUtil.isNotNull(superclass)) {
         classList.add(superclass);
         getAllSuperClasses(superclass, classList);
      }

   }

   public static boolean hasField(String fieldname, Class<?> clazz) {
      List<Class<?>> classList = new ArrayList(100);
      getAllSuperClasses(clazz, classList);
      classList.add(clazz);
      Iterator var3 = classList.iterator();

      while(var3.hasNext()) {
         Class<?> c = (Class)var3.next();
         Field[] fields = c.getDeclaredFields();
         Field[] var6 = fields;
         int var7 = fields.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Field field = var6[var8];
            if (fieldname.equals(field.getName())) {
               return true;
            }
         }
      }

      return false;
   }

   public static void setFieldValue(Object o, String fieldName, String value) throws InvocationTargetException, IllegalAccessException {
      BeanUtils.setProperty(o, fieldName, value);
   }

   public static Object getFieldValue(Object o, Class<?> clazz, String fieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
      return BeanUtils.getProperty(o, fieldName);
   }

   public static Object getNew(Class<?> clazz) throws IllegalAccessException, InstantiationException {
      Constructor<?>[] constructors = clazz.getDeclaredConstructors();
      Constructor[] var2 = constructors;
      int var3 = constructors.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Constructor constructor = var2[var4];
         if (Modifier.isPublic(constructor.getModifiers())) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length > 0) {
               return null;
            }

            return clazz.newInstance();
         }
      }

      return null;
   }

   public static void callMethod(Object object, String methodName, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      MethodUtils.invokeMethod(object, methodName, args);
   }

   public static void callMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      MethodUtils.invokeMethod(object, methodName, args, parameterTypes);
   }

   public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
      return MethodUtils.getAccessibleMethod(clazz, methodName, parameterTypes);
   }

   public static Annotation getMethodAnnotation(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Class<? extends Annotation> aclazz) {
      Method method = getMethod(clazz, methodName, parameterTypes);
      return method.getAnnotation(aclazz);
   }

   public static Annotation getMethodAnnotation(Class<?> clazz, Class<? extends Annotation> aclazz) {
      Method[] methods = clazz.getMethods();
      Method[] var3 = methods;
      int var4 = methods.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method method = var3[var5];
         Annotation an = method.getAnnotation(aclazz);
         if (null != an) {
            return an;
         }
      }

      return null;
   }

   public Class<?> readClassFile(String filename, String name) throws IOException {
      byte[] raw = FileUtil.readFileByBytes(filename);
      return this.defineClass(name, raw, 0, raw.length);
   }

   public static <T> void mergeObject(T origin, T destination) {
      if (origin != null && destination != null) {
         if (origin.getClass().equals(destination.getClass())) {
            Field[] fields = origin.getClass().getDeclaredFields();

            for(int i = 0; i < fields.length; ++i) {
               try {
                  fields[i].setAccessible(true);
                  Object value = fields[i].get(origin);
                  if (null != value) {
                     fields[i].set(destination, value);
                  }

                  fields[i].setAccessible(false);
               } catch (Exception var5) {
               }
            }

         }
      }
   }

   public static boolean check(Class<?> targetClz, String expectedName) {
      if (targetClz.isInterface()) {
         return false;
      } else if (Modifier.isAbstract(targetClz.getModifiers())) {
         return false;
      } else {
         Class[] interfaces = targetClz.getInterfaces();
         if (ArrayUtils.isEmpty(interfaces)) {
            return false;
         } else {
            Class[] var3 = interfaces;
            int var4 = interfaces.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Class intf = var3[var5];
               String intfSimpleName = intf.getSimpleName();
               if (StringUtils.equals(intfSimpleName, expectedName)) {
                  return true;
               }
            }

            return false;
         }
      }
   }
}
