package com.chang.util.source.mqtt.v5;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.mqtt.v5.common.MqttClientOptions;
import com.chang.util.source.mqtt.v5.common.PayLoad;
import com.chang.util.source.mqtt.v5.common.QoS;
import com.chang.util.source.mqtt.v5.doClient.CustomClient;
import com.chang.util.source.mqtt.v5.doClient.MqttCallbackHandler;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttActionListener;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class Mqttv5Input implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(Mqttv5Input.class);
   private String broker;
   private String topic;
   private String username;
   private String password;
   private CustomClient client;
   private List<String> res = new ArrayList();
   private String keyName;
   private Boolean sslEnable = false;
   private String caCrtFile;
   private String crtFile;
   private String keyFile;
   private String sslPassword = "";
   private MqttClientOptions options;
   private String messageType;
   private Map<String, Object> parm;
   private final ExecutorService executorService;

   public Mqttv5Input(String broker, String topic, String username, String password, String keyName, Boolean sslEnable, String caCrtFile, String crtFile, String keyFile, String sslPassword, String messageType, Map<String, Object> parm, boolean retained, String subscribersQos, boolean autoGenerationClientId, String clientID, Integer nThreads, Integer maximumQueueSize) {
      this.topic = topic;
      this.broker = broker;
      this.username = username;
      this.password = password;
      this.keyName = keyName;
      this.messageType = messageType;
      this.parm = parm;
      this.executorService = ThreadUtil.newFixedExecutor(nThreads, maximumQueueSize, "MqttV5", true);
      this.options = MqttClientOptions.builder().cleanStart(true).sessionExpiryInterval(0L).subsrcibeTopics(topic).autoGenerationClientID(autoGenerationClientId).clientID(clientID).needSubsrcibe(true).subsrcibeQoss(subscribersQos).publishQos(QoS.AT_MOST_ONCE).retained(retained).broker(broker).userName(username).passWord(password).keepAlive(5).connectionTimeout(5).cleanStart(true).sslEnable(sslEnable).caCrtFile(caCrtFile).crtFile(crtFile).keyFile(keyFile).sslPassword(StringUtils.isBlank(sslPassword) ? "" : sslPassword).build();
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) throws InterruptedException {
      try {
         MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {
             @Override
             public void deliveryComplete(IMqttToken iMqttToken) {

             }

             @Override
             public void authPacketArrived(int i, MqttProperties mqttProperties) {

             }

             @Override
             protected void connectOK(boolean b, String s, MqttClientOptions mqttClientOptions) {
                 Mqttv5Input.log.info("[MQTT MqttCallbackHandler] [info] connectOK");
             }

             @Override
             protected void connectLost(MqttDisconnectResponse mqttDisconnectResponse, String s) {
                 Mqttv5Input.log.error("[MQTT MqttCallbackHandler] [info] connectLost ", mqttDisconnectResponse.getException());
             }

             protected void mArrived(String topic, MqttMessage message) {
                 MqttPublic.doWith(topic, Mqttv5Input.this.executorService, message.getPayload(), Mqttv5Input.this.parm, Mqttv5Input.this.messageType, function, source);
             }

             @Override
             protected void Error(MqttException e) {

             }


         };
         MqttActionListener mqttActionListener = new MqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {
               Mqttv5Input.log.info("[MQTT mqttBrokerActionListener] [info] onSuccess");
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
               Mqttv5Input.log.info("[MQTT mqttBrokerActionListener] [info] onFailure");
            }
         };
         this.client = new CustomClient(this.options, (PayLoad)null, mqttCallbackHandler, mqttActionListener);
         this.client.setName(this.keyName);
         mqttCallbackHandler.setCustomClient(this.client);
         this.client.run();
         log.info("wait Connect ....... " + this.keyName);
         log.info("[Source Mqttv5Input] ClientName: {} ; ClientID:{} ; Config: {}", new Object[]{this.client.getName(), this.client.getClientID(), this.client.getConfig().toString()});

         while(!this.client.isConnect()) {
            Thread.sleep(1000L);
         }

         log.info("Connect OK  " + this.keyName);
         this.client.doSubscribe();
      } catch (Throwable var5) {
         throw var5;
      }
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      if (ObjectUtil.isNotNull(this.client)) {
         try {
            this.client.close();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public String getBroker() {
      return this.broker;
   }

   public String getTopic() {
      return this.topic;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public CustomClient getClient() {
      return this.client;
   }

   public List<String> getRes() {
      return this.res;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public Boolean getSslEnable() {
      return this.sslEnable;
   }

   public String getCaCrtFile() {
      return this.caCrtFile;
   }

   public String getCrtFile() {
      return this.crtFile;
   }

   public String getKeyFile() {
      return this.keyFile;
   }

   public String getSslPassword() {
      return this.sslPassword;
   }

   public MqttClientOptions getOptions() {
      return this.options;
   }

   public String getMessageType() {
      return this.messageType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public void setBroker(String broker) {
      this.broker = broker;
   }

   public void setTopic(String topic) {
      this.topic = topic;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setClient(CustomClient client) {
      this.client = client;
   }

   public void setRes(List<String> res) {
      this.res = res;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public void setSslEnable(Boolean sslEnable) {
      this.sslEnable = sslEnable;
   }

   public void setCaCrtFile(String caCrtFile) {
      this.caCrtFile = caCrtFile;
   }

   public void setCrtFile(String crtFile) {
      this.crtFile = crtFile;
   }

   public void setKeyFile(String keyFile) {
      this.keyFile = keyFile;
   }

   public void setSslPassword(String sslPassword) {
      this.sslPassword = sslPassword;
   }

   public void setOptions(MqttClientOptions options) {
      this.options = options;
   }

   public void setMessageType(String messageType) {
      this.messageType = messageType;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }
}
