package com.chang.uclass.bytecode;

import com.chang.until.spring.context.ClassNameComparator;
import com.chang.until.spring.context.ClassPathScanHandler;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScanClassUtils {
   public static List<Object> scanConfiguredPackagesForObject(String path, Class<? extends Annotation> annotation) throws Exception {
      ClassPathScanHandler handler = new ClassPathScanHandler(new String[]{path});
      Set<Class<?>> classSet = new TreeSet(new ClassNameComparator());
      classSet.addAll(handler.getPackageAllClasses(path, true));
      List classes = (List)((Stream)classSet.stream().parallel()).filter((x) -> {
         return x.getClass() != null;
      }).collect(Collectors.toList());
      List<Object> reObjects = new ArrayList();
      Iterator var6 = classes.iterator();

      while(var6.hasNext()) {
         Class<?> c = (Class)var6.next();
         reObjects.add(ClassUtils.newInstance(c));
      }

      return reObjects;
   }

   public static List<Class<?>> scanConfiguredPackagesForClass(String path, Class<? extends Annotation> annotation) {
      ClassPathScanHandler handler = new ClassPathScanHandler(new String[]{path});
      Set<Class<?>> classSet = new TreeSet(new ClassNameComparator());
      classSet.addAll(handler.getPackageAllClasses(path, true));
      return (List)((Stream)classSet.stream().parallel()).filter((x) -> {
         return x.getClass() != null;
      }).collect(Collectors.toList());
   }

   public static List<Class<?>> scanClass(String path) {
      ClassPathScanHandler handler = new ClassPathScanHandler(new String[]{path});
      Set<Class<?>> classSet = new TreeSet(new ClassNameComparator());
      classSet.addAll(handler.getPackageAllClasses(path, true));
      return (List)((Stream)classSet.stream().parallel()).collect(Collectors.toList());
   }

   public static List<Object> scanConfiguredPackagesForObjectFromInterface(String path, Class<?> interfaceClass) {
      if (!interfaceClass.isInterface()) {
         throw new RuntimeException("class is not interface");
      } else {
         ClassPathScanHandler handler = new ClassPathScanHandler(new String[]{path});
         Set<Class<?>> classSet = new TreeSet(new ClassNameComparator());
         classSet.addAll(handler.getPackageAllClasses(path, true));
         List<Class<?>> classes = (List)((Stream)classSet.stream().parallel()).filter((clazz) -> {
            Class<?>[] interfaces = new Class[]{clazz.getClass()};
            Class[] var3 = interfaces;
            int var4 = interfaces.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Class<?> face = var3[var5];
               if (Objects.equals(face, interfaceClass)) {
                  return true;
               }
            }

            return false;
         }).collect(Collectors.toList());
         List<Object> reObjects = new ArrayList();
         Iterator var6 = classes.iterator();

         while(var6.hasNext()) {
            Class<?> c = (Class)var6.next();
            reObjects.add(ClassUtils.newInstance(c));
         }

         return reObjects;
      }
   }

   public static List<Class<?>> scanConfiguredPackagesForClassFromInterface(String path, Class<?> interfaceClass) {
      if (!interfaceClass.isInterface()) {
         throw new RuntimeException("class is not interface");
      } else {
         ClassPathScanHandler handler = new ClassPathScanHandler(new String[]{path});
         Set<Class<?>> classSet = new TreeSet(new ClassNameComparator());
         classSet.addAll(handler.getPackageAllClasses(path, true));
         return (List)((Stream)classSet.stream().parallel()).filter((clazz) -> {
            Class<?>[] interfaces = new Class[]{clazz.getClass()};
            Class[] var3 = interfaces;
            int var4 = interfaces.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Class<?> face = var3[var5];
               if (Objects.equals(face, interfaceClass)) {
                  return true;
               }
            }

            return false;
         }).collect(Collectors.toList());
      }
   }
}
