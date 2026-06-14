package com.chang.netty.until.server;

import com.chang.netty.until.server.ServerSendListener;
import com.chang.netty.until.server.ServerSendListenerGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelGroups {
   private static final Logger log = LoggerFactory.getLogger(ChannelGroups.class);
   private static final Map<String, ChannelGroup> CHANNEL_GROUP = new ConcurrentHashMap();
   private static final Map<String, List<String>> CLIENT_GROUP = new ConcurrentHashMap();

   private ChannelGroups(String name) {
   }

   public static void addGroup(String groupName) {
      ChannelGroup group = new DefaultChannelGroup(groupName, GlobalEventExecutor.INSTANCE);
      CHANNEL_GROUP.put(groupName, group);
      CLIENT_GROUP.put(groupName, new CopyOnWriteArrayList());
   }

   public static void addClient(String groupName, String clientName) {
      ((List)CLIENT_GROUP.get(groupName)).add(clientName);
   }

   public static void removeClient(String groupName, String clientName) {
      ((List)CLIENT_GROUP.get(groupName)).remove(clientName);
   }

   public static List<String> getClient(String groupName) {
      return (List)CLIENT_GROUP.get(groupName);
   }

   public static void addChannel(String groupName, Channel channel) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      cg.add(channel);
   }

   public static ChannelGroup getChannelGroup(String groupName) {
      return (ChannelGroup)CHANNEL_GROUP.get(groupName);
   }

   public static ChannelGroupFuture broadcast(String groupName, Object msg) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.isEmpty() ? null : cg.writeAndFlush(msg).addListener(new ServerSendListenerGroup(groupName));
   }

   public static List<ChannelId> getChannelList(String groupName) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return null;
   }

   public static ChannelGroupFuture broadcast(String groupName, Object msg, ChannelMatcher matcher) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.isEmpty() ? null : cg.writeAndFlush(msg, matcher).addListener(new ServerSendListenerGroup(groupName));
   }

   public static ChannelFuture send(String groupName, ChannelId id, Object msg) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.isEmpty() ? null : cg.find(id).writeAndFlush(msg).addListener(new ServerSendListener(groupName));
   }

   public static ChannelGroup flush(String groupName) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.flush();
   }

   public static boolean discard(String groupName, Channel channel) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.remove(channel);
   }

   public static ChannelGroupFuture disconnect(String groupName) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      CHANNEL_GROUP.remove(groupName);
      return cg.disconnect();
   }

   public static ChannelGroupFuture disconnect(String groupName, ChannelMatcher matcher) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      CHANNEL_GROUP.remove(groupName);
      return cg.disconnect(matcher);
   }

   public static boolean contains(String groupName, Channel channel) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.contains(channel);
   }

   public static boolean contains(String groupName) {
      return CHANNEL_GROUP.containsKey(groupName);
   }

   public static int size(String groupName) {
      ChannelGroup cg = (ChannelGroup)CHANNEL_GROUP.get(groupName);
      return cg.size();
   }
}
