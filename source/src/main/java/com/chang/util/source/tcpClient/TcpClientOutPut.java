package com.chang.util.source.tcpClient;

import com.chang.netty.until.client.CustomClientHandler;
import com.chang.netty.until.client.NettyClient;
import com.chang.netty.until.client.ServerInfo;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.Util;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientOutPut implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(TcpClientOutPut.class);
   private final NettyClient nettyClient;
   private String keyName;
   private String groupName;
   private String messageType;
   private Map<String, Object> parm;

   public TcpClientOutPut(String keyName, String hostname, int port, String groupName, String messageType, Map<String, Object> parm) {
      this.keyName = keyName;
      this.groupName = groupName;
      this.messageType = messageType;
      this.parm = parm;
      InetSocketAddress address = new InetSocketAddress(hostname, port);
      InitializerConfig config = InitializerConfig.builder().dataType(DataType.BYTEARRAY).readerIdleTime(60L).writerIdleTime(60L).allIdleTime(120L).unit(TimeUnit.SECONDS).build();
      CustomClientHandler customClientHandler = new CustomClientHandler() {
         protected void handleData(ServerInfo serverInfo, Object msg) throws Exception {
         }

         protected void handleInactive(ServerInfo serverInfo) throws Exception {
            TcpClientOutPut.log.info("Inactive: " + serverInfo.getServerAddr());
         }

         protected void handleActive(ServerInfo serverInfo) throws Exception {
            TcpClientOutPut.log.info("Active: " + serverInfo.getServerAddr());
         }
      };
      this.nettyClient = new NettyClient(keyName, address, customClientHandler, config, true, groupName);
      this.nettyClient.doConnect();
   }

   public void Output(Object o) {
      this.nettyClient.sendMessage(Util.getSendData(this.messageType, o));
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      this.nettyClient.doCloseClient();
   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
