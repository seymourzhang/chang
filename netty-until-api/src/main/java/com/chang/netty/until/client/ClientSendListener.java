package com.chang.netty.until.client;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSendListener implements GenericFutureListener<ChannelFuture> {
   private static final Logger log = LoggerFactory.getLogger(ClientSendListener.class);
   private NettyClient client = null;

   public ClientSendListener(NettyClient client) {
      this.client = client;
   }

   public void operationComplete(ChannelFuture future) throws Exception {
      if (!future.isDone()) {
         log.debug("Client " + this.client.getName() + " send data fail");
      } else {
         log.debug("Client " + this.client.getName() + " send data ok");
      }

   }
}
