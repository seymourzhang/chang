package com.chang.util.source.manage;

import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageSource {
   private static final Map<String, InputSource> inputSourceMapMap = new ConcurrentHashMap();
   private static final Map<String, OutputSource> outputSourceMap = new ConcurrentHashMap();

   public static void addInputSource(String name, InputSource inputSource) {
      if (!inputSourceMapMap.containsKey(name)) {
         inputSourceMapMap.put(name, inputSource);
      } else {
         throw new RuntimeException("InputSource already has name: " + name);
      }
   }

   public static void addOutputSource(String name, OutputSource outputSource) {
      if (!outputSourceMap.containsKey(name)) {
         outputSourceMap.put(name, outputSource);
      } else {
         throw new RuntimeException("OutputSource already has name: " + name);
      }
   }

   public static InputSource getInputSource(String name) {
      return (InputSource)inputSourceMapMap.get(name);
   }

   public static OutputSource getOutputSource(String name) {
      return (OutputSource)outputSourceMap.get(name);
   }

   public static boolean existInputSource(String name) {
      return inputSourceMapMap.containsKey(name);
   }

   public static boolean existOutputSource(String name) {
      return outputSourceMap.containsKey(name);
   }

   public static void closeInputSource(String name) {
      ((InputSource)inputSourceMapMap.get(name)).close();
      inputSourceMapMap.remove(name);
   }

   public static void closeOutputSource(String name) {
      ((OutputSource)outputSourceMap.get(name)).close();
      outputSourceMap.remove(name);
   }
}
