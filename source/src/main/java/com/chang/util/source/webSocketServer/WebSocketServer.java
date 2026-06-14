package com.chang.util.source.webSocketServer;

import cn.hutool.core.util.ObjectUtil;
import com.chang.until.webSocket.server.Server;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import java.util.Map;
import java.util.function.Function;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServer implements InputSource<Object, Object>, OutputSource {
   private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
   private int port;
   private String keyName;
   private Map<String, Object> parm;
   private String messageType;
   private Server server;
   private IWebSocketApi socketApi;

   public WebSocketServer(int port, String keyName, String messageType, IWebSocketApi api, Map<String, Object> parm) {
      this.port = port;
      this.parm = parm;
      this.keyName = keyName;
      this.messageType = messageType;
      this.socketApi = api;
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) {
      this.server = new Server(this.port) {
         protected void doMessage(WebSocket conn, String message) {
            SourceContext.setExParm(WebSocketServer.this.parm);
            if (ObjectUtil.isNotNull(function)) {
               source.Output(function.apply(message));
            } else {
               source.Output(message);
            }

            SourceContext.clearExParm();
         }

         protected void doMessage(WebSocket conn, byte[] message) {
            SourceContext.setExParm(WebSocketServer.this.parm);

            Object revData;
            try {
               revData = Util.getRevData(WebSocketServer.this.messageType, message);
            } catch (Throwable var5) {
               WebSocketServer.log.error("messageType is err", var5);
               throw new RuntimeException("messageType is err", var5);
            }

            if (ObjectUtil.isNotNull(function)) {
               source.Output(function.apply(revData));
            } else {
               source.Output(revData);
            }

            SourceContext.clearExParm();
         }

         protected void doOpen(WebSocket conn, ClientHandshake handshake) {
            if (ObjectUtil.isNotNull(WebSocketServer.this.socketApi)) {
               WebSocketServer.this.socketApi.doOpen(conn, handshake);
            }

         }

         protected void doClose(WebSocket conn, int code, String reason, boolean remote) {
            if (ObjectUtil.isNotNull(WebSocketServer.this.socketApi)) {
               WebSocketServer.this.socketApi.doClose(conn, code, reason, remote);
            }

         }

         protected void doError(WebSocket conn, Exception ex) {
            if (ObjectUtil.isNotNull(WebSocketServer.this.socketApi)) {
               WebSocketServer.this.socketApi.doError(conn, ex);
            }

         }
      };
      this.server.start();
      log.info("Server started on port: " + this.server.getPort());
   }

   public void Output(Object o) {
      this.server.broadcast(Util.getSendData(this.messageType, o));
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      try {
         this.server.stop(10000);
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
