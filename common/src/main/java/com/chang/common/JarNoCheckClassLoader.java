package com.chang.common;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;

public class JarNoCheckClassLoader {
   private static List<File> loopJar(File file) {
      return FileUtil.loopFiles(file, (FileFilter)null);
   }

   public static void loadJar(ClassLoader loader, File jarFile) throws UtilException {
      try {
         Method method = ClassUtil.getDeclaredMethod(URLClassLoader.class, "addURL", new Class[]{URL.class});
         if (null != method) {
            method.setAccessible(true);
            List<File> jars = loopJar(jarFile);
            Iterator var4 = jars.iterator();

            while(var4.hasNext()) {
               File jar = (File)var4.next();
               ReflectUtil.invoke(loader, method, new Object[]{jar.toURI().toURL()});
            }
         }

      } catch (IOException var6) {
         throw new UtilException(var6);
      }
   }

   public static void loadJarToSystemClassLoader(File jarFile) {
      ClassLoader classLoader = JarNoCheckClassLoader.class.getClassLoader();
      loadJar(classLoader, jarFile);
   }
}
