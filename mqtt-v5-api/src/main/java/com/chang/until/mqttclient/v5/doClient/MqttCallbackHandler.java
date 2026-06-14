package com.chang.until.mqttclient.v5.doClient;

import com.chang.until.mqttclient.v5.common.MqttClientOptions;
import com.chang.until.mqttclient.v5.common.RunTask;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MqttCallbackHandler implements MqttCallback {
   private static final Logger logger = LoggerFactory.getLogger("MqttCallbackHandler");
   private CustomClient customClient = null;

   public void setCustomClient(CustomClient customClient) {
      this.customClient = customClient;
   }

   public void connectComplete(boolean reconnect, String serverURI) {
      logger.info("[MQTT CALL BACK HANDLER] connectComplete {}", this.customClient.getName());
      if (this.customClient.getConfig().isNeedSubsrcibe() && this.customClient.getConfig().isCleanStart()) {
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

   public void disconnected(MqttDisconnectResponse disconnectResponse) {
      try {
         RunTask.createNewTask(() -> {
            this.connectLost(disconnectResponse, this.customClient.getName());
         });
      } catch (Exception var4) {
         logger.info("[MQTT CALL BACK HANDLER] connectionLost RunTask.createNewTask err", var4);
      }

      try {
         RunTask.createNewTask(() -> {
            this.customClient.reconnect();
         });
      } catch (Exception var3) {
         logger.info("[MQTT CALL BACK HANDLER] connectionLost RunTask.createNewTask err", var3);
      }

   }

   public void mqttErrorOccurred(MqttException cause) {
      try {
         RunTask.createNewTask(() -> {
            this.Error(cause);
         });
      } catch (Exception var3) {
         logger.error("mqttErrorOccurred is ERR! ", var3);
      }

   }

   public void messageArrived(String topic, MqttMessage message) {
      try {
         RunTask.createNewTask(() -> {
            this.mArrived(topic, message);
         });
      } catch (Exception var4) {
         logger.error("messageArrived is ERR! ", var4);
      }

   }

   protected abstract void connectOK(boolean var1, String var2, MqttClientOptions var3);

   protected abstract void connectLost(MqttDisconnectResponse var1, String var2);

   protected abstract void mArrived(String var1, MqttMessage var2);

   protected abstract void Error(MqttException var1);
}
