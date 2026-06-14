package com.chang.until.reflect;

import java.lang.reflect.Field;

public class BeanPropertySetter {
   private String fieldName;
   private Object target;
   private Field field;

   public BeanPropertySetter(Object target, String fieldName) throws NoSuchFieldException {
      this.fieldName = fieldName;
      this.target = target;
      this.field = this.findField();
   }

   public static BeanPropertySetter instance(Object target, String fieldName) throws NoSuchFieldException {
      return new BeanPropertySetter(target, fieldName);
   }

   public boolean isFieldExists() {
      return this.field != null;
   }

   public void setValue(Object value) throws IllegalAccessException {
      if (this.isFieldExists()) {
         this.field.setAccessible(true);
         this.field.set(this.target, value);
      }
   }

   public Field findField() throws NoSuchFieldException {
      Class clzz = this.target.getClass();
      Field[] fields = clzz.getDeclaredFields();

      Field dest;
      for(dest = null; !this.hasField(fields, this.fieldName) && !clzz.getName().equalsIgnoreCase("java.lang.Object"); fields = clzz.getDeclaredFields()) {
         clzz = clzz.getSuperclass();
      }

      if (!this.hasField(fields, this.fieldName)) {
         return dest;
      } else {
         dest = clzz.getDeclaredField(this.fieldName);
         return dest;
      }
   }

   public <T> T getValue() throws IllegalAccessException {
      this.field.setAccessible(true);
      return (T) this.field.get(this.target);
   }

   public Field getField() {
      return this.field;
   }

   private boolean hasField(Field[] fields, String fieldName) {
      for(int i = 0; i < fields.length; ++i) {
         if (fields[i].getName().equals(fieldName)) {
            return true;
         }
      }

      return false;
   }
}
