package com.chang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetLogForFun {
   LogLevel loglevel() default LogLevel.INFO;

   Class classType() default Object.class;

   String[] excludeFieldName() default {};
}
