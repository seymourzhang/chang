package com.chang.util.source.rabbitmq;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.rabbitmqApi.RabbitMQFactory;
import com.chang.util.rabbitmqApi.RabbitMQOptions;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import com.chang.util.source.manage.ManageStatistics;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMqInput implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(RabbitMqInput.class);
   private final RabbitMQOptions rabbitOptions;
   private final String messageType;
   private String keyName;
   private Map<String, Object> parm;
   private final Boolean isAck;
   private final ExecutorService executorService;
   private Integer basicQos;
   private Connection connection;
   private Channel channel;

   public RabbitMqInput(String host, int port, String username, String password,
                        String queue, String keyName, String messageType, Integer basicQos,
                        Boolean isAck, Integer nThreads, Integer maximumQueueSize,
                        Map<String, Object> parm, String topic, boolean isDurable) throws IOException, TimeoutException {
      RabbitMQOptions rabbitMQOptions = RabbitMQOptions.builder()
              .host(host).port(port).username(username).password(password)
              .queue(queue).keyName(keyName).topic(topic).durable(isDurable).build();
      this.rabbitOptions = rabbitMQOptions;
      this.messageType = messageType;
      this.keyName = keyName;
      this.parm = parm;
      this.isAck = isAck;
      this.basicQos = basicQos;
      this.executorService = ThreadUtil.newFixedExecutor(nThreads, maximumQueueSize, "RabbitMq", true);
      this.channel = RabbitMQFactory.getConsumerChannel(rabbitMQOptions);
      this.channel.basicQos(basicQos, false);
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) {
      try {
         Consumer consumer = new DefaultConsumer(this.channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               RabbitMqInput.this.executorService.execute(() -> {
                  try {
                     ManageStatistics.inMessageAdd(RabbitMqInput.this.rabbitOptions.getKeyName());

                     Object revData;
                     try {
                        revData = Util.getRevData(RabbitMqInput.this.messageType, body);
                     } catch (Throwable var6) {
                        RabbitMqInput.log.error("message is lost size:{} KeyName:{} time:{}", new Object[]{body.length, RabbitMqInput.this.rabbitOptions.getKeyName(), System.currentTimeMillis()});
                        RabbitMqInput.log.error("getRevData is err", var6);
                        throw new RuntimeException("getRevData is err", var6);
                     }

                     SourceContext.setExParm(RabbitMqInput.this.parm);
                     if (ObjectUtil.isNotNull(function)) {
                        source.Output(function.apply(revData));
                     } else {
                        source.Output(revData);
                     }

//                     SourceContext.clearExParm();
                  } catch (Exception var7) {
                     RabbitMqInput.log.error("[RabbitMq] mArrived err:", var7);
                  }

               });
               RabbitMqInput.this.channel.basicAck(envelope.getDeliveryTag(), true);
            }
         };
         this.channel.basicConsume(this.rabbitOptions.getQueue(), this.isAck, consumer);
      } catch (Exception var4) {
         log.error("RabbitMqInput ERR", var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      if (Objects.nonNull(this.channel)) {
         try {
            this.channel.close();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
