package com.chang.util.rocketmqApi;

import cn.hutool.core.util.ObjectUtil;
import com.chang.common.CommUtils;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyMessageListener {
   private static final Logger log = LoggerFactory.getLogger(MyMessageListener.class);
   private final RocketMQListener<Object> rocketMQListener;
   private final RocketMQReplyListener<Object, Objects> rocketMQReplyListener;
   private final DefaultMQPushConsumer consumer;
   private final String messageType;

   public MyMessageListener(RocketMQListener<Object> rocketMQListener, RocketMQReplyListener<Object, Objects> rocketMQReplyListener, DefaultMQPushConsumer consumer, String messageType) {
      this.rocketMQListener = rocketMQListener;
      this.rocketMQReplyListener = rocketMQReplyListener;
      this.consumer = consumer;
      this.messageType = messageType;
   }

   private void handleMessage(MessageExt messageExt) throws Exception {
      byte[] body = messageExt.getBody();
      Object revData;
      if (ObjectUtil.equal(this.messageType, "str")) {
         revData = new String(body, StandardCharsets.UTF_8);
      } else if (ObjectUtil.equal(this.messageType, "serialize")) {
         revData = CommUtils.deserialize(body, Object.class);
      } else {
         if (!ObjectUtil.equal(this.messageType, "gzip-uncompress")) {
            throw new RuntimeException("messageType is err");
         }

         revData = CommUtils.gzipUncompress(body).get();
      }

      if (ObjectUtil.isNull(revData)) {
         log.error("revData is null");
         throw new RuntimeException("revData is null");
      } else {
         if (this.rocketMQListener != null) {
            this.rocketMQListener.onMessage(revData);
         } else if (this.rocketMQReplyListener != null) {
            Objects message = (Objects)this.rocketMQReplyListener.onMessage(revData);
            Message replyMessage = org.apache.rocketmq.client.utils.MessageUtil.createReplyMessage(messageExt, CommUtils.serialize(message));
            this.consumer.getDefaultMQPushConsumerImpl().getmQClientFactory().getDefaultMQProducer().send(replyMessage, new SendCallback() {
               public void onSuccess(SendResult sendResult) {
                  if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                     MyMessageListener.log.error("Consumer replies message failed. SendStatus: {}", sendResult.getSendStatus());
                  } else {
                     MyMessageListener.log.debug("Consumer replies message success.");
                  }

               }

               public void onException(Throwable e) {
                  MyMessageListener.log.error("Consumer replies message failed. error: {}", e.getLocalizedMessage());
               }
            });
         }

      }
   }

   public class MyMessageListenerOrderly implements MessageListenerOrderly {
      public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
         Iterator var3 = msgs.iterator();

         while(var3.hasNext()) {
            MessageExt messageExt = (MessageExt)var3.next();

            try {
               MyMessageListener.this.handleMessage(messageExt);
            } catch (Exception var6) {
               context.setSuspendCurrentQueueTimeMillis(1000L);
               return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
         }

         return ConsumeOrderlyStatus.SUCCESS;
      }
   }

   public class MyMessageListenerConcurrently implements MessageListenerConcurrently {
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
         Iterator var3 = msgs.iterator();

         while(var3.hasNext()) {
            MessageExt messageExt = (MessageExt)var3.next();

            try {
               MyMessageListener.this.handleMessage(messageExt);
            } catch (Exception var6) {
               context.setDelayLevelWhenNextConsume(0);
               return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
         }

         return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }
   }
}
