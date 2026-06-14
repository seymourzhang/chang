package com.chang.util.source.kafka;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import com.chang.util.source.manage.ManageStatistics;
import com.chang.util.kafkaApi.KafkaFactory;
import com.chang.util.kafkaApi.KafkaOptions;
import com.chang.until.timeTaskApi.TimeTaskManage;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaInput implements InputSource<JSONObject, Object> {
   private static final Logger log = LoggerFactory.getLogger(KafkaInput.class);
   private final KafkaConsumer<String, byte[]> consumer;
   private final KafkaOptions kafkaOptions;
   private final String messageType;
   private Map<String, Object> parm;
   private final String keyName;

   public KafkaInput(String servers, String groupId, String topic, String keyName, String messageType, Map<String, Object> parm) {
      this.messageType = messageType;
      this.parm = parm;
      this.keyName = keyName;
      this.kafkaOptions = KafkaOptions.builder().servers(servers).groupId(groupId).topic(topic).keyName(keyName).build();
      this.consumer = KafkaFactory.getConsumer(this.kafkaOptions);
   }

   public void InPut(final OutputSource source, final Function<JSONObject, Object> function) {
      this.consumer.subscribe(Collections.singletonList(this.kafkaOptions.getTopic()));

      try {
         TimeTaskManage.createNewTask(this.keyName, new Runnable() {
            public void run() {
               try {
                  while(true) {
                     try {
                        SourceContext.setExParm(KafkaInput.this.parm);
                        ConsumerRecords<String, byte[]> consumerRecords = KafkaInput.this.consumer.poll(Duration.ofSeconds(1L));
                        Iterator<ConsumerRecord<String, byte[]>> recordIterator = consumerRecords.iterator();

                        while(recordIterator.hasNext()) {
                           ManageStatistics.inMessageAdd(KafkaInput.this.kafkaOptions.getKeyName());
                           ConsumerRecord<String, byte[]> record = (ConsumerRecord)recordIterator.next();
                           String key = (String)record.key();
                           Object revData = Util.getRevData(KafkaInput.this.messageType, (byte[])record.value());
                           long offset = record.offset();
                           int partition = record.partition();
                           JSONObject jsonObject = new JSONObject();
                           jsonObject.put("message", revData);
                           jsonObject.put("offset", offset);
                           jsonObject.put("partition", partition);
                           jsonObject.put("key", key);
                           if (ObjectUtil.isNotNull(function)) {
                              source.Output(function.apply(jsonObject));
                           } else {
                              source.Output(jsonObject);
                           }
                        }

                        SourceContext.clearExParm();
                     } catch (Throwable var10) {
                        KafkaInput.log.error("KafkaInput ERR: ", var10);
                     }
                  }
               } catch (Throwable var11) {
                  throw var11;
               }
            }
         });
      } catch (Exception var4) {
         log.error("KafkaInput ERR: ", var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      TimeTaskManage.stopTask(this.keyName);
      TimeTaskManage.clearDoneTask();
      this.consumer.close();
   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public KafkaOptions getKafkaOptions() {
      return this.kafkaOptions;
   }
}
