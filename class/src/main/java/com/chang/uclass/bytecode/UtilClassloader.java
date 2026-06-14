package com.chang.uclass.bytecode;

import java.net.URL;
import java.net.URLClassLoader;

public class UtilClassloader extends URLClassLoader {
   public UtilClassloader(URL[] urls) {
      super(urls, ClassLoader.getSystemClassLoader().getParent());
   }

   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class<?> loadedClass = this.findLoadedClass(name);
      if (loadedClass != null) {
         return loadedClass;
      } else if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
         return super.loadClass(name, resolve);
      } else {
         try {
            Class<?> aClass = this.findClass(name);
            if (resolve) {
               this.resolveClass(aClass);
            }

            return aClass;
         } catch (Exception var5) {
            return super.loadClass(name, resolve);
         }
      }
   }
}
