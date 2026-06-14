package com.chang.common;

import java.lang.reflect.Field;
import org.apache.commons.lang3.ClassUtils;

public abstract class AbstractValidationInterceptor {
   protected void validate(Object target) {
      this.doValidation(target);
      Field[] fields = target.getClass().getDeclaredFields();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (!this.isChildField(field.getType())) {
            Object fieldValue = null;

            try {
               field.setAccessible(true);
               fieldValue = field.get(target);
            } catch (IllegalAccessException var9) {
               continue;
            }

            this.validate(fieldValue);
         }
      }

   }

   private boolean isChildField(Class type) {
      return type.equals(String.class) || ClassUtils.isPrimitiveOrWrapper(type);
   }

   protected abstract void doValidation(Object var1);
}
