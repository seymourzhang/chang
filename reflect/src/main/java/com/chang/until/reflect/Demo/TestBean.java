package com.chang.until.reflect.Demo;

import com.chang.until.reflect.ReflectionUtils;

public class TestBean {
   public static void main(String[] args) throws Exception {
      while(true) {
         ReflectionUtils reflectionUtils = new ReflectionUtils();
         Class<?> calzz = reflectionUtils.readClassFile("D:/Test.class", "com.chang.until.reflect.Demo.Test");
         Object oo = ReflectionUtils.getNew(calzz);
         ReflectionUtils.callMethod(oo, "fun", (Object[])null);
         Thread.sleep(3000L);
      }
   }
}
