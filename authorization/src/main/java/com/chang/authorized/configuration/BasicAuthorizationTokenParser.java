package org.hswebframework.web.authorization.basic.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.AuthenticationManager;
import org.hswebframework.web.authorization.basic.web.AuthorizedToken;
import org.hswebframework.web.authorization.basic.web.UserTokenForTypeParser;
import org.hswebframework.web.authorization.simple.PlainTextUsernamePasswordAuthenticationRequest;
import org.hswebframework.web.authorization.token.ParsedToken;
import org.hswebframework.web.authorization.token.UserToken;
import org.hswebframework.web.authorization.token.UserTokenManager;

public class BasicAuthorizationTokenParser implements UserTokenForTypeParser {
   private AuthenticationManager authenticationManager;
   private UserTokenManager userTokenManager;

   public String getTokenType() {
      return "basic";
   }

   public BasicAuthorizationTokenParser(AuthenticationManager authenticationManager, UserTokenManager userTokenManager) {
      this.authenticationManager = authenticationManager;
      this.userTokenManager = userTokenManager;
   }

   public ParsedToken parseToken(HttpServletRequest request) {
      String authorization = request.getHeader("Authorization");
      if (authorization == null) {
         return null;
      } else {
         if (authorization.contains(" ")) {
            String[] info = authorization.split("[ ]");
            if (info[0].equalsIgnoreCase(this.getTokenType())) {
               authorization = info[1];
            }
         }

         try {
            final String usernameAndPassword = new String(Base64.decodeBase64(authorization));
            UserToken token = (UserToken)this.userTokenManager.getByToken(usernameAndPassword).blockOptional().orElse((Object)null);
            if (token != null && token.isNormal()) {
               return new ParsedToken() {
                  public String getToken() {
                     return usernameAndPassword;
                  }

                  public String getType() {
                     return BasicAuthorizationTokenParser.this.getTokenType();
                  }
               };
            } else {
               if (usernameAndPassword.contains(":")) {
                  String[] arr = usernameAndPassword.split("[:]");
                  final Authentication authentication = this.authenticationManager.authenticate(new PlainTextUsernamePasswordAuthenticationRequest(arr[0], arr[1]));
                  if (authentication != null) {
                     return new AuthorizedToken() {
                        public String getUserId() {
                           return authentication.getUser().getId();
                        }

                        public String getToken() {
                           return usernameAndPassword;
                        }

                        public String getType() {
                           return BasicAuthorizationTokenParser.this.getTokenType();
                        }

                        public long getMaxInactiveInterval() {
                           return 3600000L;
                        }
                     };
                  }
               }

               return null;
            }
         } catch (Exception var7) {
            return null;
         }
      }
   }
}
