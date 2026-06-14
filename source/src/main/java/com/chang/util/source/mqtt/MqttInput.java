package com.chang.util.source.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.until.mqttclient.v3.common.MqttClientOptions;
import com.chang.until.mqttclient.v3.common.PayLoad;
import com.chang.until.mqttclient.v3.common.QoS;
import com.chang.until.mqttclient.v3.doClient.CustomClient;
import com.chang.until.mqttclient.v3.doClient.MqttCallbackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttInput implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(MqttInput.class);
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
   private Boolean currRev = false;
   private final ExecutorService executorService;

   public MqttInput(String broker, String topic, String username, String password, String keyName, Boolean sslEnable, String caCrtFile, String crtFile, String keyFile, String sslPassword, String messageType, Map<String, Object> parm, boolean retained, String subscribersQos, boolean autoGenerationClientId, String clientID, Integer nThreads, Integer maximumQueueSize) {
      this.topic = topic;
      this.broker = broker;
      this.username = username;
      this.password = password;
      this.keyName = keyName;
      this.messageType = messageType;
      this.parm = parm;
      this.executorService = ThreadUtil.newFixedExecutor(nThreads, maximumQueueSize, "MqttV3", true);
      this.options = MqttClientOptions.builder().autoGenerationClientID(autoGenerationClientId).clientID(clientID).subsrcibeTopics(topic).needSubsrcibe(true).subsrcibeQoss(subscribersQos).publishQos(QoS.AT_MOST_ONCE).retained(retained).broker(broker).userName(username).passWord(password).keepAlive(5).maxInflight(5000).connectionTimeout(5).cleanSession(true).sslEnable(sslEnable).caCrtFile(caCrtFile).crtFile(crtFile).keyFile(keyFile).sslPassword(StringUtils.isBlank(sslPassword) ? "" : sslPassword).build();
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) throws InterruptedException {
      try {
         MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {
            protected void connectOK(boolean reconnect, String clientName, MqttClientOptions config) {
               MqttInput.log.info("[MQTT MqttCallbackHandler] [info] connectOK");
            }

            protected void connectLost(Throwable cause, String breakName) {
               MqttInput.log.error("[MQTT MqttCallbackHandler] [info] connectLost", cause);
            }

            protected void mArrived(String topic, MqttMessage message) throws Exception {
               MqttPublic.doWith(topic, MqttInput.this.executorService, message.getPayload(), MqttInput.this.parm, MqttInput.this.messageType, function, source);
            }

            protected void arrivedComplete(IMqttDeliveryToken token) {
            }
         };
         IMqttActionListener iMqttActionListener = new IMqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {
               MqttInput.log.info("[MQTT mqttBrokerActionListener] [info] onSuccess");
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
               MqttInput.log.info("[MQTT mqttBrokerActionListener] [info] onFailure");
            }
         };
         this.client = new CustomClient(this.options, (PayLoad)null, mqttCallbackHandler, iMqttActionListener);
         this.client.setName(this.keyName);
         mqttCallbackHandler.setCustomClient(this.client);
         boolean run = this.client.run();
         if (!run) {
            log.info("MqttInput connect err: " + this.keyName);
         } else {
            log.info("MqttInput create OK: " + this.keyName);
         }

         log.info("wait Connect ....... " + this.keyName);
         log.info("[Source MqttInput] ClientName: {} ; ClientID:{} ; Config: {}", new Object[]{this.client.getName(), this.client.getClientID(), this.client.getConfig().toString()});

         while(!this.client.isConnect()) {
            Thread.sleep(1000L);
         }

         log.info("Connect OK  " + this.keyName);
         this.client.doSubscribe();
      } catch (Throwable var6) {
         throw var6;
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

   public Boolean getCurrRev() {
      return this.currRev;
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

   public void setCurrRev(Boolean currRev) {
      this.currRev = currRev;
   }
}
