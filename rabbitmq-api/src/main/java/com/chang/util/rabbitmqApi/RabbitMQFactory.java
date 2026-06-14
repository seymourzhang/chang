package com.chang.util.rabbitmqApi;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQFactory {
   public static Channel getConsumerChannel(RabbitMQOptions rabbitMQOptions) throws IOException, TimeoutException {
      Channel channel = createChannel(rabbitMQOptions);
      // 1. 声明主题交换机
      channel.exchangeDeclare(rabbitMQOptions.getTopic(), BuiltinExchangeType.FANOUT, false);
      // 2. 声明队列
      channel.queueDeclare(rabbitMQOptions.getQueue(), rabbitMQOptions.getDurable(), false, false, null);
      // 3. 绑定队列到交换机，使用路由键
      if (StringUtils.isNotEmpty(rabbitMQOptions.getTopic())) {
          channel.queueBind(rabbitMQOptions.getQueue(), rabbitMQOptions.getTopic(), rabbitMQOptions.getKeyName());
      }
      return channel;
   }

   public static Channel getProducerChannel(RabbitMQOptions rabbitMQOptions) throws IOException, TimeoutException {
      Channel channel = createChannel(rabbitMQOptions);
      channel.queueDeclare(rabbitMQOptions.getTopic(), rabbitMQOptions.getDurable(), false, false, (Map)null);
      return channel;
   }

   private static Channel createChannel(RabbitMQOptions rabbitMQOptions) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(rabbitMQOptions.getHost());
      factory.setPort(rabbitMQOptions.getPort());
      factory.setUsername(rabbitMQOptions.getUsername());
      factory.setPassword(rabbitMQOptions.getPassword());
      factory.setVirtualHost("/");
      Connection rabbitConnection = factory.newConnection();
      Channel channel = rabbitConnection.createChannel();
      return channel;
   }
}
