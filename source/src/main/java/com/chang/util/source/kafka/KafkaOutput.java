package com.chang.util.source.kafka;

import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.common.Util;
import com.chang.util.source.manage.ManageStatistics;
import com.chang.util.kafkaApi.KafkaFactory;
import com.chang.util.kafkaApi.KafkaOptions;
import com.chang.util.source.OutputSource;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(KafkaOutput.class);
   private final KafkaProducer<String, byte[]> producer;
   private final KafkaOptions kafkaOptions;
   private String messageType;
   private ThreadLocal<String> keyThreadLocal = new ThreadLocal();
   private Map<String, Object> parm;

   public KafkaOutput(String servers, String groupId, String topic, String keyName, String messageType, Map<String, Object> parm) {
      this.messageType = messageType;
      this.parm = parm;
      this.kafkaOptions = KafkaOptions.builder().servers(servers).groupId(groupId).topic(topic).keyName(keyName).build();
      this.producer = KafkaFactory.getProducer(this.kafkaOptions);
   }

   public void setKey(String keyValue) {
      this.keyThreadLocal.set(keyValue);
   }

   public void Output(Object o) {
      String keySend = (String)this.keyThreadLocal.get();
      ProducerRecord record;
      if (StringUtils.isNoneBlank(new CharSequence[]{keySend})) {
         record = new ProducerRecord(this.kafkaOptions.getTopic(), keySend, Util.getSendData(this.messageType, o));
      } else {
         record = new ProducerRecord(this.kafkaOptions.getTopic(), Util.getSendData(this.messageType, o));
      }

      try {
         this.producer.send(record, (metadata, exception) -> {
            if (ObjectUtil.isNotNull(exception)) {
               log.error("Send Error", exception);
            } else {
               log.info("Send OK!");
            }

         }).get();
      } catch (Exception var5) {
         log.error("Send Error", var5);
         throw new RuntimeException(var5);
      }

      ManageStatistics.outMessageAdd(this.kafkaOptions.getKeyName());
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      this.producer.close();
   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public KafkaOptions getKafkaOptions() {
      return this.kafkaOptions;
   }
}
