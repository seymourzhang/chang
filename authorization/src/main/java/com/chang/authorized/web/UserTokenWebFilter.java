package org.hswebframework.web.authorization.basic.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hswebframework.web.authorization.events.AuthorizationSuccessEvent;
import org.hswebframework.web.authorization.token.ParsedToken;
import org.hswebframework.web.authorization.token.UserTokenManager;
import org.hswebframework.web.context.ContextUtils;
import org.hswebframework.web.logger.ReactiveLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserTokenWebFilter implements WebFilter, BeanPostProcessor {
   private static final Logger log = LoggerFactory.getLogger(UserTokenWebFilter.class);
   private final List<ReactiveUserTokenParser> parsers = new ArrayList();
   private final Map<String, ReactiveUserTokenGenerator> tokenGeneratorMap = new HashMap();
   @Autowired
   private UserTokenManager userTokenManager;

   @NonNull
   public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
      return chain.filter(exchange).subscriberContext(ContextUtils.acceptContext((ctx) -> {
         Flux.fromIterable(this.parsers).flatMap((parser) -> {
            return parser.parseToken(exchange);
         }).subscribe((token) -> {
            ctx.put(ParsedToken.class, token);
         });
      })).subscriberContext(ReactiveLogger.start("requestId", exchange.getRequest().getId()));
   }

   @EventListener
   public void handleUserSign(AuthorizationSuccessEvent event) {
      Optional var10000 = event.getParameter("tokenType");
      Map var10001 = this.tokenGeneratorMap;
      var10001.getClass();
      ReactiveUserTokenGenerator generator = (ReactiveUserTokenGenerator)var10000.map(var10001::get).orElseGet(() -> {
         return (ReactiveUserTokenGenerator)this.tokenGeneratorMap.get("default");
      });
      if (generator != null) {
         GeneratedToken token = generator.generate(event.getAuthentication());
         event.getResult().putAll(token.getResponse());
         if (StringUtils.hasText(token.getToken())) {
            event.getResult().put("token", token.getToken());
            long expires = (Long)event.getParameter("expires").map(String::valueOf).map(Long::parseLong).orElse(token.getTimeout());
            event.getResult().put("expires", expires);
            event.async(this.userTokenManager.signIn(token.getToken(), token.getType(), event.getAuthentication().getUser().getId(), expires).doOnNext((t) -> {
               log.debug("user [{}] sign in", t.getUserId());
            }).then());
         }
      }

   }

   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      if (bean instanceof ReactiveUserTokenGenerator) {
         ReactiveUserTokenGenerator generator = (ReactiveUserTokenGenerator)bean;
         this.tokenGeneratorMap.put(generator.getTokenType(), generator);
      }

      if (bean instanceof ReactiveUserTokenParser) {
         this.parsers.add((ReactiveUserTokenParser)bean);
      }

      return bean;
   }
}
