package org.hswebframework.web.authorization.basic.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.hswebframework.web.authorization.define.AuthorizingContext;
import org.hswebframework.web.authorization.define.HandleType;
import org.hswebframework.web.authorization.events.AuthorizingHandleBeforeEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;

@ConfigurationProperties("hsweb.authorize")
public class UserAllowPermissionHandler {
   private Map<String, Map<String, String>> allows = new HashMap();
   private PathMatcher pathMatcher = new AntPathMatcher(".");

   @EventListener
   public void handEvent(AuthorizingHandleBeforeEvent event) {
      if (!this.allows.isEmpty() && event.getHandleType() != HandleType.DATA) {
         AuthorizingContext context = event.getContext();
         String path = ClassUtils.getUserClass(context.getParamContext().getTarget()).getName().concat(".").concat(context.getParamContext().getMethod().getName());
         AtomicBoolean allow = new AtomicBoolean();
         Iterator var5 = this.allows.entrySet().iterator();

         do {
            if (!var5.hasNext()) {
               return;
            }

            Map.Entry<String, Map<String, String>> entry = (Map.Entry)var5.next();
            String dimension = (String)entry.getKey();
            if ("user".equals(dimension)) {
               String userId = context.getAuthentication().getUser().getId();
               allow.set(Optional.ofNullable(((Map)entry.getValue()).get(userId)).filter((pattern) -> {
                  return "*".equals(pattern) || this.pathMatcher.match(pattern, path);
               }).isPresent());
            } else {
               Iterator var8 = ((Map)entry.getValue()).entrySet().iterator();

               while(var8.hasNext()) {
                  Map.Entry<String, String> confEntry = (Map.Entry)var8.next();
                  context.getAuthentication().getDimension(dimension, (String)confEntry.getKey()).ifPresent((dim) -> {
                     String pattern = (String)confEntry.getValue();
                     allow.set("*".equals(pattern) || this.pathMatcher.match((String)confEntry.getValue(), path));
                  });
               }
            }
         } while(!allow.get());

         event.setAllow(true);
      }
   }

   public Map<String, Map<String, String>> getAllows() {
      return this.allows;
   }

   public void setAllows(Map<String, Map<String, String>> allows) {
      this.allows = allows;
   }
}
