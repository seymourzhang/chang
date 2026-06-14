package org.hswebframework.web.authorization.basic.embed;

import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.AuthenticationRequest;
import org.hswebframework.web.authorization.ReactiveAuthenticationManagerProvider;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Order(10)
public class EmbedReactiveAuthenticationManager implements ReactiveAuthenticationManagerProvider {
   private EmbedAuthenticationProperties properties;

   public Mono<Authentication> authenticate(Mono<AuthenticationRequest> request) {
      EmbedAuthenticationProperties var10001 = this.properties;
      var10001.getClass();
      return request.map(var10001::authenticate);
   }

   public Mono<Authentication> getByUserId(String userId) {
      return Mono.justOrEmpty(this.properties.getAuthentication(userId));
   }

   public EmbedReactiveAuthenticationManager(EmbedAuthenticationProperties properties) {
      this.properties = properties;
   }
}
