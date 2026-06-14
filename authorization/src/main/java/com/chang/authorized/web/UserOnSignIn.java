package org.hswebframework.web.authorization.basic.web;

import java.util.ArrayList;
import java.util.List;
import org.hswebframework.web.authorization.events.AuthorizationSuccessEvent;
import org.hswebframework.web.authorization.token.UserToken;
import org.hswebframework.web.authorization.token.UserTokenHolder;
import org.hswebframework.web.authorization.token.UserTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

public class UserOnSignIn {
   private String defaultTokenType = "sessionId";
   private UserTokenManager userTokenManager;
   private List<UserTokenGenerator> userTokenGenerators = new ArrayList();

   public UserOnSignIn(UserTokenManager userTokenManager) {
      this.userTokenManager = userTokenManager;
   }

   public void setDefaultTokenType(String defaultTokenType) {
      this.defaultTokenType = defaultTokenType;
   }

   @Autowired(
      required = false
   )
   public void setUserTokenGenerators(List<UserTokenGenerator> userTokenGenerators) {
      this.userTokenGenerators = userTokenGenerators;
   }

   @EventListener
   public void onApplicationEvent(AuthorizationSuccessEvent event) {
      UserToken token = UserTokenHolder.currentToken();
      String tokenType = (String)event.getParameter("token_type").orElse(this.defaultTokenType);
      if (token != null) {
         event.async(this.userTokenManager.signOutByToken(token.getToken()));
      }

      GeneratedToken newToken = ((UserTokenGenerator)this.userTokenGenerators.stream().filter((generator) -> {
         return generator.getSupportTokenType().equals(tokenType);
      }).findFirst().orElseThrow(() -> {
         return new UnsupportedOperationException(tokenType);
      })).generate(event.getAuthentication());
      event.async(this.userTokenManager.signIn(newToken.getToken(), newToken.getType(), event.getAuthentication().getUser().getId(), newToken.getTimeout()).then());
      event.getResult().putAll(newToken.getResponse());
   }
}
