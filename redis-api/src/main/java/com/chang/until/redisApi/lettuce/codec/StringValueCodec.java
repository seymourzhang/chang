package com.chang.until.redisApi.lettuce.codec;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.codec.ToByteBufEncoder;
import io.lettuce.core.internal.LettuceAssert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class StringValueCodec implements RedisCodec<String, byte[]>, ToByteBufEncoder<String, byte[]> {
   public static final StringCodec UTF8;
   public static final StringCodec ASCII;
   private static final byte[] EMPTY;
   private final Charset charset;
   private final float averageBytesPerChar;
   private final float maxBytesPerChar;
   private final boolean ascii;
   private final boolean utf8;

   public StringValueCodec() {
      this(Charset.defaultCharset());
   }

   public StringValueCodec(Charset charset) {
      LettuceAssert.notNull(charset, "Charset must not be null");
      this.charset = charset;
      CharsetEncoder encoder = CharsetUtil.encoder(charset);
      this.averageBytesPerChar = encoder.averageBytesPerChar();
      this.maxBytesPerChar = encoder.maxBytesPerChar();
      if (charset.name().equals("UTF-8")) {
         this.utf8 = true;
         this.ascii = false;
      } else if (charset.name().contains("ASCII")) {
         this.utf8 = false;
         this.ascii = true;
      } else {
         this.ascii = false;
         this.utf8 = false;
      }

   }

   public String decodeKey(ByteBuffer bytes) {
      return Unpooled.wrappedBuffer(bytes).toString(this.charset);
   }

   public byte[] decodeValue(ByteBuffer bytes) {
      return getBytes(bytes);
   }

   public ByteBuffer encodeKey(String key) {
      return this.encodeAndAllocateBuffer(key);
   }

   public ByteBuffer encodeValue(byte[] value) {
      return value == null ? ByteBuffer.wrap(EMPTY) : ByteBuffer.wrap(value);
   }

   public void encodeKey(String key, ByteBuf target) {
      this.encode(key, target);
   }

   public void encodeValue(byte[] value, ByteBuf target) {
      if (value != null) {
         target.writeBytes(value);
      }

   }

   public int estimateSize(Object keyOrValue) {
      return keyOrValue instanceof String ? this.sizeOf((String)keyOrValue, true) : 0;
   }

   private static byte[] getBytes(ByteBuffer buffer) {
      int remaining = buffer.remaining();
      if (remaining == 0) {
         return EMPTY;
      } else {
         byte[] b = new byte[remaining];
         buffer.get(b);
         return b;
      }
   }

   public void encode(String str, ByteBuf target) {
      if (str != null) {
         if (this.utf8) {
            ByteBufUtil.writeUtf8(target, str);
         } else if (this.ascii) {
            ByteBufUtil.writeAscii(target, str);
         } else {
            CharsetEncoder encoder = CharsetUtil.encoder(this.charset);
            int length = this.sizeOf(str, false);
            target.ensureWritable(length);

            try {
               ByteBuffer dstBuf = target.nioBuffer(0, length);
               int pos = dstBuf.position();
               CoderResult cr = encoder.encode(CharBuffer.wrap(str), dstBuf, true);
               if (!cr.isUnderflow()) {
                  cr.throwException();
               }

               cr = encoder.flush(dstBuf);
               if (!cr.isUnderflow()) {
                  cr.throwException();
               }

               target.writerIndex(target.writerIndex() + dstBuf.position() - pos);
            } catch (CharacterCodingException var8) {
               throw new IllegalStateException(var8);
            }
         }
      }
   }

   private int sizeOf(String value, boolean estimate) {
      if (this.utf8) {
         return ByteBufUtil.utf8MaxBytes(value);
      } else if (this.ascii) {
         return value.length();
      } else {
         return estimate ? (int)this.averageBytesPerChar * value.length() : (int)this.maxBytesPerChar * value.length();
      }
   }

   private ByteBuffer encodeAndAllocateBuffer(String key) {
      if (key == null) {
         return ByteBuffer.wrap(EMPTY);
      } else {
         ByteBuffer buffer = ByteBuffer.allocate(this.sizeOf(key, false));
         ByteBuf byteBuf = Unpooled.wrappedBuffer(buffer);
         byteBuf.clear();
         this.encode(key, byteBuf);
         buffer.limit(byteBuf.writerIndex());
         return buffer;
      }
   }

   static {
      UTF8 = new StringCodec(StandardCharsets.UTF_8);
      ASCII = new StringCodec(StandardCharsets.US_ASCII);
      EMPTY = new byte[0];
   }
}
