package com.chang.netty.until.server;

import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSendListener implements GenericFutureListener<DefaultChannelPromise> {
   private static final Logger log = LoggerFactory.getLogger(ServerSendListener.class);
   private String groupName;

   public ServerSendListener(String groupName) {
      this.groupName = groupName;
   }

   public void operationComplete(DefaultChannelPromise future) throws Exception {
      if (!future.isDone()) {
         log.info("group name " + this.groupName + " send data fail");
      }

   }
}
