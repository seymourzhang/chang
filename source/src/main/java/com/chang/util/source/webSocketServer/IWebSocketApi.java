package com.chang.util.source.webSocketServer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public interface IWebSocketApi {
   void doOpen(WebSocket var1, ClientHandshake var2);

   void doClose(WebSocket var1, int var2, String var3, boolean var4);

   void doError(WebSocket var1, Exception var2);
}
