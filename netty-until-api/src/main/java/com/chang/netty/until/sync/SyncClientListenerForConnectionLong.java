package com.chang.netty.until.sync;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncClientListenerForConnectionLong implements GenericFutureListener<ChannelFuture> {
   private static final Logger logger = LoggerFactory.getLogger(SyncClientListenerForConnectionLong.class);
   private InetSocketAddress address = null;
   private SyncNettyClient client = null;

   public SyncClientListenerForConnectionLong(InetSocketAddress address, SyncNettyClient client) {
      this.address = address;
      this.client = client;
   }

   public void operationComplete(ChannelFuture future) throws Exception {
      if (!future.isSuccess()) {
         logger.debug("[Netty client] No connection to server reconnected");
         if (null != this.client.getNetEvent()) {
            this.client.getNetEvent().connectionFail(this.client.getAddress().toString());
         }

         future.channel().eventLoop().schedule(() -> {
            this.client.doConnect();
         }, 10L, TimeUnit.SECONDS);
      } else {
         logger.debug(String.format("[Netty client] client  bind port %s sucess", this.address));
      }

   }
}
