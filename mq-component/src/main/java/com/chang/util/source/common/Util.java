package com.chang.util.source.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Entity;
import com.chang.common.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class Util {
   private static final Logger log = LoggerFactory.getLogger(Util.class);
   public static final String MONGODB_INPUT_TOTAL_KEY = "mongodb_input_total_key";
   public static final String HBASE_INPUT_TOTAL_KEY = "hbase_input_total_key";

   public static Entity toEntity(Object o, String tableName) {
      if (!BeanUtil.isBean(o.getClass())) {
         throw new RuntimeException("toEntity param o is not bean");
      } else {
         Map<String, Object> map = BeanUtil.beanToMap(o, (String[])null);
         Entity record = new Entity(tableName);
         Iterator var4 = map.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var4.next();
            record.set((String)entry.getKey(), entry.getValue());
         }

         return record;
      }
   }

   public static byte[] getSendData(String messageType, Object o) {
      byte[] revData = null;
      if (ObjectUtil.equal(messageType, "byte")) {
         if (!(o instanceof byte[])) {
            log.error("messageType and send Type Err");
            throw new RuntimeException("messageType and send Type Err");
         }

         revData = (byte[])((byte[])o);
      } else if (ObjectUtil.equal(messageType, "str")) {
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
      } else if (ObjectUtil.equal(messageType, "gzip-compress")) {
         if (o instanceof String) {
            try {
               revData = (byte[])CommUtils.gzipCompress((String)o).get();
            } catch (Exception var7) {
               log.error("gzipCompress Err", var7);
               throw new RuntimeException("gzipCompress", var7);
            }
         } else {
            if (!(o instanceof byte[])) {
               log.error("messageType and send Type Err");
               throw new RuntimeException("messageType and send Type Err");
            }

            try {
               revData = (byte[])CommUtils.gzipCompress((byte[])((byte[])o)).get();
            } catch (Exception var6) {
               log.error("gzipCompress Err", var6);
               throw new RuntimeException("gzipCompress", var6);
            }
         }
      } else {
         if (!ObjectUtil.equal(messageType, "bzip2-compress")) {
            throw new RuntimeException("messageType is err");
         }

         if (o instanceof String) {
            try {
               revData = CommUtils.BZip(((String)o).getBytes());
            } catch (Exception var5) {
               log.error("gzipCompress Err", var5);
               throw new RuntimeException("bzip2Compress", var5);
            }
         } else {
            if (!(o instanceof byte[])) {
               log.error("messageType and send Type Err");
               throw new RuntimeException("messageType and send Type Err");
            }

            try {
               revData = CommUtils.BZip((byte[])((byte[])o));
            } catch (Exception var4) {
               log.error("gzipCompress Err", var4);
               throw new RuntimeException("bzip2Compress", var4);
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
      Object revData;
      if (ObjectUtil.equal(messageType, "str")) {
         revData = new String(body, StandardCharsets.UTF_8);
      } else if (ObjectUtil.equal(messageType, "serialize")) {
         revData = CommUtils.deserialize(body, Object.class);
      } else if (ObjectUtil.equal(messageType, "gzip-uncompress-str")) {
         revData = CommUtils.gzipUncompressToString(body).get();
      } else if (ObjectUtil.equal(messageType, "bzip2-uncompress-str")) {
         revData = new String(CommUtils.UnBZip(body), StandardCharsets.UTF_8);
      } else if (ObjectUtil.equal(messageType, "gzip-uncompress")) {
         revData = CommUtils.gzipCompress(body).get();
      } else if (ObjectUtil.equal(messageType, "bzip2-uncompress")) {
         revData = CommUtils.UnBZip(body);
      } else {
         if (!ObjectUtil.equal(messageType, "byte")) {
            throw new RuntimeException("messageType is err");
         }

         revData = body;
      }

      if (ObjectUtil.isNull(revData)) {
         log.error("revData is null");
         throw new RuntimeException("revData is null");
      } else {
         return revData;
      }
   }
}
