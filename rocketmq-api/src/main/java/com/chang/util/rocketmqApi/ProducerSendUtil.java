package com.chang.util.rocketmqApi;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.RequestCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByMachineRoom;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByRandom;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerSendUtil {
   private static final Logger log = LoggerFactory.getLogger(ProducerSendUtil.class);
   private DefaultMQProducer defaultMQProducer;

   public ProducerSendUtil(DefaultMQProducer defaultMQProducer) {
      this.defaultMQProducer = defaultMQProducer;
   }

   public void send(String topic, String tags, String keys, byte[] body, MessageQueueSelector selector, Object orderId) throws Exception {
      Message message = new Message(topic, tags, keys, body);
      SendResult result = this.defaultMQProducer.send(message, selector, orderId);
      SendStatus status = result.getSendStatus();
      if (status != SendStatus.SEND_OK) {
         throw new RuntimeException("send Err");
      }
   }

   public void send(String topic, String tags, byte[] body, MessageQueueSelector selector, Object orderId) throws Exception {
      Message message = new Message(topic, tags, body);
      SendResult result = this.defaultMQProducer.send(message, selector, orderId);
      SendStatus status = result.getSendStatus();
      if (status != SendStatus.SEND_OK) {
         throw new RuntimeException("send Err");
      }
   }

   public void send(String topic, byte[] body, MessageQueueSelector selector, Object orderId) throws Exception {
      Message message = new Message(topic, body);
      SendResult result = this.defaultMQProducer.send(message, selector, orderId);
      SendStatus status = result.getSendStatus();
      if (status != SendStatus.SEND_OK) {
         throw new RuntimeException("send Err");
      }
   }

   public void sendByHash(String topic, String tags, byte[] body, Object hash) throws Exception {
      this.send(topic, tags, body, new SelectMessageQueueByHash(), hash);
   }

   public void sendByHash(String topic, byte[] body, Object hash) throws Exception {
      this.send(topic, body, new SelectMessageQueueByHash(), hash);
   }

   public void sendByRandom(String topic, String tags, byte[] body) throws Exception {
      this.send(topic, tags, body, new SelectMessageQueueByRandom(), (Object)null);
   }

   public void sendByMachineRoom(String topic, String tags, byte[] body) throws Exception {
      this.send(topic, tags, body, new SelectMessageQueueByMachineRoom(), (Object)null);
   }

   public void send(String topic, String tags, byte[] body) throws Exception {
      Message message = new Message(topic, tags, body);
      SendResult result = this.defaultMQProducer.send(message);
      SendStatus status = result.getSendStatus();
      if (status != SendStatus.SEND_OK) {
         throw new RuntimeException("send Err");
      }
   }

   public void send(String topic, byte[] body) throws Exception {
      Message message = new Message(topic, body);
      SendResult result = this.defaultMQProducer.send(message);
      SendStatus status = result.getSendStatus();
      if (status != SendStatus.SEND_OK) {
         throw new RuntimeException("send Err");
      }
   }

   public void request(String topic, byte[] body, long timeout) throws Exception {
      Message message = new Message(topic, body);
      this.defaultMQProducer.request(message, new RequestCallback() {
         public void onSuccess(Message message) {
         }

         public void onException(Throwable e) {
            ProducerSendUtil.log.error("request message err", e);
         }
      }, timeout);
   }

   public void request(String topic, String tags, String keys, byte[] body, MessageQueueSelector selector, Object orderId, long timeout) throws Exception {
      Message message = new Message(topic, tags, keys, body);
      this.defaultMQProducer.request(message, selector, orderId, new RequestCallback() {
         public void onSuccess(Message message) {
         }

         public void onException(Throwable e) {
            ProducerSendUtil.log.error("request message err", e);
         }
      }, timeout);
   }
}
