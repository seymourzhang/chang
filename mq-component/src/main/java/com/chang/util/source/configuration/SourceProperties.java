package com.chang.util.source.configuration;


import com.chang.util.source.configuration.config.InputSourceDoWithProperties;
import com.chang.util.source.kafka.KafkaProperties;
import com.chang.util.source.mqtt.v5.MqttInProperties;
import com.chang.util.source.mqtt.v5.MqttOutProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(
   prefix = "wg.data.source"
)
public class SourceProperties {
    private PublicProperties publicConf;
    private Map<String, KafkaProperties> kafkaOutPutSources;
    private Map<String, KafkaProperties> kafkaInPutSources;
    private Map<String, MqttOutProperties> mqttOutPutSources;
    private Map<String, MqttInProperties> mqttInPutSources;
    private Map<String, InputSourceDoWithProperties> doWithInPutSources;

    public PublicProperties getPublicConf() {
        return publicConf;
    }

    public void setPublicConf(PublicProperties publicConf) {
        this.publicConf = publicConf;
    }

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

    public Map<String, MqttOutProperties> getMqttOutPutSources() {
        return mqttOutPutSources;
    }

    public void setMqttOutPutSources(Map<String, MqttOutProperties> mqttOutPutSources) {
        this.mqttOutPutSources = mqttOutPutSources;
    }

    public Map<String, MqttInProperties> getMqttInPutSources() {
        return mqttInPutSources;
    }

    public void setMqttInPutSources(Map<String, MqttInProperties> mqttInPutSources) {
        this.mqttInPutSources = mqttInPutSources;
    }
}
