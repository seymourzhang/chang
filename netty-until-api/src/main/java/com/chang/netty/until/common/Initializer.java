package com.chang.netty.until.common;

import com.chang.netty.until.common.InitializerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class Initializer extends ChannelInitializer<SocketChannel> {
   public static final String CLIENT_HEART_BEAT = "clientHello";
   public static final String SERVER_HEART_BEAT = "serverHello";
   public static final String TERMINATOR = "$_$";
   private InitializerConfig config = null;
   private SimpleChannelInboundHandler ciha;
   private String name;

   public Initializer(SimpleChannelInboundHandler ciha, InitializerConfig config, String name) {
      this.ciha = ciha;
      this.config = config;
      this.name = name;
   }

   protected void initChannel(SocketChannel socketChannel) {
      DataType dataType = this.config.getDataType();
      ChannelPipeline pipeline = socketChannel.pipeline();
      if (dataType == DataType.BYTEARRAY) {
         pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
         pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
      } else {
         ByteBuf delimiter = Unpooled.copiedBuffer("$_$".getBytes());
         pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(204800, delimiter));
         pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
         pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
      }

      pipeline.addLast(new ChannelHandler[]{new IdleStateHandler(this.config.getReaderIdleTime(), this.config.getWriterIdleTime(), this.config.getAllIdleTime(), this.config.getUnit())});
      pipeline.addLast(this.name, this.ciha);
   }
}
