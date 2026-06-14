package com.chang.until.mqttclient.v3.doClient;

import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReConnectListener implements IMqttActionListener {
   private static final Logger logger = LoggerFactory.getLogger(ReConnectListener.class);
   private CustomClient customClient;

   public ReConnectListener(CustomClient customClient) {
      this.customClient = customClient;
   }

   public void onSuccess(IMqttToken asyncActionToken) {
      logger.info("[MQTT ReConnectListener]  connect is onSuccess: " + this.customClient.getName());
   }

   public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
      try {
         logger.info("[MQTT ReConnectListener]  reConnect: " + this.customClient.getName());
         TimeUnit.SECONDS.sleep(10L);
         this.customClient.reconnect();
      } catch (Exception var4) {
         logger.error("[MQTT ReConnectListener] ConnectListener is err", var4);
      }

   }
}
