package org.hswebframework.web.authorization.basic.web;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hswebframework.web.authorization.basic.aop.AopMethodAuthorizeDefinitionParser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class WebUserTokenInterceptor extends HandlerInterceptorAdapter {
   private UserTokenManager userTokenManager;
   private List<UserTokenParser> userTokenParser;
   private AopMethodAuthorizeDefinitionParser parser;
   private boolean enableBasicAuthorization = false;

   public WebUserTokenInterceptor(UserTokenManager userTokenManager, List<UserTokenParser> userTokenParser, AopMethodAuthorizeDefinitionParser definitionParser) {
      this.userTokenManager = userTokenManager;
      this.userTokenParser = userTokenParser;
      this.parser = definitionParser;
      Stream var10001 = userTokenParser.stream();
      UserTokenForTypeParser.class.getClass();
      this.enableBasicAuthorization = var10001.filter(UserTokenForTypeParser.class::isInstance).anyMatch((parser) -> {
         return "basic".equalsIgnoreCase(((UserTokenForTypeParser)parser).getTokenType());
      });
   }

   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      List<ParsedToken> tokens = (List)this.userTokenParser.stream().map((parser) -> {
         return parser.parseToken(request);
      }).filter(Objects::nonNull).collect(Collectors.toList());
      if (tokens.isEmpty()) {
         if (this.enableBasicAuthorization && handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod)handler;
            AuthorizeDefinition definition = this.parser.parse(method.getBeanType(), method.getMethod());
            if (null != definition) {
               response.addHeader("WWW-Authenticate", " Basic realm=\"\"");
            }
         }

         return true;
      } else {
         Iterator var5 = tokens.iterator();

         while(var5.hasNext()) {
            ParsedToken parsedToken = (ParsedToken)var5.next();
            UserToken userToken = null;
            String token = parsedToken.getToken();
            if ((Boolean)this.userTokenManager.tokenIsLoggedIn(token).blockOptional().orElse(false)) {
               userToken = (UserToken)this.userTokenManager.getByToken(token).blockOptional().orElse((Object)null);
            }

            if ((userToken == null || userToken.isExpired()) && parsedToken instanceof AuthorizedToken) {
               this.userTokenManager.signOutByToken(token).subscribe();
               userToken = (UserToken)this.userTokenManager.signIn(parsedToken.getToken(), parsedToken.getType(), ((AuthorizedToken)parsedToken).getUserId(), ((AuthorizedToken)parsedToken).getMaxInactiveInterval()).block();
            }

            if (null != userToken) {
               this.userTokenManager.touch(token).subscribe();
               UserTokenHolder.setCurrent(userToken);
            }
         }

         return true;
      }
   }
}
