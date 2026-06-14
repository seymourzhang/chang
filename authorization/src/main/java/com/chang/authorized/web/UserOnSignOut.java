package org.hswebframework.web.authorization.basic.web;

import org.hswebframework.web.authorization.events.AuthorizationExitEvent;
import org.hswebframework.web.authorization.token.UserToken;
import org.hswebframework.web.authorization.token.UserTokenHolder;
import org.hswebframework.web.authorization.token.UserTokenManager;
import org.springframework.context.ApplicationListener;

public class UserOnSignOut implements ApplicationListener<AuthorizationExitEvent> {
   private UserTokenManager userTokenManager;

   public UserOnSignOut(UserTokenManager userTokenManager) {
      this.userTokenManager = userTokenManager;
   }

   private String geToken() {
      UserToken token = UserTokenHolder.currentToken();
      return null != token ? token.getToken() : "";
   }

   public void onApplicationEvent(AuthorizationExitEvent event) {
      this.userTokenManager.signOutByToken(this.geToken());
   }
}
