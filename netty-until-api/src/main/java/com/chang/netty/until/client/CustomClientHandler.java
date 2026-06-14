package com.chang.netty.until.client;

import cn.hutool.core.util.ObjectUtil;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.HeartBeat;
import com.chang.netty.until.common.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public abstract class CustomClientHandler extends SimpleChannelInboundHandler<Object> {
   private static final Logger log = LoggerFactory.getLogger(CustomClientHandler.class);
   private NettyClient client = null;
   private ChannelHandlerContext ctx = null;

   public void setClient(NettyClient client) {
      this.client = client;
   }

   public ChannelHandlerContext getCtx() {
      return this.ctx;
   }

   protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
      String serverAddr = channelHandlerContext.channel().remoteAddress().toString();
      DataType dataType = this.client.getConfig().getDataType();
      HeartBeat heartBeat = NettyUtil.findHeartBeat(msg, dataType);
      if (!ObjectUtil.isNotNull(heartBeat)) {
         ServerInfo serverInfo = ServerInfo.builder().serverAddr(serverAddr).ctx(channelHandlerContext).build();
         this.handleData(serverInfo, msg);
      }
   }

   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      if (null != this.client) {
         DataType dataType = this.client.getConfig().getDataType();
         if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent)evt;
            switch (e.state()) {
               case READER_IDLE:
               case WRITER_IDLE:
                  if (this.client.isSendHeartBeat()) {
                     NettyUtil.sendHeartBeat(ctx.channel(), dataType, this.client.getName(), "client", this.client.getGroupName());
                  }
                  break;
               case ALL_IDLE:
                  ctx.close();
            }
         }

         super.userEventTriggered(ctx, evt);
      }
   }

   protected abstract void handleData(ServerInfo var1, Object var2) throws Exception;

   protected abstract void handleInactive(ServerInfo var1) throws Exception;

   protected abstract void handleActive(ServerInfo var1) throws Exception;

   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      super.channelInactive(ctx);
      if (null != this.client) {
         this.client.doConnect();
         ServerInfo serverInfo = ServerInfo.builder().serverAddr(ctx.channel().remoteAddress().toString()).ctx(ctx).build();
         this.handleInactive(serverInfo);
      }
   }

   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      this.ctx = ctx;
      ServerInfo serverInfo = ServerInfo.builder().serverAddr(ctx.channel().remoteAddress().toString()).ctx(ctx).build();
      this.handleActive(serverInfo);
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      log.error("channel err info: " + ctx.channel().remoteAddress().toString(), cause);
   }
}
