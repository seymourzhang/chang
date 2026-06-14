package org.hswebframework.web.authorization.basic.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hswebframework.web.authorization.token.ParsedToken;
import org.hswebframework.web.authorization.token.UserToken;
import org.hswebframework.web.authorization.token.UserTokenManager;
import org.springframework.beans.factory.annotation.Autowired;

public class SessionIdUserTokenParser implements UserTokenParser {
   protected UserTokenManager userTokenManager;

   @Autowired
   public void setUserTokenManager(UserTokenManager userTokenManager) {
      this.userTokenManager = userTokenManager;
   }

   public ParsedToken parseToken(HttpServletRequest request) {
      final HttpSession session = request.getSession(false);
      if (session != null) {
         final String sessionId = session.getId();
         UserToken token = (UserToken)this.userTokenManager.getByToken(sessionId).block();
         final long interval = (long)session.getMaxInactiveInterval();
         if (token != null && token.isExpired()) {
            final String userId = token.getUserId();
            return new AuthorizedToken() {
               public String getUserId() {
                  return userId;
               }

               public String getToken() {
                  return sessionId;
               }

               public String getType() {
                  return "sessionId";
               }

               public long getMaxInactiveInterval() {
                  return interval;
               }
            };
         } else {
            return new ParsedToken() {
               public String getToken() {
                  return session.getId();
               }

               public String getType() {
                  return "sessionId";
               }
            };
         }
      } else {
         return null;
      }
   }
}
