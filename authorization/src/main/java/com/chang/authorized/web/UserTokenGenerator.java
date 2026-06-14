package org.hswebframework.web.authorization.basic.web;

import org.hswebframework.web.authorization.Authentication;

public interface UserTokenGenerator {
   String TOKEN_TYPE_SESSION_ID = "sessionId";
   String TOKEN_TYPE_SIMPLE = "simple-token";

   String getSupportTokenType();

   GeneratedToken generate(Authentication var1);
}
