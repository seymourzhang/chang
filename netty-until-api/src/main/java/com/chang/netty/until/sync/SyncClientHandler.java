package com.chang.netty.until.sync;

import com.chang.netty.until.client.ServerInfo;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.RunTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public abstract class SyncClientHandler extends SimpleChannelInboundHandler<Object> {
   private static final Logger logger = LoggerFactory.getLogger(SyncClientHandler.class);
   private volatile Object data;
   private SyncNettyClient syncNettyClient = null;
   private ChannelHandlerContext ctx = null;
   private static final String HELLO = "hello";

   public ChannelHandlerContext getCtx() {
      return this.ctx;
   }

   public void setCtx(ChannelHandlerContext ctx) {
      this.ctx = ctx;
   }

   public void setData(Object data) {
      this.data = data;
   }

   public void setSyncNettyClient(SyncNettyClient syncNettyClient) {
      this.syncNettyClient = syncNettyClient;
   }

   public SyncClientHandler() {
   }

   public SyncClientHandler(SyncNettyClient syncNettyClient) {
      this.syncNettyClient = syncNettyClient;
   }

   protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
      String serverAddr = channelHandlerContext.channel().remoteAddress().toString();
      ServerInfo serverInfo = ServerInfo.builder().serverAddr(serverAddr).ctx(channelHandlerContext).build();
      RunTask.createNewTask(() -> {
         this.handleData(serverInfo, msg);
      });
      if (this.syncNettyClient.getIsStartSend().get() && null != this.syncNettyClient.getPromise() && !this.syncNettyClient.getPromise().isSuccess()) {
         this.data = msg;
         this.syncNettyClient.getPromise().setSuccess();
      }

   }

   public Object getData() {
      return this.data;
   }

   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      DataType dataType = this.syncNettyClient.getDataType();
      if (null != this.syncNettyClient) {
         if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent)evt;
            switch (e.state()) {
               case READER_IDLE:
               default:
                  break;
               case WRITER_IDLE:
                  if (dataType == DataType.STRING) {
                     ctx.writeAndFlush("clientHello$_$");
                  } else {
                     ctx.writeAndFlush("clientHello".getBytes(CharsetUtil.UTF_8));
                  }
                  break;
               case ALL_IDLE:
                  ctx.close();
            }
         }

         super.userEventTriggered(ctx, evt);
      }
   }

   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      logger.debug("[Netty Client] channel is active");
      super.channelActive(ctx);
      this.ctx = ctx;
      if (null != this.syncNettyClient.getNetEvent()) {
         this.syncNettyClient.getNetEvent().connectionOk(ctx.channel().remoteAddress().toString());
      }

   }

   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      logger.debug("[Netty Client] channel is Inactive");
      super.channelInactive(ctx);
      this.ctx = null;
      if (null != this.syncNettyClient.getNetEvent()) {
         this.syncNettyClient.getNetEvent().connectionLost(ctx.channel().remoteAddress().toString());
      }

      if (this.syncNettyClient.isLongConnection()) {
         this.syncNettyClient.doConnect();
      }

   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      logger.error("channel err info: " + ctx.channel().remoteAddress().toString(), cause);
   }

   protected abstract void handleData(ServerInfo var1, Object var2);
}
