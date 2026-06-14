package com.chang.uclass.bytecode;

import java.util.Properties;

public class JavaVersionUtils {
   private static final String VERSION_PROP_NAME = "java.specification.version";
   private static final String JAVA_VERSION_STR = System.getProperty("java.specification.version");
   private static final float JAVA_VERSION;

   private JavaVersionUtils() {
   }

   public static String javaVersionStr() {
      return JAVA_VERSION_STR;
   }

   public static String javaVersionStr(Properties props) {
      return null != props ? props.getProperty("java.specification.version") : null;
   }

   public static float javaVersion() {
      return JAVA_VERSION;
   }

   public static boolean isJava6() {
      return JAVA_VERSION_STR.equals("1.6");
   }

   public static boolean isJava7() {
      return JAVA_VERSION_STR.equals("1.7");
   }

   public static boolean isJava8() {
      return JAVA_VERSION_STR.equals("1.8");
   }

   public static boolean isJava9() {
      return JAVA_VERSION_STR.equals("9");
   }

   public static boolean isLessThanJava9() {
      return JAVA_VERSION < 9.0F;
   }

   public static boolean isGreaterThanJava7() {
      return JAVA_VERSION > 1.7F;
   }

   public static boolean isGreaterThanJava8() {
      return JAVA_VERSION > 1.8F;
   }

   public static boolean isGreaterThanJava11() {
      return JAVA_VERSION > 11.0F;
   }

   static {
      JAVA_VERSION = Float.parseFloat(JAVA_VERSION_STR);
   }
}
