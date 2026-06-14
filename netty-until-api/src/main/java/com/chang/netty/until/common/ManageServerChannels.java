package com.chang.netty.until.common;

import io.netty.channel.ChannelId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageServerChannels {
   private static final Logger log = LoggerFactory.getLogger(ManageServerChannels.class);
   private static final Map<String, ChannelId> nId = new ConcurrentHashMap(1024);
   private static final Map<String, String> idName = new ConcurrentHashMap(1024);
   private static final Map<String, HeartBeat> idH = new ConcurrentHashMap(1024);
   private static final Map<String, HeartBeat> nH = new ConcurrentHashMap(1024);

   public static void add(String name, ChannelId id, HeartBeat heartBeat) {
      nId.put(name, id);
      idName.put(id.asLongText(), name);
      idH.put(id.asLongText(), heartBeat);
      nH.put(name, heartBeat);
   }

   public static ChannelId getChannelId(String name) {
      return (ChannelId)nId.get(name);
   }

   public static HeartBeat getHeartBeatById(String id) {
      return (HeartBeat)idH.get(id);
   }

   public static HeartBeat getHeartBeatByName(String name) {
      return (HeartBeat)nH.get(name);
   }

   public static String getName(String id) {
      return (String)idName.get(id);
   }

   public static void remove(ChannelId id) {
      String name = (String)idName.get(id.asLongText());
      if (StringUtils.isNotBlank(name)) {
         nId.remove(name);
         idName.remove(id.asLongText());
         idH.remove(id.asLongText());
         nH.remove(name);
      }

   }

   public static boolean hasClient(String name) {
      return nId.containsKey(name) && nH.containsKey(name);
   }
}
