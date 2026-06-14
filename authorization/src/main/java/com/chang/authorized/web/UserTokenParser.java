package org.hswebframework.web.authorization.basic.web;

import jakarta.servlet.http.HttpServletRequest;
import org.hswebframework.web.authorization.token.ParsedToken;

public interface UserTokenParser {
   ParsedToken parseToken(HttpServletRequest var1);
}
