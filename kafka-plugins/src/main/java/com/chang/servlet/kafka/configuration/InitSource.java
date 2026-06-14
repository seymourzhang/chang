package com.chang.servlet.kafka.configuration;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.servlet.kafka.common.SpringContext;
import com.chang.servlet.kafka.OutputSource;
import com.chang.servlet.kafka.common.ManageSource;
import com.chang.servlet.kafka.configuration.config.InputSourceDoWithProperties;
import com.chang.servlet.kafka.configuration.config.KafkaProperties;
import com.chang.servlet.kafka.kafka.KafkaInput;
import com.chang.servlet.kafka.kafka.KafkaOutput;
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
               KafkaInput kafkaInput = new KafkaInput(kafkaInConfig.getServers(), kafkaInConfig.getGroupId(), kafkaInConfig.getTopic(), (String) kafkaIn.getKey(), kafkaInConfig.getMessageType(), kafkaInConfig.getParm());
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
