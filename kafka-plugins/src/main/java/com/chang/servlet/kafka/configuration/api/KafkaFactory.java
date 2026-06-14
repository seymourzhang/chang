package com.chang.servlet.kafka.configuration.api;

import org.apache.kafka.clients.consumer.GroupProtocol;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaFactory {
   public static KafkaConsumer<String, byte[]> getConsumer(KafkaOptions kafkaOptions) {
      Properties properties = new Properties();
      properties.put("bootstrap.servers", kafkaOptions.getServers());
      properties.put("group.id", kafkaOptions.getGroupId());
      properties.put("group.protocol", GroupProtocol.CONSUMER);
      properties.put("enable.auto.commit", true);
      properties.put("max.poll.records", 500);
      properties.put("max.poll.interval.ms", 30000);
      properties.put("kafka.concurrency", 2);
      properties.put("kafka.type", "batch");
      properties.put("fetch.max.bytes", 52428800);
      properties.put("auto.commit.interval.ms", 1000);
      properties.put("session.timeout.ms", 10000);
      properties.put("heartbeat.interval.ms", 3000);
      properties.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.StickyAssignor");
      properties.put("auto.offset.reset", "earliest");
      properties.put("key.deserializer", StringDeserializer.class.getName());
      properties.put("value.deserializer", ByteArrayDeserializer.class.getName());
      properties.put("max.partition.fetch.bytes", "52428800");
      return new KafkaConsumer(properties);
   }

   public static KafkaProducer<String, byte[]> getProducer(KafkaOptions kafkaOptions) {
      Properties properties = new Properties();
      properties.put("bootstrap.servers", kafkaOptions.getServers());
//      properties.put("group.id", kafkaOptions.getGroupId());
      properties.put("key.serializer", StringSerializer.class.getName());
      properties.put("value.serializer", ByteArraySerializer.class.getName());
      properties.put("max.request.size", "52428800");
      return new KafkaProducer(properties);
   }
}
