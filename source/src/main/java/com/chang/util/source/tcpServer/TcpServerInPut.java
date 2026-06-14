package com.chang.util.source.tcpServer;

import cn.hutool.core.util.ObjectUtil;
import com.chang.netty.until.common.DataType;
import com.chang.netty.until.common.InitializerConfig;
import com.chang.netty.until.server.ClientInfo;
import com.chang.netty.until.server.CustomServerHandler;
import com.chang.netty.until.server.NettyServer;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerInPut implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(TcpServerInPut.class);
   private NettyServer nettyServer;
   private String keyName;
   private String hostname;
   private int port;
   private InitializerConfig config;
   private InetSocketAddress address;
   private List<SendMode> groupNamesMode;
   private String messageType;
   private Map<String, Object> parm;

   public TcpServerInPut(String keyName, List<SendMode> groupNamesMode, String hostname, int port, String messageType, Map<String, Object> parm) {
      this.keyName = keyName;
      this.hostname = hostname;
      this.port = port;
      this.groupNamesMode = groupNamesMode;
      this.messageType = messageType;
      this.parm = parm;
      this.config = InitializerConfig.builder().dataType(DataType.BYTEARRAY).readerIdleTime(60L).writerIdleTime(60L).allIdleTime(120L).unit(TimeUnit.SECONDS).build();
      this.address = new InetSocketAddress(hostname, port);
   }

   public void InPut(final OutputSource source, final Function<Object, Object> function) {
      CustomServerHandler handler = new CustomServerHandler() {
         protected void handleData(ClientInfo clientInfo, Object msg) throws Exception {
            SourceContext.setExParm(TcpServerInPut.this.parm);
            Object revData = Util.getRevData(TcpServerInPut.this.messageType, (byte[])((byte[])msg));
            if (ObjectUtil.isNotNull(function)) {
               source.Output(function.apply(revData));
            } else {
               source.Output(revData);
            }

            SourceContext.clearExParm();
         }
      };
      List<String> groupNames = (List)this.groupNamesMode.stream().map((x) -> {
         return x.getGroupName();
      }).collect(Collectors.toList());
      this.nettyServer = new NettyServer(this.keyName, groupNames, this.address, handler, this.config, true);
      this.nettyServer.start();
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
