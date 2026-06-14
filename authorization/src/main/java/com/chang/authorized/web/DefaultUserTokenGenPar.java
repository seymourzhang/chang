package org.hswebframework.web.authorization.basic.web;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.token.ParsedToken;
import org.hswebframework.web.id.IDGenerator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class DefaultUserTokenGenPar implements ReactiveUserTokenGenerator, ReactiveUserTokenParser {
   private long timeout;
   private String headerName;

   public DefaultUserTokenGenPar() {
      this.timeout = TimeUnit.MINUTES.toMillis(30L);
      this.headerName = "X-Access-Token";
   }

   public String getTokenType() {
      return "default";
   }

   public GeneratedToken generate(Authentication authentication) {
      final String token = (String)IDGenerator.MD5.generate();
      return new GeneratedToken() {
         public Map<String, Object> getResponse() {
            return Collections.singletonMap("expires", DefaultUserTokenGenPar.this.timeout);
         }

         public String getToken() {
            return token;
         }

         public String getType() {
            return DefaultUserTokenGenPar.this.getTokenType();
         }

         public long getTimeout() {
            return DefaultUserTokenGenPar.this.timeout;
         }
      };
   }

   public Mono<ParsedToken> parseToken(ServerWebExchange exchange) {
      final String token = (String)Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(this.headerName)).orElseGet(() -> {
         return (String)exchange.getRequest().getQueryParams().getFirst(":X_Access_Token");
      });
      return token == null ? Mono.empty() : Mono.just(new ParsedToken() {
         public String getToken() {
            return token;
         }

         public String getType() {
            return DefaultUserTokenGenPar.this.getTokenType();
         }
      });
   }

   public long getTimeout() {
      return this.timeout;
   }

   public String getHeaderName() {
      return this.headerName;
   }

   public void setTimeout(long timeout) {
      this.timeout = timeout;
   }

   public void setHeaderName(String headerName) {
      this.headerName = headerName;
   }
}
