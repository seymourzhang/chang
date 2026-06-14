package com.chang.netty.until.sync;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncClientListener implements GenericFutureListener<ChannelFuture> {
   private static final Logger logger = LoggerFactory.getLogger(SyncClientListener.class);
   private InetSocketAddress address = null;
   SyncNettyClient client;

   public SyncClientListener(InetSocketAddress address, SyncNettyClient client) {
      this.address = address;
      this.client = client;
   }

   public void operationComplete(ChannelFuture future) throws Exception {
      if (this.client.getAtomic().get() >= 5) {
         future.channel().close();
         logger.debug("[Netty client] No connection to server time out");
         this.client.getAtomic().set(0);
      } else {
         if (!future.isSuccess()) {
            logger.debug("[Netty client] No connection to server reconnected");
            this.client.doConnect();
            this.client.getAtomic().getAndIncrement();
         } else {
            logger.debug(String.format("[Netty client] client bind port %s sucess", this.address));
         }

      }
   }
}
