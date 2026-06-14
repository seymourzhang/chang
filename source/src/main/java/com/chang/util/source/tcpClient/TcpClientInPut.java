package com.chang.util.source.tcpClient;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.netty.until.client.CustomClientHandler;
import com.chang.netty.until.client.NettyClient;
import com.chang.netty.until.client.ServerInfo;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientInPut implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(TcpClientInPut.class);
   InitializerConfig config;
   private String hostname;
   private int port;
   private NettyClient nettyClient;
   private InetSocketAddress address;
   private String keyName;
   private String groupName;
   String messageType;
   private Map<String, Object> parm;
   private final ExecutorService executorService;

   public TcpClientInPut(String keyName, String hostname, int port, String groupName, String messageType, Map<String, Object> parm, Integer nThreads, Integer maximumQueueSize) {
      this.keyName = keyName;
      this.hostname = hostname;
      this.port = port;
      this.groupName = groupName;
      this.messageType = messageType;
      this.address = new InetSocketAddress(hostname, port);
      this.parm = parm;
      this.executorService = ThreadUtil.newFixedExecutor(nThreads, maximumQueueSize, "TcpClientInPut", true);
      this.config = InitializerConfig.builder().dataType(DataType.BYTEARRAY).readerIdleTime(60L).writerIdleTime(60L).allIdleTime(120L).unit(TimeUnit.SECONDS).build();
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) {
      CustomClientHandler customClientHandler = new CustomClientHandler() {
         protected void handleData(ServerInfo serverInfo, Object msg) throws Exception {
            TcpClientInPut.this.executorService.execute(() -> {
               try {
                  SourceContext.setExParm(TcpClientInPut.this.parm);
                  Object revData = Util.getRevData(TcpClientInPut.this.messageType, (byte[])((byte[])msg));
                  if (ObjectUtil.isNotNull(function)) {
                     source.Output(function.apply(revData));
                  } else {
                     source.Output(revData);
                  }

                  SourceContext.clearExParm();
               } catch (Exception var5) {
                  TcpClientInPut.log.error("[TcpClientInPut] mArrived err:", var5);
               }

            });
         }

         protected void handleInactive(ServerInfo serverInfo) throws Exception {
            TcpClientInPut.log.info("Inactive: " + serverInfo.getServerAddr());
         }

         protected void handleActive(ServerInfo serverInfo) throws Exception {
            TcpClientInPut.log.info("Active: " + serverInfo.getServerAddr());
         }
      };
      this.nettyClient = new NettyClient(this.keyName, this.address, customClientHandler, this.config, true, this.groupName);
      this.nettyClient.doConnect();
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
