package com.chang.netty.until.server;

import cn.hutool.core.util.ObjectUtil;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.HeartBeat;
import com.chang.netty.until.common.Initializer;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.netty.until.common.ManageServerChannels;
import com.chang.netty.until.common.ManageServerRoundRobin;
import com.chang.netty.until.common.RoundRobin;
import com.chang.netty.until.server.ChannelGroups;
import com.chang.netty.until.server.CustomServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer implements Runnable {
   private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
   private static final ExecutorService es = Executors.newSingleThreadExecutor();
   private final InetSocketAddress address;
   private final CustomServerHandler handler;
   private String serverName;
   private final InitializerConfig config;
   private Channel channel;
   List<String> groupNames;
   private final boolean isSendHeartBeat;

   public NettyServer(String serverName, List<String> groupNames, InetSocketAddress address, CustomServerHandler handler, InitializerConfig config, boolean isSendHeartBeat) {
      this.config = config;
      this.address = address;
      this.handler = handler;
      this.isSendHeartBeat = isSendHeartBeat;
      this.serverName = serverName;
      this.groupNames = groupNames;
      Iterator var7 = groupNames.iterator();

      while(var7.hasNext()) {
         String groupName = (String)var7.next();
         ChannelGroups.addGroup(groupName);
         ManageServerRoundRobin.add(groupName, new RoundRobin(groupName));
      }

      handler.setNettyServer(this);
   }

   public boolean isSendHeartBeat() {
      return this.isSendHeartBeat;
   }

   public Future<?> start() {
      return es.submit(this);
   }

   public void run() {
      EventLoopGroup bossGroup = new NioEventLoopGroup();
      EventLoopGroup workerGroup = new NioEventLoopGroup();

      try {
         ServerBootstrap bootstrap = new ServerBootstrap();
         ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)).localAddress(this.address)).option(ChannelOption.SO_BACKLOG, 128)).option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())).childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new Initializer(this.handler, this.config, this.serverName));
         ChannelFuture future = bootstrap.bind(this.address).sync();
         if (future.isDone()) {
            log.info(String.format("[Netty Server] server bind port %s sucess", this.address));
         }

         log.info("[Netty Server] Server start listen at " + this.address.getPort());
         this.channel = future.channel();
         this.channel.closeFuture().sync();
      } catch (Exception var8) {
         log.error("[Netty Server] Server start err", var8);
      } finally {
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
      }

   }

   public void broadCastMessage(String groupName, Object content) {
      if (ObjectUtil.equal(this.config.getDataType(), DataType.STRING)) {
         ChannelGroups.broadcast(groupName, content + "$_$");
      } else {
         ChannelGroups.broadcast(groupName, content);
      }

   }

   public void broadCastMessage(Object content) {
      Iterator var2 = this.groupNames.iterator();

      while(var2.hasNext()) {
         String groupName = (String)var2.next();
         this.broadCastMessage(groupName, content);
      }

   }

   public void broadCastRoundMessage(Object content) {
      Iterator var2 = this.groupNames.iterator();

      while(var2.hasNext()) {
         String groupName = (String)var2.next();
         this.sendRoundClientMessage(groupName, content);
      }

   }

   public void sendClientMessage(String clientName, Object content) {
      if (ManageServerChannels.hasClient(clientName)) {
         HeartBeat heartBeat = ManageServerChannels.getHeartBeatByName(clientName);
         if (ObjectUtil.isNull(heartBeat)) {
            return;
         }

         if (ObjectUtil.equal(this.config.getDataType(), DataType.STRING)) {
            ChannelGroups.send(heartBeat.getGroupName(), ManageServerChannels.getChannelId(clientName), content + "$_$");
         } else {
            ChannelGroups.send(heartBeat.getGroupName(), ManageServerChannels.getChannelId(clientName), content);
         }
      }

   }

   public void sendRoundClientMessage(String groupName, Object content) {
      ChannelId channelId = ManageServerRoundRobin.get(groupName).roundRobin();
      if (ObjectUtil.isNotNull(channelId)) {
         if (ObjectUtil.equal(this.config.getDataType(), DataType.STRING)) {
            ChannelGroups.send(groupName, channelId, content + "$_$");
         } else {
            ChannelGroups.send(groupName, channelId, content);
         }
      }

   }

   public List<String> getClient(String groupName) {
      return ChannelGroups.getClient(groupName);
   }

   public String getGroupName(String clientName) {
      HeartBeat heartBeatByName = ManageServerChannels.getHeartBeatByName(clientName);
      return ObjectUtil.isNotNull(heartBeatByName) ? heartBeatByName.getGroupName() : null;
   }

   public void close() {
      if (this.channel != null) {
         this.channel.close();
      }

   }

   public String getServerName() {
      return this.serverName;
   }

   public void setServerName(String serverName) {
      this.serverName = serverName;
   }

   public InitializerConfig getConfig() {
      return this.config;
   }
}
