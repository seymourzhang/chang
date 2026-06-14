package com.chang.until.spring.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassPathScanHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathScanHandler.class);
   private static final String CLASS_EXTENSION_NAME = ".class";
   private boolean excludeInner = true;
   private boolean checkInOrEx = true;
   private List<String> classFilters = null;
   private Reflections reflections = null;

   public ClassPathScanHandler(String... packages) {
      this.reflections = new Reflections((new ConfigurationBuilder()).forPackages(packages).addScanners(new Scanner[]{new TypeAnnotationsScanner(), new SubTypesScanner()}));
   }

   public ClassPathScanHandler(Boolean excludeInner, Boolean checkInOrEx, List<String> classFilters) {
      this.excludeInner = excludeInner;
      this.checkInOrEx = checkInOrEx;
      this.classFilters = classFilters;
   }

   public Set<Class<?>> getAllClassesWithAnnotation(Class<? extends Annotation> annotation, boolean honorInherited) {
      return this.reflections.getTypesAnnotatedWith(annotation, honorInherited);
   }

   public <T> Set<Class<? extends T>> getAllSubClassesByParent(Class<T> parent) {
      return this.reflections.getSubTypesOf(parent);
   }

   public Set<Class<?>> getPackageAllClasses(String basePackage, boolean recursive) {
      if (basePackage == null) {
         return new HashSet();
      } else {
         Set<Class<?>> classes = new LinkedHashSet();
         String packageName = basePackage;
         if (basePackage.endsWith(".")) {
            packageName = basePackage.substring(0, basePackage.lastIndexOf(46));
         }

         String package2Path = packageName.replace('.', '/');

         try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);

            while(dirs.hasMoreElements()) {
               URL url = (URL)dirs.nextElement();
               String protocol = url.getProtocol();
               if ("file".equals(protocol)) {
                  String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                  this.doScanPackageClassesByFile(classes, packageName, filePath, recursive);
               } else if ("jar".equals(protocol)) {
                  this.doScanPackageClassesByJar(packageName, url, recursive, classes);
               }
            }
         } catch (IOException var10) {
            LOGGER.error("IOException error:");
         }

         TreeSet<Class<?>> sortedClasses = new TreeSet(new ClassNameComparator());
         sortedClasses.addAll(classes);
         return sortedClasses;
      }
   }

   private void doScanPackageClassesByJar(String basePackage, URL url, boolean recursive, Set<Class<?>> classes) {
      String package2Path = basePackage.replace('.', '/');

      try {
         JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
         Enumeration<JarEntry> entries = jar.entries();

         while(true) {
            while(true) {
               JarEntry entry;
               String name;
               do {
                  do {
                     do {
                        if (!entries.hasMoreElements()) {
                           return;
                        }

                        entry = (JarEntry)entries.nextElement();
                        name = entry.getName();
                     } while(!name.startsWith(package2Path));
                  } while(entry.isDirectory());
               } while(!recursive && name.lastIndexOf(47) != package2Path.length());

               if (this.excludeInner && name.indexOf(36) != -1) {
                  LOGGER.debug("exclude inner class with name:" + name);
               } else {
                  String classSimpleName = name.substring(name.lastIndexOf(47) + 1);
                  if (this.filterClassName(classSimpleName)) {
                     String className = name.replace('/', '.');
                     className = className.substring(0, className.length() - 6);

                     try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                     } catch (ClassNotFoundException var13) {
                        LOGGER.error("Class.forName error:URL is ===>" + url.getPath());
                     }
                  }
               }
            }
         }
      } catch (IOException var14) {
         LOGGER.error("IOException error:URL is ===>" + url.getPath());
      } catch (Throwable var15) {
         LOGGER.error("ScanPackageClassesByJar error:URL is ===>" + url.getPath());
      }

   }

   private void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, final boolean recursive) {
      File dir = new File(packagePath);
      if (dir.exists() && dir.isDirectory()) {
         File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
               return ClassPathScanHandler.this.filterClassFileByCustomization(pathname, recursive);
            }
         });
         if (null != files && files.length != 0) {
            File[] var7 = files;
            int var8 = files.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               File file = var7[var9];
               if (file.isDirectory()) {
                  this.doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
               } else {
                  String className = file.getName().substring(0, file.getName().length() - ".class".length());

                  try {
                     classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                  } catch (Throwable var13) {
                     LOGGER.error("IOException error:", var13);
                  }
               }
            }

         }
      }
   }

   private boolean filterClassFileByCustomization(@Nonnull File file, boolean recursive) {
      if (file.isDirectory()) {
         return recursive;
      } else {
         String filename = file.getName();
         if (this.excludeInner && filename.indexOf(36) != -1) {
            LOGGER.debug("exclude inner class with name:" + filename);
            return false;
         } else {
            return this.filterClassName(filename);
         }
      }
   }

   private boolean filterClassName(String className) {
      if (!className.endsWith(".class")) {
         return false;
      } else if (null != this.classFilters && !this.classFilters.isEmpty()) {
         String tmpName = className.substring(0, className.length() - 6);
         boolean flag = false;
         Iterator var4 = this.classFilters.iterator();

         while(var4.hasNext()) {
            String str = (String)var4.next();
            flag = this.matchInnerClassname(tmpName, str);
            if (flag) {
               break;
            }
         }

         return this.checkInOrEx && flag || !this.checkInOrEx && !flag;
      } else {
         return true;
      }
   }

   private boolean matchInnerClassname(String className, String filterString) {
      String reg = "^" + filterString.replace("*", ".*") + "$";
      Pattern p = Pattern.compile(reg);
      return p.matcher(className).find();
   }

   public boolean isExcludeInner() {
      return this.excludeInner;
   }

   public void setExcludeInner(boolean excludeInner) {
      this.excludeInner = excludeInner;
   }

   public boolean isCheckInOrEx() {
      return this.checkInOrEx;
   }

   public void setCheckInOrEx(boolean checkInOrEx) {
      this.checkInOrEx = checkInOrEx;
   }

   public List<String> getClassFilters() {
      return this.classFilters;
   }

   public void setClassFilters(List<String> classFilters) {
      this.classFilters = classFilters;
   }

   public Reflections getReflections() {
      return this.reflections;
   }

   public void setReflections(Reflections reflections) {
      this.reflections = reflections;
   }
}
