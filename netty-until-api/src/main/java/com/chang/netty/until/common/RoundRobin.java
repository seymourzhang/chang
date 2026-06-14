package com.chang.netty.until.common;

import cn.hutool.core.util.ObjectUtil;
import com.chang.netty.until.server.ChannelGroups;
import io.netty.channel.ChannelId;
import java.util.List;

public class RoundRobin {
   private Integer index = 0;
   private String groupName;

   public RoundRobin(String groupName) {
      this.groupName = groupName;
   }

   public ChannelId roundRobin() {
      List<ChannelId> channelList = ChannelGroups.getChannelList(this.groupName);
      if (ObjectUtil.isNotEmpty(channelList)) {
         synchronized(this.index) {
            if (this.index >= channelList.size()) {
               this.index = 0;
            }

            ChannelId channelId = (ChannelId)channelList.get(this.index);
            Integer var4 = this.index;
            Integer var5 = this.index = this.index + 1;
            return channelId;
         }
      } else {
         return null;
      }
   }
}
