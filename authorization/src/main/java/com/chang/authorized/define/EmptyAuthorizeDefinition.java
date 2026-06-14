package org.hswebframework.web.authorization.basic.define;

import java.lang.reflect.Method;
import org.hswebframework.web.authorization.define.AopAuthorizeDefinition;
import org.hswebframework.web.authorization.define.DimensionsDefinition;
import org.hswebframework.web.authorization.define.Phased;
import org.hswebframework.web.authorization.define.ResourcesDefinition;

public class EmptyAuthorizeDefinition implements AopAuthorizeDefinition {
   public static EmptyAuthorizeDefinition instance = new EmptyAuthorizeDefinition();

   public ResourcesDefinition getResources() {
      throw new UnsupportedOperationException();
   }

   public DimensionsDefinition getDimensions() {
      throw new UnsupportedOperationException();
   }

   public String getMessage() {
      throw new UnsupportedOperationException();
   }

   public Phased getPhased() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return true;
   }

   public Class<?> getTargetClass() {
      throw new UnsupportedOperationException();
   }

   public Method getTargetMethod() {
      throw new UnsupportedOperationException();
   }

   private EmptyAuthorizeDefinition() {
   }
}
