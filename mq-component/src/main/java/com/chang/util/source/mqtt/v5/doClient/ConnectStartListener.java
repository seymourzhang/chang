package com.chang.util.source.mqtt.v5.doClient;

import java.util.concurrent.TimeUnit;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectStartListener implements MqttActionListener {
    private static final Logger logger = LoggerFactory.getLogger(ConnectStartListener.class);
    private CustomClient customClient;

    public ConnectStartListener(CustomClient customClient) {
        this.customClient = customClient;
    }

    public void onSuccess(IMqttToken asyncActionToken) {
        logger.info("[MQTT CONNECT]  connect is onSuccess: " + this.customClient.getName());
    }

    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        try {
            logger.error("[MQTT CONNECT] connect is lost and reconnect......  " + this.customClient.getName(), exception);
            if (this.customClient.getConnectNum().get() < 5) {
                logger.info("[MQTT CONNECT] connect is onFailure and reconnect......");
                this.customClient.getConnectNum().getAndIncrement();
                TimeUnit.SECONDS.sleep(6L);
                this.customClient.reconnectCallBack();
            } else {
                this.customClient.getConnectNum().set(0);
                logger.info("[MQTT CONNECT] connect is onFailure and not reconnect: " + this.customClient.getName());
            }
        } catch (Exception var4) {
            logger.info("[MQTT CONNECT] ConnectListener is err", var4);
        }

    }
}

