package com.chang.uclass.bytecode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javassist.CannotCompileException;
import javassist.ClassMap;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public final class ClassGenerator {
   private static final AtomicLong CLASS_NAME_COUNTER = new AtomicLong(0L);
   private static final String SIMPLE_NAME_TAG = "<init>";
   private static final Map<ClassLoader, ClassPool> POOL_MAP = new ConcurrentHashMap();
   private ClassPool mPool;
   private CtClass mCtc;
   private String mClassName;
   private String mSuperClass;
   private Set<String> mInterfaces;
   private List<String> mFields;
   private List<String> mConstructors;
   private List<String> mMethods;
   private Map<String, Method> mCopyMethods;
   private Map<String, Constructor<?>> mCopyConstructors;
   private boolean mDefaultConstructor = false;

   private ClassGenerator() {
   }

   private ClassGenerator(ClassPool pool) {
      this.mPool = pool;
   }

   public static ClassGenerator newInstance() {
      return new ClassGenerator(getClassPool(Thread.currentThread().getContextClassLoader()));
   }

   public static ClassGenerator newInstance(ClassLoader loader) {
      return new ClassGenerator(getClassPool(loader));
   }

   public static boolean isDynamicClass(Class<?> cl) {
      return DC.class.isAssignableFrom(cl);
   }

   public static ClassPool getClassPool(ClassLoader loader) {
      if (loader == null) {
         return ClassPool.getDefault();
      } else {
         ClassPool pool = (ClassPool)POOL_MAP.get(loader);
         if (pool == null) {
            pool = new ClassPool(true);
            pool.appendClassPath(new LoaderClassPath(loader));
            POOL_MAP.put(loader, pool);
         }

         return pool;
      }
   }

   private static String modifier(int mod) {
      StringBuilder modifier = new StringBuilder();
      if (Modifier.isPublic(mod)) {
         modifier.append("public");
      } else if (Modifier.isProtected(mod)) {
         modifier.append("protected");
      } else if (Modifier.isPrivate(mod)) {
         modifier.append("private");
      }

      if (Modifier.isStatic(mod)) {
         modifier.append(" static");
      }

      if (Modifier.isVolatile(mod)) {
         modifier.append(" volatile");
      }

      return modifier.toString();
   }

   public String getClassName() {
      return this.mClassName;
   }

   public ClassGenerator setClassName(String name) {
      this.mClassName = name;
      return this;
   }

   public ClassGenerator addInterface(String cn) {
      if (this.mInterfaces == null) {
         this.mInterfaces = new HashSet();
      }

      this.mInterfaces.add(cn);
      return this;
   }

   public ClassGenerator addInterface(Class<?> cl) {
      return this.addInterface(cl.getName());
   }

   public ClassGenerator setSuperClass(String cn) {
      this.mSuperClass = cn;
      return this;
   }

   public ClassGenerator setSuperClass(Class<?> cl) {
      this.mSuperClass = cl.getName();
      return this;
   }

   public ClassGenerator addField(String code) {
      if (this.mFields == null) {
         this.mFields = new ArrayList();
      }

      this.mFields.add(code);
      return this;
   }

   public ClassGenerator addField(String name, int mod, Class<?> type) {
      return this.addField(name, mod, type, (String)null);
   }

   public ClassGenerator addField(String name, int mod, Class<?> type, String def) {
      StringBuilder sb = new StringBuilder();
      sb.append(modifier(mod)).append(' ').append(ReflectUtils.getName(type)).append(' ');
      sb.append(name);
      if (StringUtils.isNotEmpty(def)) {
         sb.append('=');
         sb.append(def);
      }

      sb.append(';');
      return this.addField(sb.toString());
   }

   public ClassGenerator addMethod(String code) {
      if (this.mMethods == null) {
         this.mMethods = new ArrayList();
      }

      this.mMethods.add(code);
      return this;
   }

   public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, String body) {
      return this.addMethod(name, mod, rt, pts, (Class[])null, body);
   }

   public ClassGenerator addGetMethod(String fieldName, int mod, Class<?> rt) {
      String getName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      return this.addMethod(getName, mod, rt, new Class[0], (Class[])null, "return " + fieldName + ";");
   }

   public ClassGenerator addSetMethod(String fieldName, int mod, Class<?>[] pts) {
      String setName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      return this.addMethod(setName, mod, Void.TYPE, pts, (Class[])null, fieldName + "=$1;");
   }

   public ClassGenerator addGetSetMethod(String fieldName, int mod, Class<?> rt, Class<?>[] pts) {
      String getName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      String setName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      this.addMethod(getName, mod, rt, new Class[0], (Class[])null, "return " + fieldName + ";");
      return this.addMethod(setName, mod, Void.TYPE, pts, (Class[])null, fieldName + "=$1;");
   }

   public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, Class<?>[] ets, String body) {
      StringBuilder sb = new StringBuilder();
      sb.append(modifier(mod)).append(' ').append(ReflectUtils.getName(rt)).append(' ').append(name);
      sb.append('(');

      int i;
      for(i = 0; i < pts.length; ++i) {
         if (i > 0) {
            sb.append(',');
         }

         sb.append(ReflectUtils.getName(pts[i]));
         sb.append(" arg").append(i);
      }

      sb.append(')');
      if (ArrayUtils.isNotEmpty(ets)) {
         sb.append(" throws ");

         for(i = 0; i < ets.length; ++i) {
            if (i > 0) {
               sb.append(',');
            }

            sb.append(ReflectUtils.getName(ets[i]));
         }
      }

      sb.append('{').append(body).append('}');
      return this.addMethod(sb.toString());
   }

   public ClassGenerator addMethod(Method m) {
      this.addMethod(m.getName(), m);
      return this;
   }

   public ClassGenerator addMethod(String name, Method m) {
      String desc = name + ReflectUtils.getDescWithoutMethodName(m);
      this.addMethod(':' + desc);
      if (this.mCopyMethods == null) {
         this.mCopyMethods = new ConcurrentHashMap(8);
      }

      this.mCopyMethods.put(desc, m);
      return this;
   }

   public ClassGenerator addConstructor(String code) {
      if (this.mConstructors == null) {
         this.mConstructors = new LinkedList();
      }

      this.mConstructors.add(code);
      return this;
   }

   public ClassGenerator addConstructor(int mod, Class<?>[] pts, String body) {
      return this.addConstructor(mod, pts, (Class[])null, body);
   }

   public ClassGenerator addConstructor(int mod, Class<?>[] pts, Class<?>[] ets, String body) {
      StringBuilder sb = new StringBuilder();
      sb.append(modifier(mod)).append(' ').append("<init>");
      sb.append('(');

      int i;
      for(i = 0; i < pts.length; ++i) {
         if (i > 0) {
            sb.append(',');
         }

         sb.append(ReflectUtils.getName(pts[i]));
         sb.append(" arg").append(i);
      }

      sb.append(')');
      if (ArrayUtils.isNotEmpty(ets)) {
         sb.append(" throws ");

         for(i = 0; i < ets.length; ++i) {
            if (i > 0) {
               sb.append(',');
            }

            sb.append(ReflectUtils.getName(ets[i]));
         }
      }

      sb.append('{').append(body).append('}');
      return this.addConstructor(sb.toString());
   }

   public ClassGenerator addConstructor(Constructor<?> c) {
      String desc = ReflectUtils.getDesc(c);
      this.addConstructor(":" + desc);
      if (this.mCopyConstructors == null) {
         this.mCopyConstructors = new ConcurrentHashMap(4);
      }

      this.mCopyConstructors.put(desc, c);
      return this;
   }

   public ClassGenerator addDefaultConstructor() {
      this.mDefaultConstructor = true;
      return this;
   }

   public ClassPool getClassPool() {
      return this.mPool;
   }

   public Class<?> toClass() {
      return this.toClass(ClassUtils.getClassLoader(ClassGenerator.class), this.getClass().getProtectionDomain());
   }

   public Class<?> toClass(ClassLoader loader, ProtectionDomain pd) {
      if (this.mCtc != null) {
         this.mCtc.detach();
      }

      long id = CLASS_NAME_COUNTER.getAndIncrement();

      try {
         CtClass ctcs = this.mSuperClass == null ? null : this.mPool.get(this.mSuperClass);
         if (this.mClassName == null) {
            this.mClassName = (this.mSuperClass != null && !javassist.Modifier.isPublic(ctcs.getModifiers()) ? this.mSuperClass + "$sc" : ClassGenerator.class.getName()) + id;
         }

         this.mCtc = this.mPool.makeClass(this.mClassName);
         if (this.mSuperClass != null) {
            this.mCtc.setSuperclass(ctcs);
         }

         this.mCtc.addInterface(this.mPool.get(DC.class.getName()));
         Iterator var6;
         String code;
         if (this.mInterfaces != null) {
            var6 = this.mInterfaces.iterator();

            while(var6.hasNext()) {
               code = (String)var6.next();
               this.mCtc.addInterface(this.mPool.get(code));
            }
         }

         if (this.mFields != null) {
            var6 = this.mFields.iterator();

            while(var6.hasNext()) {
               code = (String)var6.next();
               this.mCtc.addField(CtField.make(code, this.mCtc));
            }
         }

         if (this.mMethods != null) {
            var6 = this.mMethods.iterator();

            while(var6.hasNext()) {
               code = (String)var6.next();
               if (code.charAt(0) == ':') {
                  this.mCtc.addMethod(CtNewMethod.copy(this.getCtMethod((Method)this.mCopyMethods.get(code.substring(1))), code.substring(1, code.indexOf(40)), this.mCtc, (ClassMap)null));
               } else {
                  this.mCtc.addMethod(CtNewMethod.make(code, this.mCtc));
               }
            }
         }

         if (this.mDefaultConstructor) {
            this.mCtc.addConstructor(CtNewConstructor.defaultConstructor(this.mCtc));
         }

         if (this.mConstructors != null) {
            var6 = this.mConstructors.iterator();

            while(var6.hasNext()) {
               code = (String)var6.next();
               if (code.charAt(0) == ':') {
                  this.mCtc.addConstructor(CtNewConstructor.copy(this.getCtConstructor((Constructor)this.mCopyConstructors.get(code.substring(1))), this.mCtc, (ClassMap)null));
               } else {
                  String[] sn = this.mCtc.getSimpleName().split("\\$+");
                  this.mCtc.addConstructor(CtNewConstructor.make(code.replaceFirst("<init>", sn[sn.length - 1]), this.mCtc));
               }
            }
         }

         return this.mCtc.toClass(loader, pd);
      } catch (RuntimeException var9) {
         throw var9;
      } catch (NotFoundException var10) {
         throw new RuntimeException(var10.getMessage(), var10);
      } catch (CannotCompileException var11) {
         throw new RuntimeException(var11.getMessage(), var11);
      }
   }

   public void release() {
      if (this.mCtc != null) {
         this.mCtc.detach();
      }

      if (this.mInterfaces != null) {
         this.mInterfaces.clear();
      }

      if (this.mFields != null) {
         this.mFields.clear();
      }

      if (this.mMethods != null) {
         this.mMethods.clear();
      }

      if (this.mConstructors != null) {
         this.mConstructors.clear();
      }

      if (this.mCopyMethods != null) {
         this.mCopyMethods.clear();
      }

      if (this.mCopyConstructors != null) {
         this.mCopyConstructors.clear();
      }

   }

   private CtClass getCtClass(Class<?> c) throws NotFoundException {
      return this.mPool.get(c.getName());
   }

   private CtMethod getCtMethod(Method m) throws NotFoundException {
      return this.getCtClass(m.getDeclaringClass()).getMethod(m.getName(), ReflectUtils.getDescWithoutMethodName(m));
   }

   private CtConstructor getCtConstructor(Constructor<?> c) throws NotFoundException {
      return this.getCtClass(c.getDeclaringClass()).getConstructor(ReflectUtils.getDesc(c));
   }

   public interface DC {
   }
}
