package com.chang.netty.until.client;

import com.chang.netty.until.client.ClientListener;
import com.chang.netty.until.client.ClientSendListener;
import com.chang.netty.until.client.CustomClientHandler;
import com.chang.netty.until.common.Initializer;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.netty.until.common.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {
   private static final Logger log = LoggerFactory.getLogger(NettyClient.class);
   private ChannelFuture future = null;
   private EventLoopGroup workerGroup = null;
   private Bootstrap bootstrap = null;
   private InetSocketAddress address = null;
   private CustomClientHandler handler = null;
   private String name;
   private boolean isSendHeartBeat;
   private InitializerConfig config;
   private String groupName;
   private static final ExecutorService es = Executors.newSingleThreadExecutor();

   public NettyClient(String name, InetSocketAddress address, CustomClientHandler handler, InitializerConfig config, boolean isSendHeartBeat, String groupName) {
      this.config = config;
      this.name = name;
      this.address = address;
      this.handler = handler;
      this.isSendHeartBeat = isSendHeartBeat;
      this.groupName = groupName;
      this.workerGroup = new NioEventLoopGroup();
      this.bootstrap = new Bootstrap();
      ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)this.bootstrap.group(this.workerGroup)).channel(NioSocketChannel.class)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())).option(ChannelOption.SO_KEEPALIVE, true)).handler(new Initializer(handler, config, name));
      handler.setClient(this);
   }

   public String getName() {
      return this.name;
   }

   public InitializerConfig getConfig() {
      return this.config;
   }

   public boolean isSendHeartBeat() {
      return this.isSendHeartBeat;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void sendMessage(Object content) {
      if (this.future.channel().isActive()) {
         if (content instanceof String) {
            this.future.channel().writeAndFlush(content + "$_$").addListener(new ClientSendListener(this));
         } else {
            this.future.channel().writeAndFlush(content).addListener(new ClientSendListener(this));
         }

      } else {
         throw new RuntimeException("[Netty Client] [error] the channel is close , send data fail");
      }
   }

   public void doConnect() {
      try {
         if (null == this.bootstrap) {
            log.info("[Netty Client] null == bootstrap");
         } else {
            this.future = this.bootstrap.connect(this.address);
            this.future.addListener(new ClientListener(this.address, this.handler, this));
            log.info("[Netty Client] Client Name " + this.name + " is doConnect");
            NettyUtil.sendHeartBeat(this.future.channel(), this.config.getDataType(), this.name, "Client", this.groupName);
         }
      } catch (Exception var2) {
         log.error("[Netty Client] Exception", var2);
         throw new RuntimeException("[Netty Client] Exception", var2);
      }
   }

   public boolean isActive() {
      return null != this.future && null != this.workerGroup ? this.future.channel().isActive() : false;
   }

   public void doCloseClient() {
      if (null != this.future && null != this.workerGroup) {
         this.future.channel().pipeline().fireChannelInactive();
         this.future.channel().close();
         this.workerGroup.shutdownGracefully();
      }

   }

   protected void finalize() throws Throwable {
      if (null != this.future && null != this.workerGroup) {
         this.future.channel().pipeline().fireChannelInactive();
         this.future.channel().close();
         this.workerGroup.shutdownGracefully();
      }

   }
}
