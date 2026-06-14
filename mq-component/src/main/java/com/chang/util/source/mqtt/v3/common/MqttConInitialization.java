package com.chang.util.source.mqtt.v3.common;

import cn.hutool.core.util.ObjectUtil;
import com.chang.common.SslUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;

public class MqttConInitialization {
   private static final Logger log = LoggerFactory.getLogger(MqttConInitialization.class);

   public static MqttConnectOptions getMqttConnectOptions(MqttClientOptions config, PayLoad payLoad) {
      try {
         MqttConnectOptions connOpts = new MqttConnectOptions();
         connOpts.setCleanSession(config.isCleanSession());
         connOpts.setUserName(config.getUserName());
         connOpts.setPassword(config.getPassWord().toCharArray());
         connOpts.setConnectionTimeout(config.getConnectionTimeout());
         connOpts.setKeepAliveInterval(config.getKeepAlive());
         connOpts.setMaxInflight(config.getMaxInflight());
         connOpts.setAutomaticReconnect(false);
         if (config.getSslEnable()) {
            String caCrtFile = config.getCaCrtFile();
            String crtFile = config.getCrtFile();
            String keyFile = config.getKeyFile();
            String sslPassword = config.getSslPassword();
            if (StringUtils.isBlank(caCrtFile) || StringUtils.isBlank(crtFile) || StringUtils.isBlank(keyFile)) {
               throw new RuntimeException("ssl config is Err!");
            }

            connOpts.setHttpsHostnameVerificationEnabled(false);
            SSLSocketFactory socketFactory = SslUtil.getSocketFactory(caCrtFile, crtFile, keyFile, sslPassword);
            connOpts.setSocketFactory(socketFactory);
         }

         if (ObjectUtil.isNotNull(payLoad) && ObjectUtil.isNotNull(payLoad.getWillPayload()) && ObjectUtil.isNotNull(config.getPublishQos())) {
            connOpts.setWill(config.getPublishTopic(), payLoad.getWillPayload(), config.getPublishQos(), false);
         }

         return connOpts;
      } catch (Exception var8) {
         log.error("MqttConInitialization is Err!", var8);
         throw new RuntimeException(var8);
      }
   }
}
