package com.chang.uclass.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Proxy {
   public static final InvocationHandler RETURN_NULL_INVOKER = (proxy, method, args) -> {
      return null;
   };
   public static final InvocationHandler THROW_UNSUPPORTED_INVOKER = new InvocationHandler() {
      public Object invoke(Object proxy, Method method, Object[] args) {
         throw new UnsupportedOperationException("Method [" + ReflectUtils.getName(method) + "] unimplemented.");
      }
   };
   private static final AtomicLong PROXY_CLASS_COUNTER = new AtomicLong(0L);
   private static final String PACKAGE_NAME = Proxy.class.getPackage().getName();
   private static final Map<ClassLoader, Map<String, Object>> PROXY_CACHE_MAP = new WeakHashMap();
   private static final Object PENDING_GENERATION_MARKER = new Object();

   protected Proxy() {
   }

   public static Proxy getProxy(Class<?>... ics) {
      return getProxy(ClassUtils.getClassLoader(Proxy.class), ics);
   }

   public static Proxy getProxy(ClassLoader cl, Class<?>... ics) {
      if (ics.length > 65535) {
         throw new IllegalArgumentException("interface limit exceeded");
      } else {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < ics.length; ++i) {
            String itf = ics[i].getName();
            if (!ics[i].isInterface()) {
               throw new RuntimeException(itf + " is not a interface.");
            }

            Class<?> tmp = null;

            try {
               tmp = Class.forName(itf, false, cl);
            } catch (ClassNotFoundException var42) {
            }

            if (tmp != ics[i]) {
               throw new IllegalArgumentException(ics[i] + " is not visible from class loader");
            }

            sb.append(itf).append(';');
         }

         String key = sb.toString();
         Map cache;
         synchronized(PROXY_CACHE_MAP) {
            cache = (Map)PROXY_CACHE_MAP.computeIfAbsent(cl, (k) -> {
               return new HashMap();
            });
         }

         Proxy proxy = null;
         synchronized(cache) {
            while(true) {
               Object value = cache.get(key);
               if (value instanceof Reference) {
                  proxy = (Proxy)((Reference)value).get();
                  if (proxy != null) {
                     return proxy;
                  }
               }

               if (value != PENDING_GENERATION_MARKER) {
                  cache.put(key, PENDING_GENERATION_MARKER);
                  break;
               }

               try {
                  cache.wait();
               } catch (InterruptedException var40) {
               }
            }
         }

         long id = PROXY_CLASS_COUNTER.getAndIncrement();
         String pkg = null;
         ClassGenerator ccp = null;
         ClassGenerator ccm = null;
         boolean var37 = false;

         try {
            var37 = true;
            ccp = ClassGenerator.newInstance(cl);
            Set<String> worked = new HashSet();
            ArrayList methods = new ArrayList();

            for(int i = 0; i < ics.length; ++i) {
               if (!Modifier.isPublic(ics[i].getModifiers())) {
                  String npkg = ics[i].getPackage().getName();
                  if (pkg == null) {
                     pkg = npkg;
                  } else if (!pkg.equals(npkg)) {
                     throw new IllegalArgumentException("non-public interfaces from different packages");
                  }
               }

               ccp.addInterface(ics[i]);
               Method[] var51 = ics[i].getMethods();
               int var15 = var51.length;

               for(int var16 = 0; var16 < var15; ++var16) {
                  Method method = var51[var16];
                  String desc = ReflectUtils.getDesc(method);
                  if (!worked.contains(desc) && !Modifier.isStatic(method.getModifiers()) && (!ics[i].isInterface() || !Modifier.isStatic(method.getModifiers()))) {
                     worked.add(desc);
                     int ix = methods.size();
                     Class<?> rt = method.getReturnType();
                     Class<?>[] pts = method.getParameterTypes();
                     StringBuilder code = (new StringBuilder("Object[] args = new Object[")).append(pts.length).append("];");

                     for(int j = 0; j < pts.length; ++j) {
                        code.append(" args[").append(j).append("] = ($w)$").append(j + 1).append(";");
                     }

                     code.append(" Object ret = handler.invoke(this, methods[").append(ix).append("], args);");
                     if (!Void.TYPE.equals(rt)) {
                        code.append(" return ").append(asArgument(rt, "ret")).append(";");
                     }

                     methods.add(method);
                     ccp.addMethod(method.getName(), method.getModifiers(), rt, pts, method.getExceptionTypes(), code.toString());
                  }
               }
            }

            if (pkg == null) {
               pkg = PACKAGE_NAME;
            }

            String pcn = pkg + ".proxy" + id;
            ccp.setClassName(pcn);
            ccp.addField("public static java.lang.reflect.Method[] methods;");
            ccp.addField("private " + InvocationHandler.class.getName() + " handler;");
            ccp.addConstructor(1, new Class[]{InvocationHandler.class}, new Class[0], "handler=$1;");
            ccp.addDefaultConstructor();
            Class<?> clazz = ccp.toClass();
            clazz.getField("methods").set((Object)null, methods.toArray(new Method[0]));
            String fcn = Proxy.class.getName() + id;
            ccm = ClassGenerator.newInstance(cl);
            ccm.setClassName(fcn);
            ccm.addDefaultConstructor();
            ccm.setSuperClass(Proxy.class);
            ccm.addMethod("public Object newInstance(" + InvocationHandler.class.getName() + " h){ return new " + pcn + "($1); }");
            Class<?> pc = ccm.toClass();
            proxy = (Proxy)pc.newInstance();
            var37 = false;
         } catch (RuntimeException var43) {
            throw var43;
         } catch (Exception var44) {
            throw new RuntimeException(var44.getMessage(), var44);
         } finally {
            if (var37) {
               if (ccp != null) {
                  ccp.release();
               }

               if (ccm != null) {
                  ccm.release();
               }

               synchronized(cache) {
                  if (proxy == null) {
                     cache.remove(key);
                  } else {
                     cache.put(key, new WeakReference(proxy));
                  }

                  cache.notifyAll();
               }
            }
         }

         if (ccp != null) {
            ccp.release();
         }

         if (ccm != null) {
            ccm.release();
         }

         synchronized(cache) {
            if (proxy == null) {
               cache.remove(key);
            } else {
               cache.put(key, new WeakReference(proxy));
            }

            cache.notifyAll();
            return proxy;
         }
      }
   }

   private static String asArgument(Class<?> cl, String name) {
      if (cl.isPrimitive()) {
         if (Boolean.TYPE == cl) {
            return name + "==null?false:((Boolean)" + name + ").booleanValue()";
         } else if (Byte.TYPE == cl) {
            return name + "==null?(byte)0:((Byte)" + name + ").byteValue()";
         } else if (Character.TYPE == cl) {
            return name + "==null?(char)0:((Character)" + name + ").charValue()";
         } else if (Double.TYPE == cl) {
            return name + "==null?(double)0:((Double)" + name + ").doubleValue()";
         } else if (Float.TYPE == cl) {
            return name + "==null?(float)0:((Float)" + name + ").floatValue()";
         } else if (Integer.TYPE == cl) {
            return name + "==null?(int)0:((Integer)" + name + ").intValue()";
         } else if (Long.TYPE == cl) {
            return name + "==null?(long)0:((Long)" + name + ").longValue()";
         } else if (Short.TYPE == cl) {
            return name + "==null?(short)0:((Short)" + name + ").shortValue()";
         } else {
            throw new RuntimeException(name + " is unknown primitive type.");
         }
      } else {
         return "(" + ReflectUtils.getName(cl) + ")" + name;
      }
   }

   public Object newInstance() {
      return this.newInstance(THROW_UNSUPPORTED_INVOKER);
   }

   public abstract Object newInstance(InvocationHandler var1);
}
