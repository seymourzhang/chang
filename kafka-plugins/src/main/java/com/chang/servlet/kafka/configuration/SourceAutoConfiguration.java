package com.chang.servlet.kafka.configuration;

import cn.hutool.core.map.MapUtil;
import com.chang.servlet.kafka.configuration.config.KafkaProperties;
import com.chang.servlet.kafka.kafka.KafkaOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({SourceProperties.class})
public class SourceAutoConfiguration {
   private static final Logger log = LoggerFactory.getLogger(SourceAutoConfiguration.class);
   private final SourceProperties sourceProperties;

   public SourceAutoConfiguration(SourceProperties sourceProperties) {
      this.sourceProperties = sourceProperties;
   }

   @Bean
   @ConditionalOnMissingBean
   public InitSource getInitSource() {
      return new InitSource(this.sourceProperties);
   }

   @Bean
   @ConditionalOnMissingBean
   public KafkaOutput getKafkaOutput() {
       Map<String, KafkaProperties> kafkaOutPutSources = this.sourceProperties.getKafkaOutPutSources();
       if (MapUtil.isNotEmpty(kafkaOutPutSources)) {
           Iterator var73 = kafkaOutPutSources.entrySet().iterator();

           while (var73.hasNext()) {
               Map.Entry<String, KafkaProperties> kafkaOut = (Map.Entry) var73.next();
               KafkaProperties kafkaOutConfig = (KafkaProperties) kafkaOut.getValue();
               return new KafkaOutput(kafkaOutConfig.getServers(), kafkaOutConfig.getGroupId(), kafkaOutConfig.getTopic(), (String) kafkaOut.getKey(), kafkaOutConfig.getMessageType(), kafkaOutConfig.getParm());
           }
       }
       return null;
   }
}
