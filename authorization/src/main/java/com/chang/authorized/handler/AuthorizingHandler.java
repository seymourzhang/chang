package org.hswebframework.web.authorization.basic.handler;

import org.hswebframework.web.authorization.define.AuthorizingContext;

public interface AuthorizingHandler {
   void handRBAC(AuthorizingContext var1);

   void handleDataAccess(AuthorizingContext var1);

   default void handle(AuthorizingContext context) {
      this.handRBAC(context);
      this.handleDataAccess(context);
   }
}
