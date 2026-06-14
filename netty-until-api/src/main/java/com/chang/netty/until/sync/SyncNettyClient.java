package com.chang.netty.until.sync;

import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.Initializer;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.netty.until.sync.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncNettyClient {
   private static final Logger logger = LoggerFactory.getLogger(SyncNettyClient.class);
   private ChannelFuture future = null;
   private EventLoopGroup workerGroup = null;
   private Bootstrap bootstrap = null;
   private InetSocketAddress address = null;
   private SyncClientHandler handler = null;
   private ChannelPromise promise = null;
   private final AtomicInteger atomic = new AtomicInteger(0);
   private final AtomicBoolean isStartSend = new AtomicBoolean(false);
   private DataType dataType;
   private boolean isLongConnection = false;
   private boolean heart = true;
   private NetEvent netEvent = null;
   private long writerIdleTime = 10L;
   private String name;

   public boolean isLongConnection() {
      return this.isLongConnection;
   }

   public AtomicInteger getAtomic() {
      return this.atomic;
   }

   public AtomicBoolean getIsStartSend() {
      return this.isStartSend;
   }

   public DataType getDataType() {
      return this.dataType;
   }

   public ChannelPromise getPromise() {
      return this.promise;
   }

   public NetEvent getNetEvent() {
      return this.netEvent;
   }

   public InetSocketAddress getAddress() {
      return this.address;
   }

   private synchronized Object sendMessageForSyncLongConnection(Object content, long timeout, TimeUnit unit) throws InterruptedException {
      int i = 0;

      while(!this.future.channel().isActive()) {
         Thread.sleep(100L);
         ++i;
         if (i == 5) {
            break;
         }
      }

      if (this.future.channel().isActive()) {
         this.isStartSend.set(true);
         this.handler.setData((Object)null);
         if (this.dataType == DataType.STRING) {
            this.promise = this.future.channel().writeAndFlush(content + "$_$").addListener(new SyncSendListener()).channel().newPromise();
         } else {
            this.promise = this.future.channel().writeAndFlush(content).addListener(new SyncSendListener()).channel().newPromise();
         }

         logger.debug("[Netty Client] send ok wait back!");
         boolean waitback = this.promise.await(timeout, unit);
         this.isStartSend.set(false);
         if (!waitback) {
            logger.debug("[Netty Client] sendMessageForSync wait back time out!");
            return null;
         } else {
            Object data = this.handler.getData();
            this.handler.setData((Object)null);
            return data;
         }
      } else {
         logger.debug("[Netty Client] connection to server close");
         return null;
      }
   }

   private synchronized Object sendMessageForSyncNotLongConnection(Object content, long timeout, TimeUnit unit) throws InterruptedException {
      this.doConnect();
      int i = 0;

      while(!this.future.channel().isActive()) {
         Thread.sleep(100L);
         ++i;
         if (i == 5) {
            break;
         }
      }

      if (this.future.channel().isActive()) {
         this.isStartSend.set(true);
         this.handler.setData((Object)null);
         if (this.dataType == DataType.STRING) {
            this.promise = this.future.channel().writeAndFlush(content + "$_$").addListener(new SyncSendListener()).channel().newPromise();
         } else {
            this.promise = this.future.channel().writeAndFlush(content).addListener(new SyncSendListener()).channel().newPromise();
         }

         logger.debug("[Netty Client] send ok wait back!");
         boolean waitback = this.promise.await(timeout, unit);
         this.isStartSend.set(false);
         this.future.channel().close();
         if (!waitback) {
            logger.debug("[Netty Client] sendMessageForSync wait back time out!");
            this.handler.setData((Object)null);
         }

         this.handler.setCtx((ChannelHandlerContext)null);
         Object data = this.handler.getData();
         this.handler.setData((Object)null);
         return data;
      } else {
         this.future.channel().close();
         logger.debug("[Netty Client] connection to server close");
         return null;
      }
   }

   public Object sendMessageForSync(Object content, long timeout, TimeUnit unit) throws InterruptedException {
      Object returnData = null;
      if (this.isLongConnection) {
         returnData = this.sendMessageForSyncLongConnection(content, timeout, unit);
      } else {
         returnData = this.sendMessageForSyncNotLongConnection(content, timeout, unit);
      }

      return returnData;
   }

   public SyncNettyClient(String hostname, int port, DataType dataType, NetEvent netEvent, boolean isLongConnection, boolean heart, long writerIdleTime, String name) {
      InetSocketAddress address = new InetSocketAddress(hostname, port);
      this.writerIdleTime = writerIdleTime;
      this.heart = heart;
      this.dataType = dataType;
      this.netEvent = netEvent;
      this.isLongConnection = isLongConnection;
      this.address = address;
      this.name = name;
      this.handler = new DefaultSyncClientHandler(this);
      this.workerGroup = new NioEventLoopGroup();
      this.bootstrap = new Bootstrap();
      ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)this.bootstrap.group(this.workerGroup)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())).option(ChannelOption.SO_KEEPALIVE, true)).channel(NioSocketChannel.class);
      if (isLongConnection) {
         this.doConnect();
      }

   }

   public SyncNettyClient(String hostname, int port, SyncClientHandler handler, DataType dataType, NetEvent netEvent, boolean isLongConnection, boolean heart, long writerIdleTime, String name) {
      InetSocketAddress address = new InetSocketAddress(hostname, port);
      this.writerIdleTime = writerIdleTime;
      this.heart = heart;
      this.dataType = dataType;
      this.netEvent = netEvent;
      this.isLongConnection = isLongConnection;
      this.address = address;
      this.handler = handler;
      this.name = name;
      handler.setSyncNettyClient(this);
      this.workerGroup = new NioEventLoopGroup();
      this.bootstrap = new Bootstrap();
      ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)this.bootstrap.group(this.workerGroup)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())).option(ChannelOption.SO_KEEPALIVE, true)).channel(NioSocketChannel.class);
      if (isLongConnection) {
         this.doConnect();
      }

   }

   public SyncNettyClient(InetSocketAddress address, DataType dataType, NetEvent netEvent, boolean isLongConnection, boolean heart, long writerIdleTime, String name) {
      this.writerIdleTime = writerIdleTime;
      this.heart = heart;
      this.address = address;
      this.dataType = dataType;
      this.netEvent = netEvent;
      this.name = name;
      this.isLongConnection = isLongConnection;
      this.handler = new DefaultSyncClientHandler(this);
      this.workerGroup = new NioEventLoopGroup();
      this.bootstrap = new Bootstrap();
      ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)this.bootstrap.group(this.workerGroup)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())).option(ChannelOption.SO_KEEPALIVE, true)).channel(NioSocketChannel.class);
      if (isLongConnection) {
         this.doConnect();
      }

   }

   public void doConnect() {
      try {
         if (null == this.bootstrap) {
            logger.debug("[Netty Client] null == bootstrap");
            return;
         }

         InitializerConfig config = null;
         if (this.heart) {
            config = InitializerConfig.builder().readerIdleTime(0L).writerIdleTime(this.writerIdleTime).allIdleTime(82800L).unit(TimeUnit.SECONDS).dataType(this.dataType).build();
         } else {
            config = InitializerConfig.builder().readerIdleTime(0L).writerIdleTime(0L).allIdleTime(82800L).unit(TimeUnit.SECONDS).dataType(this.dataType).build();
         }

         this.bootstrap.handler(new Initializer(this.handler, config, this.name));
         this.future = this.bootstrap.connect(this.address);
         if (this.isLongConnection) {
            this.future.addListener(new SyncClientListenerForConnectionLong(this.address, this));
         } else {
            this.future.addListener(new SyncClientListener(this.address, this));
         }

         logger.debug("[Netty Client] Clinet is doConnect");
      } catch (Exception var2) {
         logger.error("[Netty Client] Exception", var2);
      }

   }

   public boolean isActive() {
      return null != this.future && null != this.workerGroup ? this.future.channel().isActive() : false;
   }

   public void doCloseClient() {
      logger.debug("[Netty Client] close client");
      if (null != this.future && null != this.workerGroup) {
         this.future.channel().close();
         this.future.channel().pipeline().fireChannelInactive();
         this.workerGroup.shutdownGracefully();
      }

   }

   protected void finalize() throws Throwable {
      logger.debug("[Netty Client] close client");
      if (null != this.future && null != this.workerGroup) {
         this.future.channel().close();
         this.workerGroup.shutdownGracefully();
      }

   }
}
