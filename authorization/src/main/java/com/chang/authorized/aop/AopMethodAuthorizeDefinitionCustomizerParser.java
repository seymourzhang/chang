package org.hswebframework.web.authorization.basic.aop;

import java.lang.reflect.Method;
import org.hswebframework.web.aop.MethodInterceptorContext;
import org.hswebframework.web.authorization.define.AuthorizeDefinition;

public interface AopMethodAuthorizeDefinitionCustomizerParser {
   AuthorizeDefinition parse(Class<?> var1, Method var2, MethodInterceptorContext var3);
}
