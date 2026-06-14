package org.hswebframework.web.authorization.basic.aop;

import java.lang.reflect.Method;
import java.util.List;
import org.hswebframework.web.aop.MethodInterceptorContext;
import org.hswebframework.web.authorization.define.AuthorizeDefinition;

public interface AopMethodAuthorizeDefinitionParser {
   AuthorizeDefinition parse(Class<?> var1, Method var2, MethodInterceptorContext var3);

   default AuthorizeDefinition parse(Class<?> target, Method method) {
      return this.parse(target, method, (MethodInterceptorContext)null);
   }

   List<AuthorizeDefinition> getAllParsed();
}
