package org.hswebframework.web.authorization.basic.handler.access;

import org.springframework.http.ResponseEntity;

public class InvokeResultUtils {
   public static Object convertRealResult(Object result) {
      if (result instanceof ResponseEntity) {
         result = ((ResponseEntity)result).getBody();
      }

      return result;
   }
}
