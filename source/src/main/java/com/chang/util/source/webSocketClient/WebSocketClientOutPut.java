package com.chang.util.source.webSocketClient;

import cn.hutool.core.util.ObjectUtil;
import com.chang.until.webSocket.client.Client;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.Util;
import java.net.URI;
import java.util.Map;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientOutPut implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(WebSocketClientOutPut.class);
   private String url;
   private String keyName;
   private Map<String, Object> parm;
   private String messageType;
   private Client client;

   public WebSocketClientOutPut(String url, String keyName, Map<String, Object> parm, String messageType) {
      this.url = url;
      this.keyName = keyName;
      this.parm = parm;
      this.messageType = messageType;

      try {
         this.client = new Client(new URI(url)) {
            protected void doMessage(String message) {
            }

            protected void doMessage(byte[] message) {
            }

            protected void doOpen(ServerHandshake handshake) {
            }
         };
         this.client.connect();
      } catch (Exception var6) {
         log.error("WebSocketClientOutPut Error", var6);
         throw new RuntimeException(var6);
      }
   }

   public void Output(Object o) {
      if (this.client.isOpen()) {
         this.client.send(Util.getSendData(this.messageType, o));
      }

   }

   public void close() {
      if (ObjectUtil.isNotNull(this.client)) {
         try {
            this.client.closeBlocking();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
