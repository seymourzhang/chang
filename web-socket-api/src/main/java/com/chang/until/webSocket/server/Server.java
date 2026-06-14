package com.chang.until.webSocket.server;

import com.chang.until.webSocket.common.RunTask;
import com.chang.until.webSocket.common.SocketManager;
import com.chang.common.CommUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Server extends WebSocketServer {
   private static final Logger log = LoggerFactory.getLogger(Server.class);

   public Server(int port) {
      super(new InetSocketAddress(port));
   }

   public Server(InetSocketAddress address) {
      super(address);
   }

   public Server(int port, Draft_6455 draft) {
      super(new InetSocketAddress(port), Collections.singletonList(draft));
   }

   public void onOpen(WebSocket conn, ClientHandshake handshake) {
      String hostAddress = conn.getRemoteSocketAddress().getAddress().getHostAddress();
      String attachmentId = CommUtils.getSnowflakeIdStr(1L);
      log.info("new connection: ID: {} | Address: {}", attachmentId, hostAddress);
      conn.setAttachment(attachmentId);
      SocketManager.add(attachmentId, conn);
      RunTask.createNewTask(() -> {
         this.doOpen(conn, handshake);
      });
   }

   public void onClose(WebSocket conn, int code, String reason, boolean remote) {
      String hostAddress = conn.getRemoteSocketAddress().getAddress().getHostAddress();
      String attachmentId = (String)conn.getAttachment();
      log.info("Client has left: ID: {} | Address: {} ｜ code: {} | reason: {} | remote: {}", new Object[]{attachmentId, hostAddress, code, reason, remote});
      SocketManager.remove((String)conn.getAttachment());
      RunTask.createNewTask(() -> {
         this.doClose(conn, code, reason, remote);
      });
   }

   public void onMessage(WebSocket conn, String message) {
      RunTask.createNewTask(() -> {
         this.doMessage(conn, message);
      });
   }

   public void onMessage(WebSocket conn, ByteBuffer message) {
      RunTask.createNewTask(() -> {
         this.doMessage(conn, message.array());
      });
   }

   public void onError(WebSocket conn, Exception ex) {
      if (conn != null) {
         String hostAddress = conn.getRemoteSocketAddress().getAddress().getHostAddress();
         String attachmentId = (String)conn.getAttachment();
         log.error("Client has Error: ID: " + attachmentId + " | Address: " + hostAddress, ex);
         SocketManager.remove((String)conn.getAttachment());
      }

      RunTask.createNewTask(() -> {
         this.doError(conn, ex);
      });
   }

   public void onStart() {
      log.info("Server started!");
      this.setConnectionLostTimeout(0);
      this.setConnectionLostTimeout(100);
   }

   public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
      return super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
   }

   protected abstract void doMessage(WebSocket var1, String var2);

   protected abstract void doMessage(WebSocket var1, byte[] var2);

   protected abstract void doOpen(WebSocket var1, ClientHandshake var2);

   protected abstract void doClose(WebSocket var1, int var2, String var3, boolean var4);

   protected abstract void doError(WebSocket var1, Exception var2);
}
