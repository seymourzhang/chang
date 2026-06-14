package org.hswebframework.web.authorization.basic.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.hswebframework.web.aop.MethodInterceptorContext;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.basic.define.DefaultBasicAuthorizeDefinition;
import org.hswebframework.web.authorization.basic.define.EmptyAuthorizeDefinition;
import org.hswebframework.web.authorization.define.AuthorizeDefinition;
import org.hswebframework.web.utils.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

public class DefaultAopMethodAuthorizeDefinitionParser implements AopMethodAuthorizeDefinitionParser {
   private static final Logger log = LoggerFactory.getLogger(DefaultAopMethodAuthorizeDefinitionParser.class);
   private final Map<CacheKey, AuthorizeDefinition> cache = new ConcurrentHashMap();
   private List<AopMethodAuthorizeDefinitionCustomizerParser> parserCustomizers;
   private static final Set<String> excludeMethodName = new HashSet(Arrays.asList("toString", "clone", "hashCode", "getClass"));

   @Autowired(
      required = false
   )
   public void setParserCustomizers(List<AopMethodAuthorizeDefinitionCustomizerParser> parserCustomizers) {
      this.parserCustomizers = parserCustomizers;
   }

   public List<AuthorizeDefinition> getAllParsed() {
      return new ArrayList(this.cache.values());
   }

   public AuthorizeDefinition parse(Class<?> target, Method method, MethodInterceptorContext context) {
      if (excludeMethodName.contains(method.getName())) {
         return null;
      } else {
         CacheKey key = this.buildCacheKey(target, method);
         AuthorizeDefinition definition = (AuthorizeDefinition)this.cache.get(key);
         if (definition instanceof EmptyAuthorizeDefinition) {
            return null;
         } else if (null != definition) {
            return definition;
         } else {
            if (!CollectionUtils.isEmpty(this.parserCustomizers)) {
               definition = (AuthorizeDefinition)this.parserCustomizers.stream().map((customizer) -> {
                  return customizer.parse(target, method, context);
               }).filter(Objects::nonNull).findAny().orElse((Object)null);
               if (definition instanceof EmptyAuthorizeDefinition) {
                  return null;
               }

               if (definition != null) {
                  return definition;
               }
            }

            Authorize annotation = (Authorize)AnnotationUtils.findAnnotation(target, method, Authorize.class);
            if (annotation != null && annotation.ignore()) {
               this.cache.put(key, EmptyAuthorizeDefinition.instance);
               return null;
            } else {
               synchronized(this.cache) {
                  return (AuthorizeDefinition)this.cache.computeIfAbsent(key, (__) -> {
                     return DefaultBasicAuthorizeDefinition.from(target, method);
                  });
               }
            }
         }
      }
   }

   public CacheKey buildCacheKey(Class<?> target, Method method) {
      return new CacheKey(ClassUtils.getUserClass(target), method);
   }

   public void destroy() {
      this.cache.clear();
   }

   static class CacheKey {
      private final Class<?> type;
      private final Method method;

      public CacheKey(Class<?> type, Method method) {
         this.type = type;
         this.method = method;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof CacheKey)) {
            return false;
         } else {
            CacheKey other = (CacheKey)o;
            if (!other.canEqual(this)) {
               return false;
            } else {
               Object this$type = this.type;
               Object other$type = other.type;
               if (this$type == null) {
                  if (other$type != null) {
                     return false;
                  }
               } else if (!this$type.equals(other$type)) {
                  return false;
               }

               Object this$method = this.method;
               Object other$method = other.method;
               if (this$method == null) {
                  if (other$method != null) {
                     return false;
                  }
               } else if (!this$method.equals(other$method)) {
                  return false;
               }

               return true;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof CacheKey;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $type = this.type;
         result = result * 59 + ($type == null ? 43 : $type.hashCode());
         Object $method = this.method;
         result = result * 59 + ($method == null ? 43 : $method.hashCode());
         return result;
      }
   }
}
