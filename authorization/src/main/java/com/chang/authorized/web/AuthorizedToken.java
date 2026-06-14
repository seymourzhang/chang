package org.hswebframework.web.authorization.basic.web;

import org.hswebframework.web.authorization.token.ParsedToken;

public interface AuthorizedToken extends ParsedToken {
   String getUserId();

   default long getMaxInactiveInterval() {
      return -1L;
   }
}
