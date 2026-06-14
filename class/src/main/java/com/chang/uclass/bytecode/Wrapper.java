package com.chang.uclass.bytecode;

import com.chang.uclass.bytecode.ClassGenerator;
import com.chang.uclass.bytecode.ClassUtils;
import com.chang.uclass.bytecode.NoSuchMethodException;
import com.chang.uclass.bytecode.NoSuchPropertyException;
import com.chang.uclass.bytecode.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

public abstract class Wrapper {
   private static final Map<Class<?>, Wrapper> WRAPPER_MAP = new ConcurrentHashMap();
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final String[] OBJECT_METHODS = new String[]{"getClass", "hashCode", "toString", "equals"};
   private static final Wrapper OBJECT_WRAPPER = new Wrapper() {
      public String[] getMethodNames() {
         return Wrapper.OBJECT_METHODS;
      }

      public String[] getDeclaredMethodNames() {
         return Wrapper.OBJECT_METHODS;
      }

      public String[] getPropertyNames() {
         return Wrapper.EMPTY_STRING_ARRAY;
      }

      public Class<?> getPropertyType(String pn) {
         return null;
      }

      public Object getPropertyValue(Object instance, String pn) throws NoSuchPropertyException {
         throw new NoSuchPropertyException("Property [" + pn + "] not found.");
      }

      public void setPropertyValue(Object instance, String pn, Object pv) throws NoSuchPropertyException {
         throw new NoSuchPropertyException("Property [" + pn + "] not found.");
      }

      public boolean hasProperty(String name) {
         return false;
      }

      public Object invokeMethod(Object instance, String mn, Class<?>[] types, Object[] args) throws com.chang.uclass.bytecode.NoSuchMethodException {
         if ("getClass".equals(mn)) {
            return instance.getClass();
         } else if ("hashCode".equals(mn)) {
            return instance.hashCode();
         } else if ("toString".equals(mn)) {
            return instance.toString();
         } else if ("equals".equals(mn)) {
            if (args.length == 1) {
               return instance.equals(args[0]);
            } else {
               throw new IllegalArgumentException("Invoke method [" + mn + "] argument number error.");
            }
         } else {
            throw new com.chang.uclass.bytecode.NoSuchMethodException("Method [" + mn + "] not found.");
         }
      }
   };
   private static AtomicLong WRAPPER_CLASS_COUNTER = new AtomicLong(0L);

   public static Wrapper getWrapper(Class<?> c) {
      while(ClassGenerator.isDynamicClass(c)) {
         c = c.getSuperclass();
      }

      if (c == Object.class) {
         return OBJECT_WRAPPER;
      } else {
         return (Wrapper)WRAPPER_MAP.computeIfAbsent(c, (key) -> {
            return makeWrapper(key);
         });
      }
   }

   private static Wrapper makeWrapper(Class<?> c) {
      if (c.isPrimitive()) {
         throw new IllegalArgumentException("Can not create wrapper for primitive type: " + c);
      } else {
         String name = c.getName();
         ClassLoader cl = ClassUtils.getClassLoader(c);
         StringBuilder c1 = new StringBuilder("public void setPropertyValue(Object o, String n, Object v){ ");
         StringBuilder c2 = new StringBuilder("public Object getPropertyValue(Object o, String n){ ");
         StringBuilder c3 = new StringBuilder("public Object invokeMethod(Object o, String n, Class[] p, Object[] v) throws " + InvocationTargetException.class.getName() + "{ ");
         c1.append(name).append(" w; try{ w = ((").append(name).append(")$1); }catch(Throwable e){ throw new IllegalArgumentException(e); }");
         c2.append(name).append(" w; try{ w = ((").append(name).append(")$1); }catch(Throwable e){ throw new IllegalArgumentException(e); }");
         c3.append(name).append(" w; try{ w = ((").append(name).append(")$1); }catch(Throwable e){ throw new IllegalArgumentException(e); }");
         Map<String, Class<?>> pts = new HashMap();
         Map<String, Method> ms = new LinkedHashMap();
         List<String> mns = new ArrayList();
         List<String> dmns = new ArrayList();
         Field[] var10 = c.getFields();
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            Field f = var10[var12];
            String fn = f.getName();
            Class<?> ft = f.getType();
            if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
               c1.append(" if( $2.equals(\"").append(fn).append("\") ){ w.").append(fn).append("=").append(arg(ft, "$3")).append("; return; }");
               c2.append(" if( $2.equals(\"").append(fn).append("\") ){ return ($w)w.").append(fn).append("; }");
               pts.put(fn, ft);
            }
         }

         Method[] methods = c.getMethods();
         boolean hasMethod = hasMethods(methods);
         int len;
         if (hasMethod) {
            c3.append(" try{");
            Method[] var32 = methods;
            int var34 = methods.length;

            for(int var37 = 0; var37 < var34; ++var37) {
               Method m = var32[var37];
               if (m.getDeclaringClass() != Object.class) {
                  String mn = m.getName();
                  c3.append(" if( \"").append(mn).append("\".equals( $2 ) ");
                  len = m.getParameterTypes().length;
                  c3.append(" && ").append(" $3.length == ").append(len);
                  boolean override = false;
                  Method[] var19 = methods;
                  int var20 = methods.length;

                  for(int var21 = 0; var21 < var20; ++var21) {
                     Method m2 = var19[var21];
                     if (m != m2 && m.getName().equals(m2.getName())) {
                        override = true;
                        break;
                     }
                  }

                  if (override && len > 0) {
                     for(int l = 0; l < len; ++l) {
                        c3.append(" && ").append(" $3[").append(l).append("].getName().equals(\"").append(m.getParameterTypes()[l].getName()).append("\")");
                     }
                  }

                  c3.append(" ) { ");
                  if (m.getReturnType() == Void.TYPE) {
                     c3.append(" w.").append(mn).append('(').append(args(m.getParameterTypes(), "$4")).append(");").append(" return null;");
                  } else {
                     c3.append(" return ($w)w.").append(mn).append('(').append(args(m.getParameterTypes(), "$4")).append(");");
                  }

                  c3.append(" }");
                  mns.add(mn);
                  if (m.getDeclaringClass() == c) {
                     dmns.add(mn);
                  }

                  ms.put(ReflectUtils.getDesc(m), m);
               }
            }

            c3.append(" } catch(Throwable e) { ");
            c3.append("     throw new java.lang.reflect.InvocationTargetException(e); ");
            c3.append(" }");
         }

         c3.append(" throw new " + com.chang.uclass.bytecode.NoSuchMethodException.class.getName() + "(\"Not found method \\\"\"+$2+\"\\\" in class " + c.getName() + ".\"); }");
         Iterator var35 = ms.entrySet().iterator();

         while(var35.hasNext()) {
            Map.Entry<String, Method> entry = (Map.Entry)var35.next();
            String md = (String)entry.getKey();
            Method method = (Method)entry.getValue();
            Matcher matcher;
            String pn;
            if ((matcher = ReflectUtils.GETTER_METHOD_DESC_PATTERN.matcher(md)).matches()) {
               pn = propertyName(matcher.group(1));
               c2.append(" if( $2.equals(\"").append(pn).append("\") ){ return ($w)w.").append(method.getName()).append("(); }");
               pts.put(pn, method.getReturnType());
            } else if ((matcher = ReflectUtils.IS_HAS_CAN_METHOD_DESC_PATTERN.matcher(md)).matches()) {
               pn = propertyName(matcher.group(1));
               c2.append(" if( $2.equals(\"").append(pn).append("\") ){ return ($w)w.").append(method.getName()).append("(); }");
               pts.put(pn, method.getReturnType());
            } else if ((matcher = ReflectUtils.SETTER_METHOD_DESC_PATTERN.matcher(md)).matches()) {
               Class<?> pt = method.getParameterTypes()[0];
               pn = propertyName(matcher.group(1));
               c1.append(" if( $2.equals(\"").append(pn).append("\") ){ w.").append(method.getName()).append("(").append(arg(pt, "$3")).append("); return; }");
               pts.put(pn, pt);
            }
         }

         c1.append(" throw new " + NoSuchPropertyException.class.getName() + "(\"Not found property \\\"\"+$2+\"\\\" field or setter method in class " + c.getName() + ".\"); }");
         c2.append(" throw new " + NoSuchPropertyException.class.getName() + "(\"Not found property \\\"\"+$2+\"\\\" field or getter method in class " + c.getName() + ".\"); }");
         long id = WRAPPER_CLASS_COUNTER.getAndIncrement();
         ClassGenerator cc = ClassGenerator.newInstance(cl);
         cc.setClassName((Modifier.isPublic(c.getModifiers()) ? Wrapper.class.getName() : c.getName() + "$sw") + id);
         cc.setSuperClass(Wrapper.class);
         cc.addDefaultConstructor();
         cc.addField("public static String[] pns;");
         cc.addField("public static " + Map.class.getName() + " pts;");
         cc.addField("public static String[] mns;");
         cc.addField("public static String[] dmns;");
         int i = 0;

         for(len = ms.size(); i < len; ++i) {
            cc.addField("public static Class[] mts" + i + ";");
         }

         cc.addMethod("public String[] getPropertyNames(){ return pns; }");
         cc.addMethod("public boolean hasProperty(String n){ return pts.containsKey($1); }");
         cc.addMethod("public Class getPropertyType(String n){ return (Class)pts.get($1); }");
         cc.addMethod("public String[] getMethodNames(){ return mns; }");
         cc.addMethod("public String[] getDeclaredMethodNames(){ return dmns; }");
         cc.addMethod(c1.toString());
         cc.addMethod(c2.toString());
         cc.addMethod(c3.toString());

         try {
            Class<?> wc = cc.toClass();
            wc.getField("pts").set((Object)null, pts);
            wc.getField("pns").set((Object)null, pts.keySet().toArray(new String[0]));
            wc.getField("mns").set((Object)null, mns.toArray(new String[0]));
            wc.getField("dmns").set((Object)null, dmns.toArray(new String[0]));
            len = 0;
            Iterator var48 = ms.values().iterator();

            while(var48.hasNext()) {
               Method m = (Method)var48.next();
               wc.getField("mts" + len++).set((Object)null, m.getParameterTypes());
            }

            Wrapper var50 = (Wrapper)wc.newInstance();
            return var50;
         } catch (RuntimeException var27) {
            throw var27;
         } catch (Throwable var28) {
            throw new RuntimeException(var28.getMessage(), var28);
         } finally {
            cc.release();
            ms.clear();
            mns.clear();
            dmns.clear();
         }
      }
   }

   private static String arg(Class<?> cl, String name) {
      if (cl.isPrimitive()) {
         if (cl == Boolean.TYPE) {
            return "((Boolean)" + name + ").booleanValue()";
         } else if (cl == Byte.TYPE) {
            return "((Byte)" + name + ").byteValue()";
         } else if (cl == Character.TYPE) {
            return "((Character)" + name + ").charValue()";
         } else if (cl == Double.TYPE) {
            return "((Number)" + name + ").doubleValue()";
         } else if (cl == Float.TYPE) {
            return "((Number)" + name + ").floatValue()";
         } else if (cl == Integer.TYPE) {
            return "((Number)" + name + ").intValue()";
         } else if (cl == Long.TYPE) {
            return "((Number)" + name + ").longValue()";
         } else if (cl == Short.TYPE) {
            return "((Number)" + name + ").shortValue()";
         } else {
            throw new RuntimeException("Unknown primitive type: " + cl.getName());
         }
      } else {
         return "(" + ReflectUtils.getName(cl) + ")" + name;
      }
   }

   private static String args(Class<?>[] cs, String name) {
      int len = cs.length;
      if (len == 0) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < len; ++i) {
            if (i > 0) {
               sb.append(',');
            }

            sb.append(arg(cs[i], name + "[" + i + "]"));
         }

         return sb.toString();
      }
   }

   private static String propertyName(String pn) {
      return pn.length() != 1 && !Character.isLowerCase(pn.charAt(1)) ? pn : Character.toLowerCase(pn.charAt(0)) + pn.substring(1);
   }

   private static boolean hasMethods(Method[] methods) {
      if (methods != null && methods.length != 0) {
         Method[] var1 = methods;
         int var2 = methods.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Method m = var1[var3];
            if (m.getDeclaringClass() != Object.class) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public abstract String[] getPropertyNames();

   public abstract Class<?> getPropertyType(String var1);

   public abstract boolean hasProperty(String var1);

   public abstract Object getPropertyValue(Object var1, String var2) throws NoSuchPropertyException, IllegalArgumentException;

   public abstract void setPropertyValue(Object var1, String var2, Object var3) throws NoSuchPropertyException, IllegalArgumentException;

   public Object[] getPropertyValues(Object instance, String[] pns) throws NoSuchPropertyException, IllegalArgumentException {
      Object[] ret = new Object[pns.length];

      for(int i = 0; i < ret.length; ++i) {
         ret[i] = this.getPropertyValue(instance, pns[i]);
      }

      return ret;
   }

   public void setPropertyValues(Object instance, String[] pns, Object[] pvs) throws NoSuchPropertyException, IllegalArgumentException {
      if (pns.length != pvs.length) {
         throw new IllegalArgumentException("pns.length != pvs.length");
      } else {
         for(int i = 0; i < pns.length; ++i) {
            this.setPropertyValue(instance, pns[i], pvs[i]);
         }

      }
   }

   public abstract String[] getMethodNames();

   public abstract String[] getDeclaredMethodNames();

   public boolean hasMethod(String name) {
      String[] var2 = this.getMethodNames();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String mn = var2[var4];
         if (mn.equals(name)) {
            return true;
         }
      }

      return false;
   }

   public abstract Object invokeMethod(Object var1, String var2, Class<?>[] var3, Object[] var4) throws NoSuchMethodException, InvocationTargetException;
}
