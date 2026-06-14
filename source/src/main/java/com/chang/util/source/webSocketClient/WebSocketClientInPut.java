package com.chang.util.source.webSocketClient;

import cn.hutool.core.util.ObjectUtil;
import com.chang.until.webSocket.client.Client;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientInPut implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(WebSocketClientInPut.class);
   private String url;
   private String keyName;
   private Map<String, Object> parm;
   private String messageType;
   private Client client;

   public WebSocketClientInPut(String url, String keyName, Map<String, Object> parm, String messageType) {
      this.url = url;
      this.keyName = keyName;
      this.parm = parm;
      this.messageType = messageType;
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) {
      try {
         this.client = new Client(new URI(this.url)) {
            protected void doMessage(String message) {
               SourceContext.setExParm(WebSocketClientInPut.this.parm);
               if (ObjectUtil.isNotNull(function)) {
                  source.Output(function.apply(message));
               } else {
                  source.Output(message);
               }

               SourceContext.clearExParm();
            }

            protected void doMessage(byte[] message) {
               SourceContext.setExParm(WebSocketClientInPut.this.parm);

               Object revData;
               try {
                  revData = Util.getRevData(WebSocketClientInPut.this.messageType, message);
               } catch (Throwable var4) {
                  WebSocketClientInPut.log.error("messageType is err", var4);
                  throw new RuntimeException("messageType is err", var4);
               }

               if (ObjectUtil.isNotNull(function)) {
                  source.Output(function.apply(revData));
               } else {
                  source.Output(revData);
               }

               SourceContext.clearExParm();
            }

            protected void doOpen(ServerHandshake handshake) {
            }
         };
         this.client.connect();

         while(!this.client.isOpen()) {
            TimeUnit.SECONDS.sleep(1L);
         }
      } catch (Exception var4) {
         log.error("InPut Error", var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
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
