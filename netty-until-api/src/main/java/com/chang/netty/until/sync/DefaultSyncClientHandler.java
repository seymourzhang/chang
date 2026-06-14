package com.chang.netty.until.sync;

import com.chang.netty.until.client.ServerInfo;

public class DefaultSyncClientHandler extends SyncClientHandler {
   public DefaultSyncClientHandler(SyncNettyClient syncNettyClient) {
      super(syncNettyClient);
   }

   protected void handleData(ServerInfo serverInfo, Object msg) {
   }
}
