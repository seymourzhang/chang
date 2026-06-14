package com.chang.util.source.mqtt.v3.doClient;

import com.chang.util.source.mqtt.v3.common.MqttClientOptions;
import com.chang.util.source.mqtt.v3.common.RunTask;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MqttCallbackHandler implements MqttCallbackExtended {
   private static final Logger logger = LoggerFactory.getLogger("MqttCallbackHandler");
   private CustomClient customClient = null;

   public void setCustomClient(CustomClient customClient) {
      this.customClient = customClient;
   }

   public void connectComplete(boolean reconnect, String serverURI) {
      logger.info("[MQTT CALL BACK HANDLER] connectComplete {}", this.customClient.getName());
      if (this.customClient.getConfig().isNeedSubsrcibe() && this.customClient.getConfig().isCleanSession()) {
         logger.info("[MQTT CALL BACK HANDLER]  {} reSubscribe", this.customClient.getName());
         this.customClient.doSubscribe();
      }

      try {
         RunTask.createNewTask(() -> {
            this.connectOK(reconnect, this.customClient.getName(), this.customClient.getConfig());
         });
      } catch (Exception var4) {
         logger.info("[MQTT CALL BACK HANDLER] connectComplete RunTask.createNewTask err", var4);
      }

   }

   public void connectionLost(Throwable cause) {
      Throwable throwable = cause;

      try {
         RunTask.createNewTask(() -> {
            this.connectLost(throwable, this.customClient.getName());
         });
      } catch (Exception var5) {
         logger.info("[MQTT CALL BACK HANDLER] connectionLost RunTask.createNewTask err", var5);
      }

      try {
         RunTask.createNewTask(() -> {
            this.customClient.reconnect();
         });
      } catch (Exception var4) {
         logger.info("[MQTT CALL BACK HANDLER] connectionLost RunTask.createNewTask err", var4);
      }

   }

   public void messageArrived(String topic, MqttMessage message) {
      try {
         this.mArrived(topic, message);
      } catch (Exception var4) {
         logger.error("messageArrived is ERR! ", var4);
      }

   }

   public void deliveryComplete(IMqttDeliveryToken token) {
      this.arrivedComplete(token);
   }

   protected abstract void connectOK(boolean var1, String var2, MqttClientOptions var3);

   protected abstract void connectLost(Throwable var1, String var2);

   protected abstract void mArrived(String var1, MqttMessage var2) throws Exception;

   protected abstract void arrivedComplete(IMqttDeliveryToken var1);
}
