package com.chang.until.mqttclient.v5.common;

import cn.hutool.core.util.ObjectUtil;
import com.chang.common.SslUtil;
import java.nio.charset.Charset;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttConInitialization {
   private static final Logger log = LoggerFactory.getLogger(MqttConInitialization.class);

   public static MqttConnectionOptions getMqttConnectOptions(MqttClientOptions config, PayLoad payLoad) {
      try {
         MqttConnectionOptions connOpts = new MqttConnectionOptions();
         connOpts.setCleanStart(config.isCleanStart());
         connOpts.setSessionExpiryInterval(config.getSessionExpiryInterval());
         connOpts.setUserName(config.getUserName());
         connOpts.setPassword(config.getPassWord().getBytes(Charset.defaultCharset()));
         connOpts.setConnectionTimeout(config.getConnectionTimeout());
         connOpts.setKeepAliveInterval(config.getKeepAlive());
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
            MqttMessage message = new MqttMessage(payLoad.getWillPayload(), config.getPublishQos(), config.isRetained(), new MqttProperties());
            connOpts.setWill(config.getPublishTopic(), message);
         }

         return connOpts;
      } catch (Exception var8) {
         log.error("MqttConInitialization is Err!", var8);
         throw new RuntimeException(var8);
      }
   }
}
