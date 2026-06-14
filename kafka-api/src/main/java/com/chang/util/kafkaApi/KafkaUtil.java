package com.chang.util.kafkaApi;

import cn.hutool.core.collection.ListUtil;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class KafkaUtil {
   private final KafkaConsumer<String, byte[]> consumer;

   public KafkaUtil(KafkaConsumer<String, byte[]> consumer) {
      this.consumer = consumer;
   }

   public void readPartition(String topic, int partition, long readStart) {
      TopicPartition topicPartition = new TopicPartition(topic, partition);
      this.consumer.assign(ListUtil.of(new TopicPartition[]{topicPartition}));
      this.consumer.seek(topicPartition, readStart);
   }
}
