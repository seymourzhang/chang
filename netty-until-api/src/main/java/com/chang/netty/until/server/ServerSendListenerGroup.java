package com.chang.netty.until.server;

import io.netty.channel.group.ChannelGroupFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSendListenerGroup implements GenericFutureListener<ChannelGroupFuture> {
   private static final Logger log = LoggerFactory.getLogger(ServerSendListenerGroup.class);
   private String groupName;

   public ServerSendListenerGroup(String groupName) {
      this.groupName = groupName;
   }

   public void operationComplete(ChannelGroupFuture future) throws Exception {
      if (!future.isDone()) {
         log.info("group name " + this.groupName + " send data fail");
      }

   }
}
