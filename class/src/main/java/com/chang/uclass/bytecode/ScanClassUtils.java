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

   public static List<Class<?>> scanClass(String path) {
      ClassPathScanHandler handler = new ClassPathScanHandler(new String[]{path});
      Set<Class<?>> classSet = new TreeSet(new ClassNameComparator());
      classSet.addAll(handler.getPackageAllClasses(path, true));
      return (List)((Stream)classSet.stream().parallel()).collect(Collectors.toList());
   }

}
