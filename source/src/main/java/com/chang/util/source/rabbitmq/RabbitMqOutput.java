package com.chang.util.source.rabbitmq;

import com.chang.util.rabbitmqApi.RabbitMQFactory;
import com.chang.util.rabbitmqApi.RabbitMQOptions;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.Util;
import com.chang.util.source.manage.ManageStatistics;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMqOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(RabbitMqOutput.class);
   private final RabbitMQOptions rabbitOptions;
   private Channel channel;
   private String messageType;
   private String keyName;
   private Map<String, Object> parm;

   public RabbitMqOutput(String host, int port, String username, String password, String topic, String keyName, String messageType, Boolean isDurable, Map<String, Object> parm) throws IOException, TimeoutException {
      this.messageType = messageType;
      this.keyName = keyName;
      this.parm = parm;
      RabbitMQOptions rabbitMQOptions = RabbitMQOptions.builder().host(host).port(port).username(username).password(password).topic(topic).keyName(keyName).durable(isDurable).build();
      this.rabbitOptions = rabbitMQOptions;
      this.channel = RabbitMQFactory.getProducerChannel(rabbitMQOptions);
   }

   public void Output(Object o) {
      try {
         this.channel.basicPublish("", this.rabbitOptions.getTopic(), (AMQP.BasicProperties)null, Util.getSendData(this.messageType, o));
         ManageStatistics.outMessageAdd(this.rabbitOptions.getKeyName());
      } catch (Exception var3) {
         log.error("RabbitMqOutput ERR", var3);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   protected void finalize() throws Throwable {
      this.close();
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
}
