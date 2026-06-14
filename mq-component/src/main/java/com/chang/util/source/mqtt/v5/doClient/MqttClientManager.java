package com.chang.util.source.mqtt.v5.doClient;

import com.chang.util.source.mqtt.v5.common.MqttClientOptions;
import com.chang.common.CommUtils;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MqttClientManager {
   private static final Logger logger = LoggerFactory.getLogger(CustomClient.class);
   private static final ConcurrentHashMap<String, CustomClient> serviceMap = new ConcurrentHashMap();

   private static byte[] compressMessage(byte[] message_byte) throws Exception {
      return (byte[]) CommUtils.gzipCompress(message_byte).get();
   }

   public static boolean addPublishClient(String clientName, CustomClient customClient) {
      if (serviceMap.containsKey(clientName)) {
         return false;
      } else {
         serviceMap.put(clientName, customClient);
         return true;
      }
   }

   public static int getInFlight(String clientName) {
      return ((CustomClient)serviceMap.get(clientName)).getInFlight();
   }

   public static MqttClientOptions getProperties(String clientName) {
      if (!serviceMap.containsKey(clientName)) {
         throw new RuntimeException("Client not found");
      } else {
         return ((CustomClient)serviceMap.get(clientName)).getConfig();
      }
   }

   public static boolean sendMessage(List<String> names, String message) throws MqttException {
      Iterator var2 = names.iterator();

      String name;
      while(var2.hasNext()) {
         name = (String)var2.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var2 = names.iterator();

      while(var2.hasNext()) {
         name = (String)var2.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(message);
      }

      return true;
   }

   public static boolean sendMessage(List<String> names, byte[] msgBye) throws MqttException {
      Iterator var2 = names.iterator();

      String name;
      while(var2.hasNext()) {
         name = (String)var2.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var2 = names.iterator();

      while(var2.hasNext()) {
         name = (String)var2.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(msgBye);
      }

      return true;
   }

   public static boolean sendCompressMessage(List<String> names, String message) throws Exception {
      Iterator var2 = names.iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(message.getBytes()));
      Iterator var6 = names.iterator();

      while(var6.hasNext()) {
         String name = (String)var6.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(mqttMessage);
      }

      return true;
   }

   public static boolean sendCompressMessage(List<String> names, byte[] msgBye) throws Exception {
      Iterator var2 = names.iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(msgBye));
      Iterator var6 = names.iterator();

      while(var6.hasNext()) {
         String name = (String)var6.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(mqttMessage);
      }

      return true;
   }

   public static boolean sendMessage(List<String> names, MqttMessage msgContent) throws MqttException {
      Iterator var2 = names.iterator();

      String name;
      while(var2.hasNext()) {
         name = (String)var2.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var2 = names.iterator();

      while(var2.hasNext()) {
         name = (String)var2.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(msgContent);
      }

      return true;
   }

   public static boolean sendCompressMessage(List<String> names, MqttMessage msgContent) throws Exception {
      Iterator var2 = names.iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      byte[] messageCompressByte = compressMessage(msgContent.getPayload());
      msgContent.setPayload(messageCompressByte);
      Iterator var6 = names.iterator();

      while(var6.hasNext()) {
         String name = (String)var6.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(msgContent);
      }

      return true;
   }

   public static boolean sendMessage(List<String> names, MqttMessage msgContent, long timeout) throws MqttException {
      Iterator var4 = names.iterator();

      String name;
      while(var4.hasNext()) {
         name = (String)var4.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var4 = names.iterator();

      while(var4.hasNext()) {
         name = (String)var4.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(msgContent, timeout);
      }

      return true;
   }

   public static boolean sendCompressMessage(List<String> names, MqttMessage msgContent, long timeout) throws Exception {
      Iterator var4 = names.iterator();

      while(var4.hasNext()) {
         String name = (String)var4.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      byte[] messageCompressByte = compressMessage(msgContent.getPayload());
      msgContent.setPayload(messageCompressByte);
      Iterator var8 = names.iterator();

      while(var8.hasNext()) {
         String name = (String)var8.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(msgContent, timeout);
      }

      return true;
   }

   public static boolean sendMessage(List<String> names, String message, long timeout) throws MqttException {
      Iterator var4 = names.iterator();

      String name;
      while(var4.hasNext()) {
         name = (String)var4.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var4 = names.iterator();

      while(var4.hasNext()) {
         name = (String)var4.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(message, timeout);
      }

      return true;
   }

   public static boolean sendMessage(List<String> names, byte[] msgBye, long timeout) throws MqttException {
      Iterator var4 = names.iterator();

      String name;
      while(var4.hasNext()) {
         name = (String)var4.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var4 = names.iterator();

      while(var4.hasNext()) {
         name = (String)var4.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(msgBye, timeout);
      }

      return true;
   }

   public static boolean sendCompressMessage(List<String> names, String message, long timeout) throws Exception {
      Iterator var4 = names.iterator();

      while(var4.hasNext()) {
         String name = (String)var4.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(message.getBytes()));
      Iterator var8 = names.iterator();

      while(var8.hasNext()) {
         String name = (String)var8.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(mqttMessage, timeout);
      }

      return true;
   }

   public static boolean sendCompressMessage(List<String> names, byte[] msgBye, long timeout) throws Exception {
      Iterator var4 = names.iterator();

      while(var4.hasNext()) {
         String name = (String)var4.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(msgBye));
      Iterator var8 = names.iterator();

      while(var8.hasNext()) {
         String name = (String)var8.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(mqttMessage, timeout);
      }

      return true;
   }

   public static boolean sendMessage(String topic, List<String> names, String message) throws MqttException {
      Iterator var3 = names.iterator();

      String name;
      while(var3.hasNext()) {
         name = (String)var3.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var3 = names.iterator();

      while(var3.hasNext()) {
         name = (String)var3.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, message);
      }

      return true;
   }

   public static boolean sendMessage(String topic, List<String> names, byte[] msgBye) throws MqttException {
      Iterator var3 = names.iterator();

      String name;
      while(var3.hasNext()) {
         name = (String)var3.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var3 = names.iterator();

      while(var3.hasNext()) {
         name = (String)var3.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, msgBye);
      }

      return true;
   }

   public static boolean sendCompressMessage(String topic, List<String> names, String message) throws Exception {
      Iterator var3 = names.iterator();

      while(var3.hasNext()) {
         String name = (String)var3.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(message.getBytes()));
      Iterator var7 = names.iterator();

      while(var7.hasNext()) {
         String name = (String)var7.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, mqttMessage);
      }

      return true;
   }

   public static boolean sendCompressMessage(String topic, List<String> names, byte[] msgBye) throws Exception {
      Iterator var3 = names.iterator();

      while(var3.hasNext()) {
         String name = (String)var3.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(msgBye));
      Iterator var7 = names.iterator();

      while(var7.hasNext()) {
         String name = (String)var7.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, mqttMessage);
      }

      return true;
   }

   public static boolean sendMessage(String topic, List<String> names, String message, long timeout) throws MqttException {
      Iterator var5 = names.iterator();

      String name;
      while(var5.hasNext()) {
         name = (String)var5.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var5 = names.iterator();

      while(var5.hasNext()) {
         name = (String)var5.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, message, timeout);
      }

      return true;
   }

   public static boolean sendMessage(String topic, List<String> names, byte[] msgBye, long timeout) throws MqttException {
      Iterator var5 = names.iterator();

      String name;
      while(var5.hasNext()) {
         name = (String)var5.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      var5 = names.iterator();

      while(var5.hasNext()) {
         name = (String)var5.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, msgBye, timeout);
      }

      return true;
   }

   public static boolean sendCompressMessage(String topic, List<String> names, String message, long timeout) throws Exception {
      Iterator var5 = names.iterator();

      while(var5.hasNext()) {
         String name = (String)var5.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(message.getBytes()));
      Iterator var9 = names.iterator();

      while(var9.hasNext()) {
         String name = (String)var9.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, mqttMessage, timeout);
      }

      return true;
   }

   public static boolean sendCompressMessage(String topic, List<String> names, byte[] msgBye, long timeout) throws Exception {
      Iterator var5 = names.iterator();

      while(var5.hasNext()) {
         String name = (String)var5.next();
         if (!serviceMap.containsKey(name)) {
            return false;
         }
      }

      MqttMessage mqttMessage = new MqttMessage(compressMessage(msgBye));
      Iterator var9 = names.iterator();

      while(var9.hasNext()) {
         String name = (String)var9.next();
         ((CustomClient)serviceMap.get(name)).PublishMessage(topic, mqttMessage, timeout);
      }

      return true;
   }

   public static List<String> getBrokerName() {
      List<String> brokers = new ArrayList();
      Iterator var1 = serviceMap.keySet().iterator();

      while(var1.hasNext()) {
         String value = (String)var1.next();
         brokers.add(value);
      }

      return brokers;
   }

   public static String getBrokerName(String serverURI) {
      if (!StringUtils.hasText(serverURI)) {
         return null;
      } else {
         Iterator var1 = serviceMap.entrySet().iterator();

         String brokerName;
         CustomClient customClient;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            Map.Entry<String, CustomClient> entry = (Map.Entry)var1.next();
            brokerName = (String)entry.getKey();
            customClient = (CustomClient)entry.getValue();
         } while(!customClient.getConfig().getBroker().equals(serverURI));

         return brokerName;
      }
   }

   private static boolean hasClient(String name) {
      return serviceMap.containsKey(name);
   }

   public static boolean isConnect(String name) {
      return ((CustomClient)serviceMap.get(name)).isConnect();
   }
}
