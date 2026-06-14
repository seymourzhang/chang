package com.chang.common.enumvalid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.chang.common.Config.ServiceInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumValidtor implements ConstraintValidator<EnumValidAnnotation, Object> {
   private static final Logger logger = LoggerFactory.getLogger(ServiceInfo.class);
   Class<?>[] cls;

   public void initialize(EnumValidAnnotation constraintAnnotation) {
      this.cls = constraintAnnotation.target();
   }

   public boolean isValid(Object value, ConstraintValidatorContext context) {
      if (null == value) {
         return true;
      } else if (this.cls.length <= 0) {
         return true;
      } else {
         Class[] var3 = this.cls;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class<?> cl = var3[var5];

            try {
               if (cl.isEnum()) {
                  Object[] objs = cl.getEnumConstants();
                  Method method = cl.getMethod("getValue");
                  Object[] var9 = objs;
                  int var10 = objs.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     Object obj = var9[var11];
                     Object code = method.invoke(obj, (Object[])null);
                     if (!ObjectUtils.notEqual(value, code)) {
                        return true;
                     }
                  }
               }
            } catch (NoSuchMethodException var14) {
               logger.error("NoSuchMethodException", var14);
            } catch (IllegalAccessException var15) {
               logger.error("IllegalAccessException", var15);
            } catch (InvocationTargetException var16) {
               logger.error("InvocationTargetException", var16);
            }
         }

         return false;
      }
   }
}
