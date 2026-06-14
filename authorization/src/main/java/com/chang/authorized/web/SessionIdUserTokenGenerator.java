package org.hswebframework.web.authorization.basic.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.utils.WebUtils;

public class SessionIdUserTokenGenerator implements UserTokenGenerator, Serializable {
   private static final long serialVersionUID = -9197243220777237431L;

   public String getSupportTokenType() {
      return "sessionId";
   }

   public GeneratedToken generate(Authentication authentication) {
      HttpServletRequest request = WebUtils.getHttpServletRequest();
      if (null == request) {
         throw new UnsupportedOperationException();
      } else {
         final int timeout = request.getSession().getMaxInactiveInterval() * 1000;
         final String sessionId = request.getSession().getId();
         return new GeneratedToken() {
            private static final long serialVersionUID = 3964183451883410929L;

            public Map<String, Object> getResponse() {
               return new HashMap();
            }

            public String getToken() {
               return sessionId;
            }

            public String getType() {
               return "sessionId";
            }

            public long getTimeout() {
               return (long)timeout;
            }
         };
      }
   }
}
