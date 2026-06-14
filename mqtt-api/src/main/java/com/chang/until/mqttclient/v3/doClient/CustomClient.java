package com.chang.until.mqttclient.v3.doClient;

import cn.hutool.core.util.ObjectUtil;
import com.chang.until.mqttclient.v3.common.MqttClientOptions;
import com.chang.until.mqttclient.v3.common.MqttConInitialization;
import com.chang.until.mqttclient.v3.common.PayLoad;
import com.chang.until.mqttclient.v3.common.QoS;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CustomClient {
   private static final Logger logger = LoggerFactory.getLogger(CustomClient.class);
   private final Object reconnectLock = new Object();
   private MqttClientOptions config = null;
   private MqttAsyncClient sampleClient = null;
   private MqttCallbackHandler callback = null;
   private IMqttActionListener mqttActionListener = null;
   private String name;
   private MqttConnectOptions con = null;
   private AtomicInteger connectNum = new AtomicInteger(0);
   private String clientID = "";

   public CustomClient(MqttClientOptions config, PayLoad payLoad, MqttCallbackHandler callback, IMqttActionListener mqttActionListener) {
      this.config = config;
      this.callback = callback;
      this.mqttActionListener = mqttActionListener;
      this.con = MqttConInitialization.getMqttConnectOptions(config, payLoad);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public AtomicInteger getConnectNum() {
      return this.connectNum;
   }

   public MqttClientOptions getConfig() {
      return this.config;
   }

   public MqttAsyncClient getSampleClient() {
      return this.sampleClient;
   }

   public boolean run() {
      try {
         String clientID = "";
         if (this.config.isAutoGenerationClientID()) {
            clientID = MqttClient.generateClientId();
         } else {
            if (!StringUtils.hasText(this.config.getClientID())) {
               logger.info("[MQTT CustomClient] [error] ClientID is null");
               return false;
            }

            clientID = this.config.getClientID();
         }

         this.sampleClient = new MqttAsyncClient(this.config.getBroker(), clientID, new MemoryPersistence());
         this.sampleClient.setCallback(this.callback);
         this.sampleClient.connect(this.con, (Object)null, new ConnectStartListener(this));
         return true;
      } catch (MqttException var2) {
         logger.info("[MQTT CustomClient] [error] CustomClient run err", var2);
         return false;
      }
   }

   public boolean run(long time, TimeUnit unit) throws MqttException, TimeoutException, InterruptedException {
      if (this.config.isAutoGenerationClientID()) {
         this.clientID = MqttClient.generateClientId();
      } else {
         if (!StringUtils.hasText(this.config.getClientID())) {
            logger.info("[MQTT CustomClient] [error] ClientID is null");
            return false;
         }

         this.clientID = this.config.getClientID();
      }

      this.sampleClient = new MqttAsyncClient(this.config.getBroker(), this.clientID, new MemoryPersistence());
      this.sampleClient.setCallback(this.callback);
      this.sampleClient.connect(this.con);
      logger.info("[MQTT CustomClient]  start connect wait sleep: " + this.name);
      unit.sleep(time);
      if (!this.sampleClient.isConnected()) {
         logger.info("[MQTT CustomClient]  Client is not Connected: " + this.name);
         throw new TimeoutException("[MQTT CustomClient] [error] new mqtt client connect is time out");
      } else {
         logger.info("[MQTT CustomClient]  Client is Connected: " + this.name);
         return true;
      }
   }

   public boolean doSubscribe() {
      try {
         if (StringUtils.hasText(this.config.getSubsrcibeTopics()) && StringUtils.hasText(this.config.getSubsrcibeQoss())) {
            String[] Topics = this.config.getSubsrcibeTopics().split(",");
            String[] sQoss = this.config.getSubsrcibeQoss().split(",");
            if (0 != Topics.length && 0 != sQoss.length) {
               if (Topics.length != sQoss.length) {
                  logger.info("[MQTT Subscribe] [error] Topics.length != sQoss.length");
                  return false;
               } else {
                  int[] Qoss = new int[sQoss.length];

                  for(int i = 0; i < sQoss.length; ++i) {
                     Qoss[i] = Integer.parseInt(sQoss[i]);
                  }

                  int[] var9 = Qoss;
                  int var5 = Qoss.length;

                  for(int var6 = 0; var6 < var5; ++var6) {
                     int iOos = var9[var6];
                     if (iOos != QoS.AT_LEAST_ONCE && iOos != QoS.AT_MOST_ONCE && iOos != QoS.EXACTLY_ONCE) {
                        logger.info("[MQTT Subscribe] [error] QoS Value is error,olny 0 ,1 ,2");
                        return false;
                     }
                  }

                  this.sampleClient.subscribe(Topics, Qoss);
                  return true;
               }
            } else {
               logger.info("[MQTT Subscribe] [error] T0 == Topics.length or 0  ==  sQoss.length");
               return false;
            }
         } else {
            logger.info("[MQTT Subscribe] [error] Topics or sQoss not has text!");
            return false;
         }
      } catch (MqttException var8) {
         logger.info("[MQTT Subscribe] [error] Subscribe run err: ", var8);
         return false;
      }
   }

   public void reconnectCallBack() {
      logger.info("[MQTT RECONNECT] : The client start reconnect " + this.name);

      try {
         this.sampleClient.setCallback(this.callback);
         this.sampleClient.connect(this.con, (Object)null, new ConnectStartListener(this));
      } catch (Exception var2) {
         logger.info("[MQTT RECONNECT] [ERR] reconnect failed ", var2);
      }

   }

   public void reconnect() {
      try {
         this.sampleClient.close();
         this.sampleClient = new MqttAsyncClient(this.config.getBroker(), this.clientID, new MemoryPersistence());
         this.sampleClient.setCallback(this.callback);
         this.sampleClient.connect(this.con, (Object)null, new ReConnectListener(this));
      } catch (Exception var2) {
         logger.info("[MQTT RECONNECT] [ERR] reconnect failed ", var2);
      }

   }

   public boolean isConnect() {
      return this.sampleClient.isConnected();
   }

   public int getInFlight() {
      return this.sampleClient.getInFlightMessageCount();
   }

   private void checkClient() {
      if (!this.sampleClient.isConnected()) {
         throw new RuntimeException(this.name + " is not Connected");
      }
   }

   public void PublishMessage(MqttMessage msgContent, long timeout) throws MqttException {
      this.checkClient();
      IMqttDeliveryToken token = this.sampleClient.publish(this.config.getPublishTopic(), msgContent, (Object)null, this.mqttActionListener);
      token.waitForCompletion(timeout);
   }

   public void PublishMessage(MqttMessage msgContent) throws MqttException {
      this.checkClient();
      this.sampleClient.publish(this.config.getPublishTopic(), msgContent, (Object)null, this.mqttActionListener);
   }

   public void PublishMessage(String topic, MqttMessage msgContent) throws MqttException {
      this.checkClient();
      this.sampleClient.publish(topic, msgContent, (Object)null, this.mqttActionListener);
   }

   public void PublishMessage(String msgContent) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgContent.getBytes());
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      this.sampleClient.publish(this.config.getPublishTopic(), message, (Object)null, this.mqttActionListener);
   }

   public void PublishMessage(byte[] msgBye) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgBye);
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      this.sampleClient.publish(this.config.getPublishTopic(), message, (Object)null, this.mqttActionListener);
   }

   public void PublishMessage(String msgContent, long timeout) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgContent.getBytes(StandardCharsets.UTF_8));
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      IMqttDeliveryToken token = this.sampleClient.publish(this.config.getPublishTopic(), message, (Object)null, this.mqttActionListener);
      token.waitForCompletion(timeout);
   }

   public void PublishMessage(byte[] msgBye, long timeout) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgBye);
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      IMqttDeliveryToken token = this.sampleClient.publish(this.config.getPublishTopic(), message, (Object)null, this.mqttActionListener);
      token.waitForCompletion(timeout);
   }

   public void PublishMessage(String topic, String msgContent) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgContent.getBytes(StandardCharsets.UTF_8));
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      this.sampleClient.publish(topic, message, (Object)null, this.mqttActionListener);
   }

   public void PublishMessage(String topic, byte[] msgBye) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgBye);
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      this.sampleClient.publish(topic, message, (Object)null, this.mqttActionListener);
   }

   public void PublishMessage(String topic, MqttMessage msgContent, long timeout) throws MqttException {
      this.checkClient();
      msgContent.setQos(this.config.getPublishQos());
      msgContent.setRetained(this.config.isRetained());
      IMqttDeliveryToken token = this.sampleClient.publish(topic, msgContent, (Object)null, this.mqttActionListener);
      token.waitForCompletion(timeout);
   }

   public void PublishMessage(String topic, String msgContent, long timeout) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgContent.getBytes(StandardCharsets.UTF_8));
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      IMqttDeliveryToken token = this.sampleClient.publish(topic, message, (Object)null, this.mqttActionListener);
      token.waitForCompletion(timeout);
   }

   public void PublishMessage(String topic, byte[] msgBye, long timeout) throws MqttException {
      this.checkClient();
      MqttMessage message = new MqttMessage(msgBye);
      message.setQos(this.config.getPublishQos());
      message.setRetained(this.config.isRetained());
      IMqttDeliveryToken token = this.sampleClient.publish(topic, message, (Object)null, this.mqttActionListener);
      token.waitForCompletion(timeout);
   }

   public void close() throws MqttException {
      if (ObjectUtil.isNotNull(this.sampleClient)) {
         this.sampleClient.close(true);
      }

   }

   public String getClientID() {
      return this.clientID;
   }
}
