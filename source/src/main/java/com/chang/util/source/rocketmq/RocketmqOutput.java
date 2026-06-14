package com.chang.util.source.rocketmq;

import com.chang.util.rocketmqApi.ProducerSendUtil;
import com.chang.util.rocketmqApi.RocketFactory;
import com.chang.util.rocketmqApi.RocketMqOptions;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.Util;
import java.util.Map;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketmqOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(RocketmqOutput.class);
   private final DefaultMQProducer producer;
   private final RocketMqOptions rocketMqOptions;
   private final ProducerSendUtil producerSendUtil;
   private String messageType;
   private String keyName;
   private Map<String, Object> parm;

   public RocketmqOutput(String keyName, String producerGroup, String nameSrvAddr, String topic, int sendMsgTimeout, String namespace, String accessKey, String secretKey, String messageType, Map<String, Object> parm) throws MQClientException {
      this.messageType = messageType;
      this.keyName = keyName;
      this.parm = parm;
      this.rocketMqOptions = RocketMqOptions.builder().accessKey(accessKey).secretKey(secretKey).namespace(namespace).topic(topic).group(producerGroup).nameSrvAddr(nameSrvAddr).sendMsgTimeout(sendMsgTimeout).build();
      this.producer = RocketFactory.getProducer(this.rocketMqOptions);
      this.producerSendUtil = new ProducerSendUtil(this.producer);
   }

   public void Output(Object o) {
      try {
         this.producerSendUtil.send(this.rocketMqOptions.getTopic(), Util.getSendData(this.messageType, o));
      } catch (Exception var3) {
         log.error("RocketmqOutput ", var3);
      }

   }

   public void OutputByTag(String tag, Object o) {
      try {
         this.producerSendUtil.send(this.rocketMqOptions.getTopic(), tag, Util.getSendData(this.messageType, o));
      } catch (Exception var4) {
         log.error("RocketmqOutput ", var4);
      }

   }

   public void OutputByTag(String tag, Object o, Object hash) {
      try {
         this.producerSendUtil.sendByHash(this.rocketMqOptions.getTopic(), tag, Util.getSendData(this.messageType, o), hash);
      } catch (Exception var5) {
         log.error("RocketmqOutput ", var5);
      }

   }

   public void OutputByTag(Object o, Object hash) {
      try {
         this.producerSendUtil.sendByHash(this.rocketMqOptions.getTopic(), Util.getSendData(this.messageType, o), hash);
      } catch (Exception var4) {
         log.error("RocketmqOutput ", var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      this.producer.shutdown();
   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
