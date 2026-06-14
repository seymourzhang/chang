package com.chang.util.rocketmqApi;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class RocketFactory {
   public static DefaultMQPushConsumer getConsumer(RocketMqOptions rocketMqOptions) throws MQClientException {
      DefaultMQPushConsumer consumer;
      if (!StringUtils.isBlank(rocketMqOptions.getAccessKey()) && !StringUtils.isBlank(rocketMqOptions.getSecretKey())) {
         AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(rocketMqOptions.getAccessKey(), rocketMqOptions.getSecretKey()));
         consumer = new DefaultMQPushConsumer(rocketMqOptions.getNamespace(), rocketMqOptions.getGroup(), aclClientRPCHook);
      } else {
         consumer = new DefaultMQPushConsumer(rocketMqOptions.getNamespace(), rocketMqOptions.getGroup());
      }

      consumer.setNamesrvAddr(rocketMqOptions.getNameSrvAddr());
      if (StringUtils.isBlank(rocketMqOptions.getSubExpression())) {
         consumer.subscribe(rocketMqOptions.getTopic(), "*");
      } else {
         consumer.subscribe(rocketMqOptions.getTopic(), rocketMqOptions.getSubExpression());
      }

      consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
      consumer.setConsumeMessageBatchMaxSize(rocketMqOptions.getMessageBatchMaxSize());
      consumer.setConsumeThreadMax(rocketMqOptions.getConsumeThread());
      consumer.setConsumeThreadMin(rocketMqOptions.getConsumeThread());
      if (rocketMqOptions.getMessageModel().equals("BROADCASTING")) {
         consumer.setMessageModel(MessageModel.BROADCASTING);
      } else {
         if (!rocketMqOptions.getMessageModel().equals("CLUSTERING")) {
            throw new RuntimeException("MessageModel is err");
         }

         consumer.setMessageModel(MessageModel.CLUSTERING);
      }

      return consumer;
   }

   public static DefaultMQProducer getProducer(RocketMqOptions rocketMqOptions) throws MQClientException {
      DefaultMQProducer producer;
      if (!StringUtils.isBlank(rocketMqOptions.getAccessKey()) && !StringUtils.isBlank(rocketMqOptions.getSecretKey())) {
         AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(rocketMqOptions.getAccessKey(), rocketMqOptions.getSecretKey()));
         producer = new DefaultMQProducer(rocketMqOptions.getNamespace(), rocketMqOptions.getGroup(), aclClientRPCHook);
      } else {
         producer = new DefaultMQProducer(rocketMqOptions.getNamespace(), rocketMqOptions.getGroup());
      }

      producer.setNamesrvAddr(rocketMqOptions.getNameSrvAddr());
      producer.setSendMsgTimeout(rocketMqOptions.getSendMsgTimeout());
      producer.setRetryTimesWhenSendFailed(3);
      producer.start();
      return producer;
   }
}
