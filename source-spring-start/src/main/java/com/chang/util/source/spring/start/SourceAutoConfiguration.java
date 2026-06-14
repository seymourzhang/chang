package com.chang.util.source.spring.start;

import com.chang.util.source.spring.start.config.BinLogProperties;
import com.chang.util.source.spring.start.config.PublicProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SourceProperties.class, PublicProperties.class, BinLogProperties.class})
public class SourceAutoConfiguration {
   private static final Logger log = LoggerFactory.getLogger(SourceAutoConfiguration.class);
   private final SourceProperties sourceProperties;

   public SourceAutoConfiguration(SourceProperties sourceProperties) {
      this.sourceProperties = sourceProperties;
   }

   @Bean
   public InitSource getInitSource() {
      return new InitSource(this.sourceProperties);
   }
}
