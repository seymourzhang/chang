package com.chang.until.webSocket.client;

import com.chang.until.webSocket.common.RunTask;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Client extends WebSocketClient {
   private static final Logger log = LoggerFactory.getLogger(Client.class);

   public Client(URI serverUri) {
      super(serverUri);
   }

   public Client(URI serverUri, Draft protocolDraft) {
      super(serverUri, protocolDraft);
   }

   public Client(URI serverUri, Map<String, String> httpHeaders) {
      super(serverUri, httpHeaders);
   }

   public Client(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
      super(serverUri, protocolDraft, httpHeaders);
   }

   public Client(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
      super(serverUri, protocolDraft, httpHeaders, connectTimeout);
   }

   public void onOpen(ServerHandshake handshake) {
      RunTask.createNewTask(() -> {
         this.doOpen(handshake);
      });
   }

   public void onMessage(String message) {
      RunTask.createNewTask(() -> {
         this.doMessage(message);
      });
   }

   public void onMessage(ByteBuffer bytes) {
      RunTask.createNewTask(() -> {
         this.doMessage(bytes.array());
      });
   }

   public void onClose(int code, String reason, boolean remote) {
      log.info("You have been disconnected from: " + this.getURI() + "; Code: " + code + " Reason: " + reason);
      RunTask.createNewTask(() -> {
         try {
            TimeUnit.SECONDS.sleep(5L);
            this.reconnect();
         } catch (Exception var2) {
            log.error("onClose Error: ", var2);
            throw new RuntimeException(var2);
         }
      });
   }

   public void onError(Exception ex) {
      log.error("onError: ", ex);
   }

   protected abstract void doMessage(String var1);

   protected abstract void doMessage(byte[] var1);

   protected abstract void doOpen(ServerHandshake var1);
}
