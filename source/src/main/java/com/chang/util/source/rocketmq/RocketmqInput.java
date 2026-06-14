package com.chang.util.source.rocketmq;

import cn.hutool.core.util.ObjectUtil;
import com.chang.util.rocketmqApi.MyMessageListener;
import com.chang.util.rocketmqApi.RocketFactory;
import com.chang.util.rocketmqApi.RocketMQReplyListener;
import com.chang.util.rocketmqApi.RocketMqOptions;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import java.util.Map;
import java.util.function.Function;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketmqInput implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(RocketmqInput.class);
   private final DefaultMQPushConsumer consumer;
   private final String messageType;
   private final ConsumerMessageType consumerMessageType;
   private final String keyName;
   private Map<String, Object> parm;

   public RocketmqInput(String keyName, String consumerGroup, String nameSrvAddr, String topic, String subExpression, String messageModel, String namespace, String messageType, Integer messageBatchMaxSize, Integer consumeThread, String accessKey, String secretKey, ConsumerMessageType consumerMessageType, Map<String, Object> parm) throws MQClientException {
      RocketMqOptions rocketMqOptions = RocketMqOptions.builder().accessKey(accessKey).secretKey(secretKey).namespace(namespace).topic(topic).group(consumerGroup).messageModel(messageModel).nameSrvAddr(nameSrvAddr).messageBatchMaxSize(messageBatchMaxSize).consumeThread(consumeThread).subExpression(subExpression).build();
      this.messageType = messageType;
      this.consumerMessageType = consumerMessageType;
      this.parm = parm;
      this.keyName = keyName;
      this.consumer = RocketFactory.getConsumer(rocketMqOptions);
   }

   public void InPut(OutputSource source, Function<Object, Object> function) {
      try {
         MyMessageListener myMessageListener = new MyMessageListener((message) -> {
            SourceContext.setExParm(this.parm);
            if (ObjectUtil.isNotNull(function)) {
               source.Output(function.apply(message));
            } else {
               source.Output(message);
            }

            SourceContext.clearExParm();
         }, (RocketMQReplyListener)null, this.consumer, this.messageType);
         if (ObjectUtil.equals(this.consumerMessageType, ConsumerMessageType.CONCURRENTLY)) {
            this.consumer.registerMessageListener(myMessageListener.new MyMessageListenerConcurrently());
         } else {
            if (!ObjectUtil.equals(this.consumerMessageType, ConsumerMessageType.ORDERLY)) {
               throw new RuntimeException(this.keyName + " ConsumerMessageType Err!");
            }

            this.consumer.registerMessageListener(myMessageListener.new MyMessageListenerOrderly());
         }

         this.consumer.start();
      } catch (Exception var4) {
         log.error("RocketmqInput InPut Err", var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      this.consumer.shutdown();
   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
