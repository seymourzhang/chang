package com.chang.netty.until.client;

import com.chang.netty.until.client.CustomClientHandler;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientListener implements GenericFutureListener<ChannelFuture> {
   private static final Logger log = LoggerFactory.getLogger(ClientListener.class);
   private InetSocketAddress address = null;
   private CustomClientHandler handler = null;
   private NettyClient client = null;

   public ClientListener(InetSocketAddress address, CustomClientHandler handler, NettyClient client) {
      this.address = address;
      this.handler = handler;
      this.client = client;
   }

   public void operationComplete(ChannelFuture future) throws Exception {
      if (!future.isSuccess()) {
         log.info("[Netty client] No connection to server reconnected");
         future.channel().eventLoop().schedule(() -> {
            this.client.doConnect();
         }, 5L, TimeUnit.SECONDS);
      } else {
         log.info(String.format("[Netty client] client " + this.client.getName() + " bind port %s sucess", this.address));
      }

   }
}
