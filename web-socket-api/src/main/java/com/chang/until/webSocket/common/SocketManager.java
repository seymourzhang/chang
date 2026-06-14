package com.chang.until.webSocket.common;

import cn.hutool.core.util.ObjectUtil;
import java.util.concurrent.ConcurrentHashMap;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketManager {
   private static final Logger log = LoggerFactory.getLogger(SocketManager.class);
   private static final ConcurrentHashMap<String, WebSocket> manager = new ConcurrentHashMap();

   public static void add(String key, WebSocket webSocket) {
      manager.put(key, webSocket);
   }

   public static void remove(String key) {
      WebSocket webSocket = (WebSocket)manager.get(key);
      if (ObjectUtil.isNotNull(webSocket) && webSocket.isOpen()) {
         webSocket.close();
      }

      manager.remove(key);
   }

   public static WebSocket get(String key) {
      return (WebSocket)manager.get(key);
   }
}
