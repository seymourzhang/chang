package com.chang.util.source.configuration;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.OutputSource;
import com.chang.util.source.configuration.config.InputSourceDoWithProperties;
import com.chang.util.source.kafka.KafkaProperties;
import com.chang.util.source.common.ManageSource;
import com.chang.util.source.common.SpringContext;
import com.chang.util.source.kafka.KafkaInput;
import com.chang.util.source.kafka.KafkaOutput;
import com.chang.util.source.mqtt.v3.MqttInput;
import com.chang.util.source.mqtt.v3.MqttOutput;
import com.chang.util.source.mqtt.v5.MqttInProperties;
import com.chang.util.source.mqtt.v5.MqttOutProperties;
import com.chang.util.source.mqtt.v5.Mqttv5Input;
import com.chang.util.source.mqtt.v5.Mqttv5Output;
import com.chang.uclass.bytecode.ClassUtils;
import com.chang.until.timeTaskApi.TimeTaskManage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class InitSource {
   private static final Logger log = LoggerFactory.getLogger(InitSource.class);
   private SourceProperties sourceProperties;
   public final Map<String, List<String>> rabbitConsumerKeys = new HashMap();

   public InitSource(SourceProperties sourceProperties) {
      this.sourceProperties = sourceProperties;
   }

   public void init() throws Exception {
       Map<String, KafkaProperties> kafkaInPutSources = this.sourceProperties.getKafkaInPutSources();
       if (MapUtil.isNotEmpty(kafkaInPutSources)) {
           Iterator var69 = kafkaInPutSources.entrySet().iterator();

           while (var69.hasNext()) {
               Map.Entry<String, KafkaProperties> kafkaIn = (Map.Entry) var69.next();
               KafkaProperties kafkaInConfig = (KafkaProperties) kafkaIn.getValue();
               if (kafkaInConfig.getPollMinRecords() == 0) {
                   break;
               }
               KafkaInput kafkaInput = new KafkaInput(kafkaInConfig.getServers(),
                   kafkaInConfig.getGroupId(),
                   kafkaInConfig.getTopic(),
                   (String) kafkaIn.getKey(),
                   kafkaInConfig.getMessageType(),
                   kafkaInConfig.getParm(),
                   kafkaInConfig.getPollMinRecords(),
                   kafkaInConfig.getPollMaxRecords(),
                   kafkaInConfig.getPollTimeout());
               ManageSource.addInputSource((String) kafkaIn.getKey(), kafkaInput);
           }
       }

       Map<String, KafkaProperties> kafkaOutPutSources = this.sourceProperties.getKafkaOutPutSources();
       if (MapUtil.isNotEmpty(kafkaOutPutSources)) {
           Iterator var73 = kafkaOutPutSources.entrySet().iterator();

           while (var73.hasNext()) {
               Map.Entry<String, KafkaProperties> kafkaOut = (Map.Entry) var73.next();
               KafkaProperties kafkaOutConfig = (KafkaProperties) kafkaOut.getValue();
               KafkaOutput kafkaOutput = new KafkaOutput(kafkaOutConfig.getServers(), kafkaOutConfig.getGroupId(), kafkaOutConfig.getTopic(), (String) kafkaOut.getKey(), kafkaOutConfig.getMessageType(), kafkaOutConfig.getParm());
               ManageSource.addOutputSource((String) kafkaOut.getKey(), kafkaOutput);
           }
       }


       Map<String, MqttInProperties> mqttInPutSources = this.sourceProperties.getMqttInPutSources();
       if (MapUtil.isNotEmpty(mqttInPutSources)) {
           Iterator var50 = mqttInPutSources.entrySet().iterator();

           while(var50.hasNext()) {
               Map.Entry<String, MqttInProperties> mqttIn = (Map.Entry)var50.next();
               MqttInProperties mqttConfig = (MqttInProperties)mqttIn.getValue();
               if (ObjectUtil.equals(mqttConfig.getVersion(), "v3")) {
                   MqttInput mqttInput = new MqttInput(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), (String)mqttIn.getKey(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.getSubscribersQos(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getNThreads(), mqttConfig.getMaximumQueueSize());
                   ManageSource.addInputSource((String)mqttIn.getKey(), mqttInput);
               } else {
                   if (!ObjectUtil.equals(mqttConfig.getVersion(), "v5")) {
                       throw new RuntimeException("Mqtt Version err");
                   }

                   Mqttv5Input mqttv5Input = new Mqttv5Input(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), (String)mqttIn.getKey(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.getSubscribersQos(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getNThreads(), mqttConfig.getMaximumQueueSize());
                   ManageSource.addInputSource((String)mqttIn.getKey(), mqttv5Input);
               }
           }
       }

       Map<String, MqttOutProperties> mqttOutPutSources = this.sourceProperties.getMqttOutPutSources();
       PublicProperties publicProperties = this.sourceProperties.getPublicConf();
       if (ObjectUtil.isNotNull(publicProperties) && ObjectUtil.isNotNull(publicProperties.getEnableMqttOutPrint())) {
           MqttOutput.setIsPrint(publicProperties.getEnableMqttOutPrint());
       }

       if (MapUtil.isNotEmpty(mqttOutPutSources)) {
           Iterator var55 = mqttOutPutSources.entrySet().iterator();

           while(var55.hasNext()) {
               Map.Entry<String, MqttOutProperties> mqttOut = (Map.Entry)var55.next();
               MqttOutProperties mqttConfig = (MqttOutProperties)mqttOut.getValue();
               if (mqttConfig.getIsCache() && StringUtils.isBlank(mqttConfig.getCacheTopic())) {
                   throw new RuntimeException("cacheTopic must not Blank");
               }

               if (ObjectUtil.equals(mqttConfig.getVersion(), "v3")) {
                   MqttOutput mqttOutput = new MqttOutput(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), mqttConfig.getIsCache(), mqttConfig.getCacheName(), mqttConfig.getCacheTopic(), mqttConfig.getBatchSize(), mqttConfig.getTimeInterval(), mqttConfig.getExpiredDataSize(), (String)mqttOut.getKey(), mqttConfig.getQueuePopSize(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getWillPayLoad(), mqttConfig.getPublishQos());
                   ManageSource.addOutputSource((String)mqttOut.getKey(), mqttOutput);
               } else {
                   if (!ObjectUtil.equals(mqttConfig.getVersion(), "v5")) {
                       throw new RuntimeException("Mqtt Version err");
                   }

                   Mqttv5Output mqttv5Output = new Mqttv5Output(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), mqttConfig.getIsCache(), mqttConfig.getCacheName(), mqttConfig.getCacheTopic(), mqttConfig.getBatchSize(), mqttConfig.getTimeInterval(), mqttConfig.getExpiredDataSize(), (String)mqttOut.getKey(), mqttConfig.getQueuePopSize(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getWillPayLoad(), mqttConfig.getPublishQos());
                   ManageSource.addOutputSource((String)mqttOut.getKey(), mqttv5Output);
               }
           }
       }

       Map<String, InputSourceDoWithProperties> doWithInPutSources = this.sourceProperties.getDoWithInPutSources();
       if (ObjectUtil.isNotNull(doWithInPutSources)) {
           Iterator var179 = doWithInPutSources.entrySet().iterator();

           while(true) {
               while(true) {
                   Map.Entry doWith;
                   InputSourceDoWithProperties doWithValue;
                   String beanName;
                   do {
                       if (!var179.hasNext()) {
                           return;
                       }

                       doWith = (Map.Entry)var179.next();
                       doWithValue = (InputSourceDoWithProperties)doWith.getValue();
                       beanName = doWithValue.getBeanName();
                   } while(!StringUtils.isNotBlank(beanName));

                   Object outputSource = SpringContext.getBean(beanName);
                   Set<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(outputSource.getClass(), new Predicate[]{(clazz) -> {
                       return clazz.equals(OutputSource.class);
                   }});
                   if (allInterfaces.size() == 0) {
                       throw new RuntimeException(outputSource.getClass().getName() + " is not implements " + OutputSource.class.getName());
                   }

                   log.info("[Source Info] beanName: " + beanName + " getInputSourceName: " + doWithValue.getInputSourceName() + " outputSourceName: " + outputSource.getClass().getName());
                   List<String> keys = (List)this.rabbitConsumerKeys.get(doWithValue.getInputSourceName());
                   if (ObjectUtil.isNotNull(keys)) {
                       Iterator var187 = keys.iterator();

                       while(var187.hasNext()) {
                           String key = (String)var187.next();
                           InPutRun inPutRun = new InPutRun((OutputSource)outputSource, key);
                           log.info((String)doWith.getKey() + "_" + key + " InPutRun");
                           TimeTaskManage.createNewTask((String)doWith.getKey() + "_" + key, inPutRun);
                           TimeUnit.SECONDS.sleep(5L);
                       }
                   } else {
                       InPutRun inPutRun = new InPutRun((OutputSource)outputSource, doWithValue.getInputSourceName());
                       log.info((String)doWith.getKey() + " InPutRun");
                       TimeTaskManage.createNewTask((String)doWith.getKey() + "_DoWith", inPutRun);
                       TimeUnit.SECONDS.sleep(5L);
                   }
               }
           }
       }
   }
}
