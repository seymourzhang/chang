package com.chang.common.enumvalid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
   validatedBy = {EnumValidtor.class}
)
@Documented
public @interface EnumValidAnnotation {
   String message() default "";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

   Class<?>[] target() default {};
}
