package com.chang.netty.until.server;

import cn.hutool.core.util.ObjectUtil;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.HeartBeat;
import com.chang.netty.until.common.ManageServerChannels;
import com.chang.netty.until.common.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public abstract class CustomServerHandler extends SimpleChannelInboundHandler<Object> {
   private static final Logger log = LoggerFactory.getLogger(CustomServerHandler.class);
   private static final Logger logger = LoggerFactory.getLogger(CustomServerHandler.class);
   private NettyServer nettyServer = null;

   public void setNettyServer(NettyServer nettyServer) {
      this.nettyServer = nettyServer;
   }

   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
   }

   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      super.channelInactive(ctx);
      ctx.close();
      ManageServerChannels.remove(ctx.channel().id());
      HeartBeat heartBeat = ManageServerChannels.getHeartBeatById(ctx.channel().id().asLongText());
      ChannelGroups.removeClient(heartBeat.getGroupName(), heartBeat.getName());
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      logger.error("[Netty Server] exceptionCaught", cause);
      ctx.close();
      ManageServerChannels.remove(ctx.channel().id());
      HeartBeat heartBeat = ManageServerChannels.getHeartBeatById(ctx.channel().id().asLongText());
      ChannelGroups.removeClient(heartBeat.getGroupName(), heartBeat.getName());
   }

   protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
      String clientAddr = channelHandlerContext.channel().remoteAddress().toString();
      ClientInfo clientInfo = ClientInfo.builder().name(channelHandlerContext.name()).clientAddr(clientAddr).ctx(channelHandlerContext).build();
      DataType dataType = this.nettyServer.getConfig().getDataType();
      HeartBeat heartBeat = NettyUtil.findHeartBeat(msg, dataType);
      if (ObjectUtil.isNotNull(heartBeat)) {
         ManageServerChannels.add(heartBeat.getName(), channelHandlerContext.channel().id(), heartBeat);
         ChannelGroups.addChannel(heartBeat.getGroupName(), channelHandlerContext.channel());
      } else {
         this.handleData(clientInfo, msg);
      }
   }

   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      DataType dataType = this.nettyServer.getConfig().getDataType();
      if (evt instanceof IdleStateEvent) {
         IdleStateEvent e = (IdleStateEvent)evt;
         switch (e.state()) {
            case READER_IDLE:
            case WRITER_IDLE:
            case ALL_IDLE:
               if (this.nettyServer.isSendHeartBeat()) {
                  NettyUtil.sendHeartBeat(ctx.channel(), dataType, this.nettyServer.getServerName(), "Server", (String)null);
               }
         }
      }

      super.userEventTriggered(ctx, evt);
   }

   protected abstract void handleData(ClientInfo var1, Object var2) throws Exception;
}
