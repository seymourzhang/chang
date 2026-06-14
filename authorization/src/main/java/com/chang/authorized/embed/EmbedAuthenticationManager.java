package org.hswebframework.web.authorization.basic.embed;

import java.util.Optional;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.AuthenticationManager;
import org.hswebframework.web.authorization.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

@Order(Integer.MIN_VALUE)
public class EmbedAuthenticationManager implements AuthenticationManager {
   @Autowired
   private EmbedAuthenticationProperties properties;

   public Authentication authenticate(AuthenticationRequest request) {
      return this.properties.authenticate(request);
   }

   public Optional<Authentication> getByUserId(String userId) {
      return this.properties.getAuthentication(userId);
   }
}
