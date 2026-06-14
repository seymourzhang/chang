package com.chang.netty.until.sync;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncSendListener implements GenericFutureListener<ChannelFuture> {
   private static final Logger logger = LoggerFactory.getLogger(SyncSendListener.class);

   public void operationComplete(ChannelFuture future) throws Exception {
      if (!future.isDone()) {
         logger.debug("Client  send data fail");
      } else {
         logger.debug("Client  send data ok");
      }

   }
}
