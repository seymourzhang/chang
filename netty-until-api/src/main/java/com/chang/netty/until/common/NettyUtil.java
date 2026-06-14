package com.chang.netty.until.common;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.CommUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.CharsetUtil;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyUtil {
   private static final Logger log = LoggerFactory.getLogger(NettyUtil.class);
   private static Integer index = 0;

   public static void sendHeartBeat(Channel ctx, DataType dataType, String name, String type, String groupName) {
      HeartBeat heartBeat = new HeartBeat();
      heartBeat.setName(name);
      heartBeat.setAddr(CommUtils.getLocalHostExactAddress().toString());
      heartBeat.setType(type);
      heartBeat.setGroupName(groupName);
      String str_heartBeat = JSON.toJSONString(heartBeat);
      if (dataType == DataType.STRING) {
         str_heartBeat = str_heartBeat + "$_$";
         ctx.writeAndFlush(str_heartBeat);
      } else {
         ctx.writeAndFlush(str_heartBeat.getBytes(CharsetUtil.UTF_8));
      }

   }

   public static HeartBeat findHeartBeat(Object msg, DataType dataType) {
      try {
         JSONObject beat = null;
         String sMsg;
         if (dataType == DataType.STRING) {
            sMsg = (String)msg;
         } else {
            sMsg = new String((byte[])((byte[])msg), StandardCharsets.UTF_8);
         }

         if (CommUtils.isJson(sMsg)) {
            beat = JSON.parseObject(sMsg);
         }

         if (ObjectUtil.isNotNull(beat) && beat.containsKey("messageType")) {
            Object messageType = beat.get("messageType");
            if (ObjectUtil.equal(messageType, "HEARTBEAT")) {
               return (HeartBeat)CommUtils.getObjectFromJSONString(beat.toJSONString(), HeartBeat.class);
            }
         }

         return null;
      } catch (Throwable var5) {
         log.error("findHeartBeat Err: ", var5);
         return null;
      }
   }

   public static ChannelId roundRobin(List<ChannelId> channelList) {
      synchronized(index) {
         if (index >= channelList.size()) {
            index = 0;
         }

         ChannelId channelId = (ChannelId)channelList.get(index);
         Integer var3 = index;
         index = index + 1;
         return channelId;
      }
   }
}
