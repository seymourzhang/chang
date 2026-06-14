package com.chang.util.source.mqtt.v3;

import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.*;
import com.chang.util.source.mqtt.v3.common.MqttClientOptions;
import com.chang.util.source.mqtt.v3.common.PayLoad;
import com.chang.util.source.mqtt.v3.common.QoS;
import com.chang.util.source.mqtt.v3.doClient.CustomClient;
import com.chang.util.source.mqtt.v3.doClient.MqttCallbackHandler;
import com.chang.until.timeTaskApi.TimeTaskManage;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class MqttOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(MqttOutput.class);
   private final ConcurrentLinkedQueue<MqttQueueData> cacheList = new ConcurrentLinkedQueue();
   private String topic;
   private final ThreadLocal<String> topicThread = new ThreadLocal();
   private String username;
   private String password;
   private Boolean isCache = false;
   private String cacheTopic;
   private final ThreadLocal<String> cacheTopicThread = new ThreadLocal();
   private String cacheName;
   private Long batchSize;
   private Long timeInterval;
   private Long expiredDataSize = 0L;
   private String broker;
   private String keyName;
   private MqttClientOptions options;
   private CustomClient client;
   private Long queuePopSize = 1L;
   private Boolean sslEnable = false;
   private String caCrtFile;
   private String crtFile;
   private String keyFile;
   private String sslPassword = "";
   private String messageType;
   private Map<String, Object> parm;
   private static Boolean isPrint = false;

   public static void setIsPrint(Boolean print) {
      isPrint = print;
   }

   public MqttOutput(String broker, String topic, String username, String password, Boolean isCache, String cacheName, String cacheTopic, Long batchSize, Long timeInterval, Long expiredDataSize, final String keyName, Long queuePopSize, Boolean sslEnable, String caCrtFile, String crtFile, String keyFile, String sslPassword, String messageType, Map<String, Object> parm, boolean retained, boolean autoGenerationClientId, String clientID, String willPayLoad, int publishQos) throws Exception {
      this.messageType = messageType;
      this.broker = broker;
      this.topic = topic;
      this.username = username;
      this.password = password;
      this.isCache = isCache;
      this.cacheName = cacheName;
      this.cacheTopic = cacheTopic;
      this.batchSize = batchSize;
      this.timeInterval = timeInterval;
      this.expiredDataSize = expiredDataSize;
      this.keyName = keyName;
      this.queuePopSize = queuePopSize;
      this.parm = parm;
      this.options = MqttClientOptions.builder().autoGenerationClientID(autoGenerationClientId).clientID(clientID).needSubsrcibe(false).publishQos(QoS.AT_MOST_ONCE).retained(retained).broker(broker).userName(username).passWord(password).keepAlive(5).maxInflight(5000).connectionTimeout(5).cleanSession(true).sslEnable(sslEnable).crtFile(crtFile).caCrtFile(caCrtFile).keyFile(keyFile).sslPassword(StringUtils.isBlank(sslPassword) ? "" : sslPassword).publishQos(publishQos).build();
      MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {
         protected void connectOK(boolean reconnect, String clientName, MqttClientOptions config) {
            MqttOutput.log.info("[MQTT MqttCallbackHandler] [info] connectOK");
         }

         protected void connectLost(Throwable cause, String breakName) {
            MqttOutput.log.error("[MQTT MqttCallbackHandler] [info] connectLost", cause);
         }

         protected void mArrived(String topic, MqttMessage message) throws Exception {
         }

         protected void arrivedComplete(IMqttDeliveryToken token) {
         }
      };
      if (StringUtils.isNoneBlank(new CharSequence[]{willPayLoad})) {
         this.client = new CustomClient(this.options, () -> {
            return willPayLoad.getBytes(Charset.defaultCharset());
         }, mqttCallbackHandler, new IMqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {
               ManageStatistics.outMessageAdd(keyName);
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
               MqttOutput.log.error("[MQTT mqttBrokerActionListener] [info] onFailure", exception);
            }
         });
      } else {
         this.client = new CustomClient(this.options, (PayLoad)null, mqttCallbackHandler, new IMqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {
               ManageStatistics.outMessageAdd(keyName);
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
               MqttOutput.log.error("[MQTT mqttBrokerActionListener] [info] onFailure", exception);
            }
         });
      }

      this.client.setName(keyName);
      mqttCallbackHandler.setCustomClient(this.client);
      log.info("[Source MqttOutput] ClientName: {} ; ClientID:{} ; Config: {}", new Object[]{this.client.getName(), this.client.getClientID(), this.client.getConfig().toString()});
      this.client.run();
      log.info("wait Connect ....... " + keyName);

      while(!this.client.isConnect()) {
         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var27) {
            throw new RuntimeException(var27);
         }
      }

      log.info("Connect OK" + keyName);
      if (isCache) {
         this.RunCache();
         this.AddCache();
         if (expiredDataSize > 0L) {
            ManageCache.upCacheCount(cacheName);
         }
      }

   }

   public void RunCache() {
      try {
         ManageCache.creatCache(this.cacheName);
         TimeTaskManage.createNewTask(new Runnable() {
            public void run() {
               while(true) {
                  while(true) {
                     try {
                        if (!MqttOutput.this.client.isConnect()) {
                           Thread.sleep(1000L);
                        } else if (ManageCache.hasCache(MqttOutput.this.cacheName)) {
                           List<DataDb> dataDbs = ManageCache.readCache(MqttOutput.this.cacheName, MqttOutput.this.batchSize);
                           Iterator var2 = dataDbs.iterator();

                           while(var2.hasNext()) {
                              DataDb db = (DataDb)var2.next();
                              byte[] date = db.getDate();
                              if (StringUtils.isNoneBlank(new CharSequence[]{db.getTopic()}) && !ArrayUtils.isEmpty(date)) {
                                 MqttOutput.this.client.PublishMessage(db.getTopic(), date);
                              }
                           }

                           List<Long> delIdes = (List)dataDbs.stream().map((x) -> {
                              return x.getId();
                           }).collect(Collectors.toList());
                           ManageCache.delCache(delIdes, MqttOutput.this.cacheName);
                           Thread.sleep(MqttOutput.this.timeInterval);
                        } else {
                           Thread.sleep(1000L);
                        }
                     } catch (Exception var5) {
                        MqttOutput.log.error("RunCache", var5);
                     }
                  }
               }
            }
         });
      } catch (Exception var2) {
         log.error("MqttOutput:", var2);
      }

   }

   public void AddCache() {
      try {
         TimeTaskManage.createNewTask(new Runnable() {
            public void run() {
               List<MqttQueueData> cacheTemp = new ArrayList();

               while(true) {
                  while(true) {
                     try {
                        if (MqttOutput.this.cacheList.isEmpty()) {
                           Thread.sleep(1000L);
                        } else {
                           cacheTemp.clear();

                           for(int i = 0; (long)i < MqttOutput.this.queuePopSize && !MqttOutput.this.cacheList.isEmpty(); ++i) {
                              cacheTemp.add(MqttOutput.this.cacheList.poll());
                           }

                           Long count = ManageCache.getCacheCount(MqttOutput.this.cacheName);
                           if (MqttOutput.this.expiredDataSize > 0L && count > MqttOutput.this.expiredDataSize) {
                              ManageCache.clearExpiredData(MqttOutput.this.cacheName);
                           }

                           ManageCache.cacheBach(MqttOutput.this.cacheName, MqttOutput.this.messageType, cacheTemp);
                           cacheTemp.clear();
                        }
                     } catch (Exception var3) {
                        MqttOutput.log.error("MqttOutput:", var3);
                     }
                  }
               }
            }
         });
      } catch (Exception var2) {
         log.error("MqttOutput:", var2);
      }

   }

   public void Output(Object o) {
      ManageStatistics.outMessageAdd(this.keyName);

      try {
         String cacheTopicTemp;
         if (this.client.isConnect()) {
            cacheTopicTemp = this.getTopic();
            if (isPrint) {
               log.info("[Source Mqtt Output] broker: " + this.broker + " topic:" + cacheTopicTemp + " isConnect: " + this.client.isConnect());
            }

            this.client.PublishMessage(cacheTopicTemp, Util.getSendData(this.messageType, o));
         } else {
            cacheTopicTemp = this.getCacheTopic();
            if (isPrint) {
               log.info("[Source Mqtt Output] broker: " + this.broker + " topic:" + cacheTopicTemp + " isConnect: " + this.client.isConnect());
            }

            if (this.isCache) {
               MqttQueueData mqttQueueData = new MqttQueueData();
               mqttQueueData.setTopic(cacheTopicTemp);
               mqttQueueData.setObject(o);
               this.cacheList.offer(mqttQueueData);
            }
         }
      } catch (Exception var4) {
         log.error("Output ERR", var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   private String getTopic() {
      if (StringUtils.isBlank((CharSequence)this.topicThread.get())) {
         return this.topic;
      } else {
         String topic = (String)this.topicThread.get();
         this.topicThread.remove();
         return topic;
      }
   }

   public void setTopic(String topic) {
      this.topicThread.set(topic);
   }

   private String getCacheTopic() {
      if (StringUtils.isBlank((CharSequence)this.cacheTopicThread.get())) {
         return this.cacheTopic;
      } else {
         String cacheTopic = (String)this.cacheTopicThread.get();
         this.cacheTopicThread.remove();
         return cacheTopic;
      }
   }

   public void setCacheTopic(String cacheTopic) {
      this.cacheTopicThread.set(cacheTopic);
   }

   public String getConfigTopic() {
      return this.topic;
   }

   public String getConfigCacheTopic() {
      return this.cacheTopic;
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

   public Boolean getIsCache() {
      return this.isCache;
   }

   public void setIsCache(Boolean isCache) {
      this.isCache = isCache;
   }

   public String getCacheName() {
      return this.cacheName;
   }

   public void setCacheName(String cacheName) {
      this.cacheName = cacheName;
   }

   public Long getBatchSize() {
      return this.batchSize;
   }

   public void setBatchSize(Long batchSize) {
      this.batchSize = batchSize;
   }

   public Long getTimeInterval() {
      return this.timeInterval;
   }

   public void setTimeInterval(Long timeInterval) {
      this.timeInterval = timeInterval;
   }
}
