package com.chang.servlet.kafka.configuration;


import com.chang.servlet.kafka.configuration.config.InputSourceDoWithProperties;
import com.chang.servlet.kafka.configuration.config.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(
   prefix = "wg.data.source"
)
public class SourceProperties {
    private Map<String, KafkaProperties> kafkaOutPutSources;
    private Map<String, KafkaProperties> kafkaInPutSources;
    private Map<String, InputSourceDoWithProperties> doWithInPutSources;

    public Map<String, KafkaProperties> getKafkaOutPutSources() {
        return this.kafkaOutPutSources;
    }

    public Map<String, KafkaProperties> getKafkaInPutSources() {
        return this.kafkaInPutSources;
    }

    public Map<String, InputSourceDoWithProperties> getDoWithInPutSources() {
        return doWithInPutSources;
    }

    public void setDoWithInPutSources(Map<String, InputSourceDoWithProperties> doWithInPutSources) {
        this.doWithInPutSources = doWithInPutSources;
    }

    public void setKafkaOutPutSources(Map<String, KafkaProperties> kafkaOutPutSources) {
        this.kafkaOutPutSources = kafkaOutPutSources;
    }

    public void setKafkaInPutSources(Map<String, KafkaProperties> kafkaInPutSources) {
        this.kafkaInPutSources = kafkaInPutSources;
    }

}
