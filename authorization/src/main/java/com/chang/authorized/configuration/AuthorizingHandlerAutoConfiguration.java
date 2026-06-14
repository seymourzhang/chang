package org.hswebframework.web.authorization.basic.configuration;

import java.util.List;
import org.hswebframework.web.authorization.AuthenticationManager;
import org.hswebframework.web.authorization.ReactiveAuthenticationManagerProvider;
import org.hswebframework.web.authorization.access.DataAccessController;
import org.hswebframework.web.authorization.access.DataAccessHandler;
import org.hswebframework.web.authorization.basic.aop.AopMethodAuthorizeDefinitionParser;
import org.hswebframework.web.authorization.basic.embed.EmbedAuthenticationProperties;
import org.hswebframework.web.authorization.basic.embed.EmbedReactiveAuthenticationManager;
import org.hswebframework.web.authorization.basic.handler.DefaultAuthorizingHandler;
import org.hswebframework.web.authorization.basic.handler.UserAllowPermissionHandler;
import org.hswebframework.web.authorization.basic.handler.access.DefaultDataAccessController;
import org.hswebframework.web.authorization.basic.twofactor.TwoFactorHandlerInterceptorAdapter;
import org.hswebframework.web.authorization.basic.web.AuthorizationController;
import org.hswebframework.web.authorization.basic.web.DefaultUserTokenGenPar;
import org.hswebframework.web.authorization.basic.web.ReactiveUserTokenController;
import org.hswebframework.web.authorization.basic.web.SessionIdUserTokenGenerator;
import org.hswebframework.web.authorization.basic.web.SessionIdUserTokenParser;
import org.hswebframework.web.authorization.basic.web.UserOnSignIn;
import org.hswebframework.web.authorization.basic.web.UserOnSignOut;
import org.hswebframework.web.authorization.basic.web.UserTokenForTypeParser;
import org.hswebframework.web.authorization.basic.web.UserTokenParser;
import org.hswebframework.web.authorization.basic.web.UserTokenWebFilter;
import org.hswebframework.web.authorization.basic.web.WebUserTokenInterceptor;
import org.hswebframework.web.authorization.token.UserTokenManager;
import org.hswebframework.web.authorization.twofactor.TwoFactorValidatorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableConfigurationProperties({EmbedAuthenticationProperties.class})
public class AuthorizingHandlerAutoConfiguration {
   @Bean
   public DefaultDataAccessController dataAccessController() {
      return new DefaultDataAccessController();
   }

   @Bean
   public DefaultAuthorizingHandler authorizingHandler(DataAccessController dataAccessController) {
      return new DefaultAuthorizingHandler(dataAccessController);
   }

   @Bean
   @ConditionalOnWebApplication(
      type = Type.REACTIVE
   )
   public UserTokenWebFilter userTokenWebFilter() {
      return new UserTokenWebFilter();
   }

   @Bean
   public ReactiveAuthenticationManagerProvider embedAuthenticationManager(EmbedAuthenticationProperties properties) {
      return new EmbedReactiveAuthenticationManager(properties);
   }

   @Bean
   public UserAllowPermissionHandler userAllowPermissionHandler() {
      return new UserAllowPermissionHandler();
   }

   @Bean
   @ConditionalOnWebApplication(
      type = Type.REACTIVE
   )
   @ConfigurationProperties(
      prefix = "hsweb.authorize.token.default"
   )
   public DefaultUserTokenGenPar defaultUserTokenGenPar() {
      return new DefaultUserTokenGenPar();
   }

   @Bean
   public AuthorizationController authorizationController() {
      return new AuthorizationController();
   }

   @Bean
   @ConditionalOnWebApplication(
      type = Type.REACTIVE
   )
   public ReactiveUserTokenController userTokenController() {
      return new ReactiveUserTokenController();
   }

   @Configuration
   @ConditionalOnProperty(
      prefix = "hsweb.authorize",
      name = {"basic-authorization"},
      havingValue = "true"
   )
   @ConditionalOnClass({UserTokenForTypeParser.class})
   @ConditionalOnWebApplication(
      type = Type.SERVLET
   )
   public static class BasicAuthorizationConfiguration {
      @Bean
      public BasicAuthorizationTokenParser basicAuthorizationTokenParser(AuthenticationManager authenticationManager, UserTokenManager tokenManager) {
         return new BasicAuthorizationTokenParser(authenticationManager, tokenManager);
      }
   }

   @Configuration
   public static class DataAccessHandlerProcessor implements BeanPostProcessor {
      @Autowired
      private DefaultDataAccessController defaultDataAccessController;

      public Object postProcessBeforeInitialization(Object bean, String beanName) {
         return bean;
      }

      public Object postProcessAfterInitialization(Object bean, String beanName) {
         if (bean instanceof DataAccessHandler) {
            this.defaultDataAccessController.addHandler((DataAccessHandler)bean);
         }

         return bean;
      }
   }

   @Configuration
   @ConditionalOnClass(
      name = {"org.springframework.web.servlet.config.annotation.WebMvcConfigurer"}
   )
   @ConditionalOnWebApplication(
      type = Type.SERVLET
   )
   static class WebMvcAuthorizingConfiguration {
      @Bean
      @Order(Integer.MIN_VALUE)
      public WebMvcConfigurer webUserTokenInterceptorConfigurer(final UserTokenManager userTokenManager, final AopMethodAuthorizeDefinitionParser parser, final List<UserTokenParser> userTokenParser) {
         return new WebMvcConfigurer() {
            public void addInterceptors(InterceptorRegistry registry) {
               registry.addInterceptor(new WebUserTokenInterceptor(userTokenManager, userTokenParser, parser));
            }
         };
      }

      @Bean
      public UserOnSignIn userOnSignIn(UserTokenManager userTokenManager) {
         return new UserOnSignIn(userTokenManager);
      }

      @Bean
      public UserOnSignOut userOnSignOut(UserTokenManager userTokenManager) {
         return new UserOnSignOut(userTokenManager);
      }

      @Bean
      @ConditionalOnMissingBean({UserTokenParser.class})
      public UserTokenParser userTokenParser() {
         return new SessionIdUserTokenParser();
      }

      @Bean
      public SessionIdUserTokenGenerator sessionIdUserTokenGenerator() {
         return new SessionIdUserTokenGenerator();
      }

      @Bean
      @ConditionalOnProperty(
         prefix = "hsweb.authorize.two-factor",
         name = {"enable"},
         havingValue = "true"
      )
      @Order(100)
      public WebMvcConfigurer twoFactorHandlerConfigurer(final TwoFactorValidatorManager manager) {
         return new WebMvcConfigurerAdapter() {
            public void addInterceptors(InterceptorRegistry registry) {
               registry.addInterceptor(new TwoFactorHandlerInterceptorAdapter(manager));
               super.addInterceptors(registry);
            }
         };
      }
   }
}
