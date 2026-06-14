package com.chang.util.source.tcpServer;

import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.netty.until.server.ClientInfo;
import com.chang.netty.until.server.CustomServerHandler;
import com.chang.netty.until.server.NettyServer;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.TcpData;
import com.chang.util.source.common.Util;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerOutPut implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(TcpServerOutPut.class);
   private final NettyServer nettyServer;
   private String keyName;
   private final Map<String, String> groupAndModeMap = new HashMap();
   private List<SendMode> groupNamesMode;
   private String messageType;
   private Map<String, Object> parm;

   public TcpServerOutPut(String keyName, List<SendMode> groupNamesMode, String hostname, int port, String messageType, Map<String, Object> parm) {
      this.keyName = keyName;
      this.groupNamesMode = groupNamesMode;
      this.messageType = messageType;
      this.parm = parm;
      InitializerConfig config = InitializerConfig.builder().dataType(DataType.BYTEARRAY).readerIdleTime(60L).writerIdleTime(60L).allIdleTime(120L).unit(TimeUnit.SECONDS).build();
      InetSocketAddress address = new InetSocketAddress(hostname, port);
      CustomServerHandler handler = new CustomServerHandler() {
         protected void handleData(ClientInfo clientInfo, Object msg) throws Exception {
         }
      };
      List<String> groupNames = (List)groupNamesMode.stream().map(SendMode::getGroupName).collect(Collectors.toList());
      groupNamesMode.stream().forEach((x) -> {
         this.groupAndModeMap.put(x.getGroupName(), x.getMode());
      });
      this.nettyServer = new NettyServer(keyName, groupNames, address, handler, config, true);
      this.nettyServer.start();
   }

   public void Output(Object o) {
      if (o instanceof TcpData) {
         TcpData tcpData = (TcpData)o;
         String groupName = tcpData.getGroupName();
         if (StringUtils.isNotBlank(groupName)) {
            String modeName = (String)this.groupAndModeMap.get(groupName);
            if (StringUtils.isNotBlank(modeName)) {
               switch (modeName) {
                  case "GROUP_BROADCAST":
                     this.nettyServer.broadCastMessage(groupName, Util.getSendData(this.messageType, tcpData.getData()));
                     break;
                  case "BROADCAST":
                     this.nettyServer.broadCastMessage(Util.getSendData(this.messageType, tcpData.getData()));
                     break;
                  case "GROUP_BROADCAST_ROUND":
                     this.nettyServer.broadCastRoundMessage(Util.getSendData(this.messageType, tcpData.getData()));
                     break;
                  case "CLIENT":
                     String client = tcpData.getClient();
                     if (StringUtils.isNotBlank(client)) {
                        this.nettyServer.sendClientMessage(client, Util.getSendData(this.messageType, tcpData.getData()));
                     }
                     break;
                  case "GROUP_CLIENT_ROUND":
                     this.nettyServer.sendRoundClientMessage(groupName, Util.getSendData(this.messageType, tcpData.getData()));
                     break;
                  default:
                     throw new RuntimeException("modeName is ERR");
               }
            }
         }

      } else {
         throw new RuntimeException("Output parm is not type TcpData");
      }
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      this.nettyServer.close();
   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
