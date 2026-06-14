package com.chang.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtil {
   private static final Logger log = LoggerFactory.getLogger(MessageUtil.class);

   public static byte[] getSendData(String messageType, Object o) {
      byte[] revData = null;
      if (ObjectUtil.equal(messageType, "str")) {
         if (o instanceof String) {
            revData = ((String)o).getBytes(StandardCharsets.UTF_8);
         } else {
            if (!BeanUtil.isBean(o.getClass())) {
               log.error("messageType and send Type Err");
               throw new RuntimeException("messageType and send Type Err");
            }

            revData = CommUtils.getJSONStringFromObject(o).getBytes(StandardCharsets.UTF_8);
         }
      } else if (ObjectUtil.equal(messageType, "serialize")) {
         if (!BeanUtil.isBean(o.getClass())) {
            log.error("messageType and send Type Err");
            throw new RuntimeException("messageType and send Type Err");
         }

         revData = CommUtils.serialize(o);
      } else {
         if (!ObjectUtil.equal(messageType, "gzip-compress")) {
            throw new RuntimeException("messageType is err");
         }

         if (o instanceof String) {
            try {
               revData = (byte[]) CommUtils.gzipCompress((String)o).get();
            } catch (Exception var5) {
               log.error("gzipCompress Err", var5);
               throw new RuntimeException("gzipCompress", var5);
            }
         } else {
            if (!(o instanceof byte[])) {
               log.error("messageType and send Type Err");
               throw new RuntimeException("messageType and send Type Err");
            }

            try {
               revData = (byte[]) CommUtils.gzipCompress((byte[])((byte[])o)).get();
            } catch (Exception var4) {
               log.error("gzipCompress Err", var4);
               throw new RuntimeException("gzipCompress", var4);
            }
         }
      }

      if (ObjectUtil.isNull(revData)) {
         log.error("revData is null");
         throw new RuntimeException("revData is null");
      } else {
         return revData;
      }
   }

   public static Object getRevData(String messageType, byte[] body) throws Exception {
      Object revData = null;
      if (ObjectUtil.equal(messageType, "str")) {
         revData = new String(body, StandardCharsets.UTF_8);
      } else if (ObjectUtil.equal(messageType, "serialize")) {
         revData = CommUtils.deserialize(body, Object.class);
      } else {
         if (!ObjectUtil.equal(messageType, "gzip-uncompress")) {
            throw new RuntimeException("messageType is err");
         }

         revData = CommUtils.gzipUncompressToString(body).get();
      }

      if (ObjectUtil.isNull(revData)) {
         log.error("revData is null");
         throw new RuntimeException("revData is null");
      } else {
         return revData;
      }
   }
}
