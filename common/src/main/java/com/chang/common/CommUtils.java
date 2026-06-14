package com.chang.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.extra.compress.CompressUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.chang.common.util.ObjectSizeCalculator;
import com.google.common.base.Preconditions;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StopWatch;
import org.xerial.snappy.Snappy;

public class CommUtils {
   private static Pattern linePattern = Pattern.compile("_(\\w)");
   private static Pattern humpPattern = Pattern.compile("[A-Z]");
   private static final MessagePack msgPack2 = new MessagePack();
   private static final SnowflakeShardingKeyGenerator snowflakeShardingKeyGenerator = new SnowflakeShardingKeyGenerator();

   public static String getEventId(Long id) {
      StringBuffer buffer = new StringBuffer();
      String sid = String.valueOf(id);
      int len = sid.length();

      for(int i = 0; i < 8 - len; ++i) {
         buffer.append("0");
      }

      buffer.append(sid);
      return buffer.toString();
   }

   public static String getAppId(Long id) {
      StringBuffer buffer = new StringBuffer();
      String sid = String.valueOf(id);
      int len = sid.length();

      for(int i = 0; i < 5 - len; ++i) {
         buffer.append("0");
      }

      buffer.append(sid);
      return "100" + buffer.toString();
   }

   public static JSONArray getJSONA(List<Object[]> data, String[] com, boolean isContainsNull) {
      JSONArray jsa = new JSONArray();
      Iterator<Object[]> it = data.iterator();
      int i = 1;

      while(true) {
         JSONObject jo;
         Object m;
         Object[] oo;
         do {
            if (!it.hasNext()) {
               return jsa;
            }

            jo = new JSONObject();
            m = it.next();
            oo = null;
            String so = null;
            if (m instanceof String) {
               so = (String)m;
               jo.put(com[0], so);
               jsa.add(jo);
            }
         } while(!(m instanceof Object[]));

         oo = (Object[])((Object[])m);
         i = 0;
         Object[] var10 = oo;
         int var11 = oo.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            Object o = var10[var12];
            if (null == o) {
               if (isContainsNull) {
                  jo.put(com[i], oo[i]);
               }

               ++i;
            } else {
               jo.put(com[i], oo[i]);
               ++i;
            }
         }

         jsa.add(jo);
      }
   }

   public static JSONObject getJson(Object[] data, String[] keys, boolean isContainsNull) {
      JSONObject json = new JSONObject();
      int i = 0;
      Object[] var5 = data;
      int var6 = data.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Object o = var5[var7];
         if (null == o && isContainsNull) {
            json.put(keys[i], o);
            ++i;
         } else {
            json.put(keys[i], o);
            ++i;
         }
      }

      return json;
   }

   public static void setRtnInfo(JSONObject json, String rtnCode, String rtnMsg, Object data) {
      json.put("rtn_code", rtnCode);
      json.put("rtn_msg", rtnMsg);
      if (null != data) {
         if (data instanceof List) {
            json.put("list", data);
         } else {
            json.put("data", data);
         }
      }

   }

   public static Boolean permissionUrl(String servletPath, String regEx) {
      return Pattern.matches(regEx, servletPath);
   }

   public static JSONObject getPageBean(long totalElements, int size, int page, int totalPages) {
      JSONObject re = new JSONObject();
      re.put("total", totalElements);
      re.put("size", size);
      re.put("page", page);
      re.put("totalPage", totalPages);
      return re;
   }

   public static String decodeParam(String urlParam) throws UnsupportedEncodingException {
      String rtnStr = "";
      if (org.apache.commons.lang3.StringUtils.isBlank(urlParam)) {
         return rtnStr;
      } else if ("?".equals(urlParam)) {
         return rtnStr;
      } else {
         String[] arr = urlParam.split("&");

         for(int i = 0; i < arr.length; ++i) {
            String[] keyValArr = arr[i].split("=");
            String valDecode = URLEncoder.encode(keyValArr[1], "utf-8");
            rtnStr = rtnStr + keyValArr[0] + "=" + valDecode + "&";
         }

         return rtnStr;
      }
   }

   public static <T extends Serializable> T deepClone(T object) {
      return SerializationUtils.clone(object);
   }

   public static void shadowClone(Object source, Object target) {
      BeanUtils.copyProperties(source, target);
   }

   public static JSONObject getJSONObjectFromObject(Object o) {
      Preconditions.checkNotNull(o);
      return JSONObject.parseObject(JSON.toJSONString(o, new SerializerFeature[]{SerializerFeature.WriteMapNullValue}));
   }

   public static JSONObject getJSONObjectFromObject(Map<String, Object> o) {
      Preconditions.checkNotNull(o);
      return new JSONObject(o);
   }

   public static JSONArray getJSONArrayFromObject(Object o) {
      Preconditions.checkNotNull(o);
      return JSONArray.parseArray(JSON.toJSONString(o, new SerializerFeature[]{SerializerFeature.WriteMapNullValue}));
   }

   public static JSONObject getJSONObjectFromObject(Object o, String... fields) {
      Preconditions.checkNotNull(o);
      SimplePropertyPreFilter filter = new SimplePropertyPreFilter(o.getClass(), new String[0]);
      String[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String field = var3[var5];
         filter.getExcludes().add(field);
      }

      return JSONObject.parseObject(JSON.toJSONString(o, filter, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty}));
   }

   public static String getJSONStringFromObject(Object o) {
      Preconditions.checkNotNull(o);
      return JSON.toJSONString(o, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty});
   }

   public static <T> T getObjectFromJSONString(String str, Class<T> clazz) {
      return JSON.parseObject(str, clazz, new Feature[]{Feature.SupportAutoType, Feature.SupportArrayToBean});
   }

   public static String getJSONArrayStringFromObject(Object o) {
      Preconditions.checkNotNull(o);
      return JSONArray.toJSONString(o, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty});
   }

   public static <T> List<T> getListFromJSONArrayString(String str, Class<T> clazz) {
      return JSONArray.parseArray(str, clazz);
   }

   public static <T> JSONObject changeJsonClass(JSONObject o, Class<T> clazz) {
      Preconditions.checkNotNull(o);
      o.put("@type", clazz.getName());
      return o;
   }

   public static String getJSONStringFromObject(Object o, String... fields) {
      Preconditions.checkNotNull(o);
      SimplePropertyPreFilter filter = new SimplePropertyPreFilter(o.getClass(), new String[0]);
      String[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String field = var3[var5];
         filter.getExcludes().add(field);
      }

      return JSON.toJSONString(o, filter, new SerializerFeature[]{SerializerFeature.WriteMapNullValue});
   }

   public static boolean isJson(String data) {
      try {
         if (!ObjectUtil.isNull(data) && !org.apache.commons.lang3.StringUtils.isBlank(data)) {
            JSONObject.parseObject(data);
            return true;
         } else {
            return false;
         }
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isJSONArray(String data) {
      try {
         if (!ObjectUtil.isNull(data) && !org.apache.commons.lang3.StringUtils.isBlank(data)) {
            JSONArray.parseArray(data);
            return true;
         } else {
            return false;
         }
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isObjectFromJson(String data, Class clazz) {
      try {
         if (!ObjectUtil.isNull(data) && !org.apache.commons.lang3.StringUtils.isBlank(data)) {
            JSON.parseObject(data, clazz);
            return true;
         } else {
            return false;
         }
      } catch (Exception var3) {
         return false;
      }
   }

   public static long getNowMilli() {
      return Instant.now().toEpochMilli();
   }

   public static String pathVarUrl(String pattern, Object... arguments) {
      List<Cursor> index = new ArrayList();
      char[] sata = pattern.toCharArray();
      int start = 0;
      int end = 0;

      for(int i = 0; i < sata.length; ++i) {
         if (sata[i] == '{') {
            start = i;
         }

         if (sata[i] == '}') {
            end = i;
         }

         if (end > start) {
            index.add(Cursor.builder().start(start + 1).end(end).build());
            start = 0;
            end = 0;
         }
      }

      List<String> rps = new ArrayList();

      for(int j = 0; j < index.size(); ++j) {
         Integer s = ((Cursor)index.get(j)).getStart();
         Integer e = ((Cursor)index.get(j)).getEnd();
         String d = pattern.substring(s, e);
         rps.add(d);
      }

      String temp = pattern;

      for(int x = 0; x < rps.size(); ++x) {
         temp = temp.replace((CharSequence)rps.get(x), x + "");
      }

      return MessageFormat.format(temp, arguments);
   }

   public static int getInt(byte[] data) throws IOException {
      ByteArrayInputStream bain = new ByteArrayInputStream(data);
      DataInputStream dis = new DataInputStream(bain);
      return dis.readInt();
   }

   public static double getDouble(byte[] data) throws IOException {
      ByteArrayInputStream bain = new ByteArrayInputStream(data);
      DataInputStream dis = new DataInputStream(bain);
      return dis.readDouble();
   }

   public static long getLong(byte[] data) throws IOException {
      ByteArrayInputStream bain = new ByteArrayInputStream(data);
      DataInputStream dis = new DataInputStream(bain);
      return dis.readLong();
   }

   public static float getFloat(byte[] data) throws IOException {
      ByteArrayInputStream bain = new ByteArrayInputStream(data);
      DataInputStream dis = new DataInputStream(bain);
      return dis.readFloat();
   }

   public static byte[] getBytes(int data) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeInt(data);
      return baos.toByteArray();
   }

   public static byte[] getBytes(double data) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeDouble(data);
      return baos.toByteArray();
   }

   public static byte[] getBytes(long data) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeLong(data);
      return baos.toByteArray();
   }

   public static byte[] doubleArrayToByteArray(List<Double> data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.size(); ++i) {
         byte[] longToBytes = getBytes(Double.doubleToLongBits((Double)data.get(i)));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] doubleArrayToByteArray(double[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = getBytes(Double.doubleToLongBits(data[i]));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] doubleArrayToByteArray(Double[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = getBytes(Double.doubleToLongBits(data[i]));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static List<Double> byteArrayToDouble(byte[] data) throws IOException {
      List<Double> res = new ArrayList();
      if (data.length % 8 != 0) {
         throw new RuntimeException("data is ERR");
      } else {
         Queue<Byte> queue = new ArrayDeque();
         byte[] doubleByte = data;
         int forNum = data.length;

         int j;
         int i;
         for(j = 0; j < forNum; ++j) {
            i = doubleByte[j];
            queue.add(Byte.valueOf((byte)i));
         }

         doubleByte = new byte[8];
         forNum = data.length / 8;

         for(j = 0; j < forNum; ++j) {
            for(i = 0; i < 8; ++i) {
               doubleByte[i] = (Byte)queue.poll();
            }

            res.add(Double.longBitsToDouble(getLong(doubleByte)));
         }

         return res;
      }
   }

   public static byte[] floatArrayToByteArray(List<Float> data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.size(); ++i) {
         byte[] longToBytes = getBytes(Float.floatToIntBits((Float)data.get(i)));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] floatArrayToByteArray(float[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = getBytes(Float.floatToIntBits(data[i]));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] floatArrayToByteArray(Float[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = getBytes(Float.floatToIntBits(data[i]));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static List<Float> byteArrayToFloat(byte[] data) throws IOException {
      List<Float> res = new ArrayList();
      if (data.length % 4 != 0) {
         throw new RuntimeException("data is ERR");
      } else {
         Queue<Byte> queue = new ArrayDeque();
         byte[] doubleByte = data;
         int forNum = data.length;

         int j;
         int i;
         for(j = 0; j < forNum; ++j) {
            i = doubleByte[j];
            queue.add(Byte.valueOf((byte)i));
         }

         doubleByte = new byte[4];
         forNum = data.length / 4;

         for(j = 0; j < forNum; ++j) {
            for(i = 0; i < 4; ++i) {
               doubleByte[i] = (Byte)queue.poll();
            }

            res.add(Float.intBitsToFloat(getInt(doubleByte)));
         }

         return res;
      }
   }

   public static byte[] longArrayToByteArray(long[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = Bytes.toBytes(data[i]);
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] longArrayToByteArray(Long[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = Bytes.toBytes(data[i]);
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] longArrayToByteArray(List<Long> data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.size(); ++i) {
         byte[] longToBytes = Bytes.toBytes((Long)data.get(i));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static List<Long> byteArrayToLong(byte[] data) throws IOException {
      List<Long> res = new ArrayList();
      if (data.length % 8 != 0) {
         throw new RuntimeException("data is ERR");
      } else {
         Queue<Byte> queue = new ArrayDeque();
         byte[] longByte = data;
         int forNum = data.length;

         int j;
         int i;
         for(j = 0; j < forNum; ++j) {
            i = longByte[j];
            queue.add(Byte.valueOf((byte)i));
         }

         longByte = new byte[8];
         forNum = data.length / 8;

         for(j = 0; j < forNum; ++j) {
            for(i = 0; i < 8; ++i) {
               longByte[i] = (Byte)queue.poll();
            }

            res.add(Bytes.toLong(longByte));
         }

         return res;
      }
   }

   public static byte[] intArrayToByteArray(int[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = Bytes.toBytes(data[i]);
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] intArrayToByteArray(Integer[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = Bytes.toBytes(data[i]);
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] intArrayToByteArray(List<Integer> data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.size(); ++i) {
         byte[] longToBytes = Bytes.toBytes((Integer)data.get(i));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static List<Integer> byteArrayToInteger(byte[] data) throws IOException {
      List<Integer> res = new ArrayList();
      if (data.length % 4 != 0) {
         throw new RuntimeException("data is ERR");
      } else {
         Queue<Byte> queue = new ArrayDeque();
         byte[] longByte = data;
         int forNum = data.length;

         int j;
         int i;
         for(j = 0; j < forNum; ++j) {
            i = longByte[j];
            queue.add(Byte.valueOf((byte)i));
         }

         longByte = new byte[4];
         forNum = data.length / 4;

         for(j = 0; j < forNum; ++j) {
            for(i = 0; i < 4; ++i) {
               longByte[i] = (Byte)queue.poll();
            }

            res.add(Bytes.toInt(longByte));
         }

         return res;
      }
   }

   public static byte[] shortArrayToByteArray(short[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = Bytes.toBytes(data[i]);
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] shortArrayToByteArray(Short[] data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.length; ++i) {
         byte[] longToBytes = Bytes.toBytes(data[i]);
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static byte[] shortArrayToByteArray(List<Short> data) throws IOException {
      List<Byte> result = new ArrayList();

      for(int i = 0; i < data.size(); ++i) {
         byte[] longToBytes = Bytes.toBytes((Short)data.get(i));
         byte[] var4 = longToBytes;
         int var5 = longToBytes.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            result.add(b);
         }
      }

      return getListBytes(result);
   }

   public static List<Short> byteArrayToShort(byte[] data) throws IOException {
      List<Short> res = new ArrayList();
      if (data.length % 2 != 0) {
         throw new RuntimeException("data is ERR");
      } else {
         Queue<Byte> queue = new ArrayDeque();
         byte[] longByte = data;
         int forNum = data.length;

         int j;
         int i;
         for(j = 0; j < forNum; ++j) {
            i = longByte[j];
            queue.add(Byte.valueOf((byte)i));
         }

         longByte = new byte[2];
         forNum = data.length / 2;

         for(j = 0; j < forNum; ++j) {
            for(i = 0; i < 2; ++i) {
               longByte[i] = (Byte)queue.poll();
            }

            res.add(Bytes.toShort(longByte));
         }

         return res;
      }
   }

   private static byte[] getListBytes(List<Byte> result) {
      byte[] backData = new byte[result.size()];

      for(int i = 0; i < result.size(); ++i) {
         backData[i] = (Byte)result.get(i);
      }

      return backData;
   }

   public static byte[] getBytes(Byte[] result) {
      byte[] backData = new byte[result.length];

      for(int i = 0; i < result.length; ++i) {
         backData[i] = result[i];
      }

      return backData;
   }

   public static Optional<byte[]> gzipCompress(String str) throws Exception {
      if (str != null && str.length() != 0) {
         byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
         return gzipCompress(bytes);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<byte[]> gzipCompress(byte[] bytes) throws Exception {
      if (bytes.length == 0) {
         return Optional.empty();
      } else {
         GZIPOutputStream gzip = null;
         ByteArrayOutputStream out = null;

         try {
            out = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
         } finally {
            if (null != gzip) {
               gzip.close();
            }

            if (null != out) {
               out.close();
            }

         }

         byte[] bytesCompress = out.toByteArray();
         return Optional.of(bytesCompress);
      }
   }

   public static Optional<byte[]> gzipUncompress(byte[] bytes) throws Exception {
      if (bytes != null && bytes.length != 0) {
         ByteArrayOutputStream out = null;
         ByteArrayInputStream in = null;
         GZIPInputStream unGzip = null;

         try {
            byte[] buffer = new byte[256];
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(bytes);
            unGzip = new GZIPInputStream(in);

            int n;
            while((n = unGzip.read(buffer)) >= 0) {
               out.write(buffer, 0, n);
            }
         } finally {
            if (null != out) {
               out.close();
            }

            if (null != in) {
               in.close();
            }

            if (null != unGzip) {
               unGzip.close();
            }

         }

         byte[] bytesUncompress = out.toByteArray();
         return Optional.of(bytesUncompress);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<String> gzipUncompressToString(byte[] bytes) throws Exception {
      Optional<byte[]> optionalBytes = gzipUncompress(bytes);
      if (optionalBytes.isPresent()) {
         String data = new String((byte[])optionalBytes.get(), StandardCharsets.UTF_8);
         return Optional.of(data);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<byte[]> snappyCompress(String str) throws Exception {
      return str != null && str.length() != 0 ? Optional.of(Snappy.compress(str, StandardCharsets.UTF_8)) : Optional.empty();
   }

   public static Optional<byte[]> snappyCompress(byte[] bytes) throws Exception {
      return bytes != null && bytes.length != 0 ? Optional.of(Snappy.compress(bytes)) : Optional.empty();
   }

   public static Optional<byte[]> snappyUncompress(byte[] bytes) throws Exception {
      return bytes != null && bytes.length != 0 ? Optional.of(Snappy.uncompress(bytes)) : Optional.empty();
   }

   public static Optional<String> snappyUncompressToString(byte[] bytes) throws Exception {
      Optional<byte[]> optionalBytes = snappyUncompress(bytes);
      if (optionalBytes.isPresent()) {
         String data = new String((byte[])optionalBytes.get(), StandardCharsets.UTF_8);
         return Optional.of(data);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<byte[]> zipCompress(String str) throws Exception {
      if (str != null && str.length() != 0) {
         byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
         return zipCompress(bytes);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<byte[]> zipCompress(byte[] bytes) throws Exception {
      if (bytes != null && bytes.length != 0) {
         ByteArrayOutputStream out = null;
         ZipOutputStream zip = null;

         try {
            out = new ByteArrayOutputStream();
            zip = new ZipOutputStream(out);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize((long)bytes.length);
            zip.putNextEntry(entry);
            zip.write(bytes);
         } finally {
            if (null != out) {
               out.close();
            }

            if (null != zip) {
               zip.closeEntry();
               zip.close();
            }

         }

         byte[] bytesCompress = out.toByteArray();
         return Optional.of(bytesCompress);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<byte[]> zipUncompress(byte[] bytes) throws Exception {
      if (bytes != null && bytes.length != 0) {
         byte[] b = null;
         ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
         ZipInputStream zip = new ZipInputStream(inputStream);

         try {
            while(zip.getNextEntry() != null) {
               byte[] buf = new byte[1024];
               int num = 1;
               ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

               while((num = zip.read(buf, 0, buf.length)) != -1) {
                  outputStream.write(buf, 0, num);
               }

               b = outputStream.toByteArray();
               outputStream.flush();
               outputStream.close();
            }
         } finally {
            zip.close();
            inputStream.close();
         }

         return Optional.of(b);
      } else {
         return Optional.empty();
      }
   }

   public static Optional<String> zipUncompressToString(byte[] bytes) throws Exception {
      Optional<byte[]> optionalBytes = zipUncompress(bytes);
      if (optionalBytes.isPresent()) {
         String data = new String((byte[])optionalBytes.get(), StandardCharsets.UTF_8);
         return Optional.of(data);
      } else {
         return Optional.empty();
      }
   }

   public static BloomFilter<String> getBloomFilterTypeString(int expectedInsertions, double fpp) {
      Funnel<CharSequence> funnel = Funnels.stringFunnel(Charset.forName("utf-8"));
      return BloomFilter.create(funnel, expectedInsertions, fpp);
   }

   public static BloomFilter<byte[]> getBloomFilterTypeByte(int expectedInsertions, double fpp) {
      Funnel<byte[]> funnel = Funnels.byteArrayFunnel();
      return BloomFilter.create(funnel, expectedInsertions, fpp);
   }

   public static BloomFilter<Long> getBloomFilterTypeLong(int expectedInsertions, double fpp) {
      Funnel<Long> funnel = Funnels.longFunnel();
      return BloomFilter.create(funnel, expectedInsertions, fpp);
   }

   public static BloomFilter<Integer> getBloomFilterTypeInteger(int expectedInsertions, double fpp) {
      Funnel<Integer> funnel = Funnels.integerFunnel();
      return BloomFilter.create(funnel, expectedInsertions, fpp);
   }

   public static String stringToAscii(String value) {
      StringBuffer sbu = new StringBuffer();
      char[] chars = value.toCharArray();

      for(int i = 0; i < chars.length; ++i) {
         if (i != chars.length - 1) {
            sbu.append(chars[i]).append(",");
         } else {
            sbu.append(chars[i]);
         }
      }

      return sbu.toString();
   }

   public static String asciiToString(String value) {
      StringBuffer sbu = new StringBuffer();
      String[] chars = value.split(",");

      for(int i = 0; i < chars.length; ++i) {
         sbu.append((char)Integer.parseInt(chars[i]));
      }

      return sbu.toString();
   }

   public static String AToB(String value) {
      return asciiToString(value);
   }

   public static HttpMessageConverters getFastJsonHttpMessageConverters() {
      FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
      List<MediaType> supportedMediaTypes = new ArrayList();
      supportedMediaTypes.add(MediaType.APPLICATION_JSON);
      supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
      supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
      supportedMediaTypes.add(MediaType.APPLICATION_PDF);
      supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_XML);
      supportedMediaTypes.add(MediaType.IMAGE_GIF);
      supportedMediaTypes.add(MediaType.IMAGE_JPEG);
      supportedMediaTypes.add(MediaType.IMAGE_PNG);
      supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
      supportedMediaTypes.add(MediaType.TEXT_HTML);
      supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
      supportedMediaTypes.add(MediaType.TEXT_PLAIN);
      supportedMediaTypes.add(MediaType.TEXT_XML);
      fastConverter.setSupportedMediaTypes(supportedMediaTypes);
      FastJsonConfig fastJsonConfig = new FastJsonConfig();
      fastJsonConfig.setSerializerFeatures(new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty});
      fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
      fastConverter.setFastJsonConfig(fastJsonConfig);
      Charset charset = Charset.forName("utf8");
      fastConverter.setDefaultCharset(charset);
      return new HttpMessageConverters(new HttpMessageConverter[]{fastConverter});
   }

   public static String humpToLine(String str) {
      Matcher matcher = humpPattern.matcher(str);
      StringBuffer sb = new StringBuffer();

      while(matcher.find()) {
         matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
      }

      matcher.appendTail(sb);
      return sb.toString();
   }

   public static String lineToHump(String str) {
      Matcher matcher = linePattern.matcher(str);
      StringBuffer sb = new StringBuffer();

      while(matcher.find()) {
         matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
      }

      matcher.appendTail(sb);
      return sb.toString();
   }

   public static Long getDataSize(Object obj) {
      Long objectSize = ObjectSizeCalculator.getObjectSize(obj);
      return objectSize;
   }

   public static StopWatch getWatch(String id) {
      return new StopWatch(id);
   }

   public static <T> byte[] msgPackWrite(T v) throws IOException {
      return msgPack2.write(v);
   }

   public static <T> void msgPackWrite(OutputStream out, T v) throws IOException {
      msgPack2.write(out, v);
   }

   public static <T> List<T> msgPackReadList(byte[] bytes, Class<T> clazz) throws IOException {
      return (List)msgPack2.read(bytes, Templates.tList(msgPack2.lookup(clazz)));
   }

   public static <T> T msgPackReadObject(byte[] bytes, Class<T> clazz) throws IOException {
      return msgPack2.read(bytes, clazz);
   }

   public static <K, V> Map<K, V> msgPackReadMap(byte[] bytes, Class<K> clazzKey, Class<V> clazzValue) throws IOException {
      return (Map)msgPack2.read(bytes, Templates.tMap(msgPack2.lookup(clazzKey), msgPack2.lookup(clazzValue)));
   }

   public static <E> Collection<E> msgPackReadCollection(byte[] bytes, Class<E> clazz) throws IOException {
      return (Collection)msgPack2.read(bytes, Templates.tCollection(msgPack2.lookup(clazz)));
   }

   public static boolean hasMillis(Duration duration) {
      return duration.toMillis() % 1000L != 0L;
   }

   public static long toSeconds(Duration duration) {
      return roundUpIfNecessary(duration.toMillis(), duration.getSeconds());
   }

   public static long toSeconds(long timeout, TimeUnit unit) {
      return roundUpIfNecessary(timeout, unit.toSeconds(timeout));
   }

   public static long toMillis(long timeout, TimeUnit unit) {
      return roundUpIfNecessary(timeout, unit.toMillis(timeout));
   }

   private static long roundUpIfNecessary(long timeout, long convertedTimeout) {
      return timeout > 0L && convertedTimeout == 0L ? 1L : convertedTimeout;
   }

   public static InetAddress getLocalHostExactAddress() {
      try {
         InetAddress candidateAddress = null;
         Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

         while(networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = (NetworkInterface)networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();

            while(inetAddrs.hasMoreElements()) {
               InetAddress inetAddr = (InetAddress)inetAddrs.nextElement();
               if (!inetAddr.isLoopbackAddress()) {
                  if (inetAddr.isSiteLocalAddress()) {
                     return inetAddr;
                  }

                  if (candidateAddress == null) {
                     candidateAddress = inetAddr;
                  }
               }
            }
         }

         return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static byte[] serialize(Object t) throws SerializationException {
      FastJsonConfig fastJsonConfig = new FastJsonConfig();
      fastJsonConfig.setCharset(StandardCharsets.UTF_8);
      fastJsonConfig.setFeatures(new Feature[]{Feature.SupportAutoType});
      fastJsonConfig.setSerializerFeatures(new SerializerFeature[]{SerializerFeature.WriteClassName});
      if (t == null) {
         return new byte[0];
      } else {
         try {
            return JSON.toJSONBytes(StandardCharsets.UTF_8, t, fastJsonConfig.getSerializeConfig(), fastJsonConfig.getSerializeFilters(), fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, fastJsonConfig.getSerializerFeatures());
         } catch (Exception var3) {
            throw new SerializationException("Could not serialize: " + var3.getMessage(), var3);
         }
      }
   }

   public static <T> T deserialize(byte[] bytes, Class<T> type) throws SerializationException {
      FastJsonConfig fastJsonConfig = new FastJsonConfig();
      fastJsonConfig.setCharset(StandardCharsets.UTF_8);
      fastJsonConfig.setFeatures(new Feature[]{Feature.SupportAutoType});
      fastJsonConfig.setSerializerFeatures(new SerializerFeature[]{SerializerFeature.WriteClassName});
      if (bytes != null && bytes.length != 0) {
         try {
            return JSON.parseObject(bytes, fastJsonConfig.getCharset(), type, fastJsonConfig.getParserConfig(), fastJsonConfig.getParseProcess(), JSON.DEFAULT_PARSER_FEATURE, fastJsonConfig.getFeatures());
         } catch (Exception var4) {
            throw new SerializationException("Could not deserialize: " + var4.getMessage(), var4);
         }
      } else {
         return null;
      }
   }

   public static Long getSnowflakeId(Long workerId) {
      if (workerId >= 0L && workerId < 1024L) {
         snowflakeShardingKeyGenerator.getProperties().put("worker.id", String.valueOf(workerId));
         Long generateKey = (Long)snowflakeShardingKeyGenerator.generateKey();
         return generateKey;
      } else {
         throw new RuntimeException(String.format("workerId is not support range must be [0,1024)"));
      }
   }

   public static Long getSnowflakeId(Long workerId, Long backwards) {
      if (workerId >= 0L && workerId < 1024L) {
         snowflakeShardingKeyGenerator.getProperties().put("worker.id", String.valueOf(workerId));
         snowflakeShardingKeyGenerator.getProperties().put("max.tolerate.time.difference.milliseconds", String.valueOf(backwards));
         Long generateKey = (Long)snowflakeShardingKeyGenerator.generateKey();
         return generateKey;
      } else {
         throw new RuntimeException(String.format("workerId is not support range must be [0,1024)"));
      }
   }

   public static String getSnowflakeIdStr(Long workerId) {
      if (workerId >= 0L && workerId < 1024L) {
         snowflakeShardingKeyGenerator.getProperties().put("worker.id", String.valueOf(workerId));
         Long generateKey = (Long)snowflakeShardingKeyGenerator.generateKey();
         return String.valueOf(generateKey);
      } else {
         throw new RuntimeException(String.format("workerId is not support range must be [0,1024)"));
      }
   }

   public static String getSnowflakeIdStr(Long workerId, Long backwards) {
      if (workerId >= 0L && workerId < 1024L) {
         snowflakeShardingKeyGenerator.getProperties().put("worker.id", String.valueOf(workerId));
         snowflakeShardingKeyGenerator.getProperties().put("max.tolerate.time.difference.milliseconds", String.valueOf(backwards));
         Long generateKey = (Long)snowflakeShardingKeyGenerator.generateKey();
         return String.valueOf(generateKey);
      } else {
         throw new RuntimeException(String.format("workerId is not support range must be [0,1024)"));
      }
   }

   public static Boolean checkVersion(String vera, String verb) {
      boolean flag = true;
      if (!org.apache.commons.lang3.StringUtils.isBlank(vera) && !org.apache.commons.lang3.StringUtils.isBlank(verb)) {
         String[] appVersion1 = vera.replaceAll("[a-zA-Z]", "").split("\\.");
         String[] appVersion2 = verb.replaceAll("[a-zA-Z]", "").split("\\.");
         int lim = Math.min(appVersion1.length, appVersion2.length);

         for(int i = 0; i < lim; ++i) {
            if (Integer.parseInt(appVersion1[i]) > Integer.parseInt(appVersion2[i])) {
               flag = true;
               break;
            }

            if (Integer.parseInt(appVersion1[i]) < Integer.parseInt(appVersion2[i])) {
               flag = false;
               break;
            }
         }

         return flag;
      } else {
         throw new RuntimeException("checkVersion vera or verb is null");
      }
   }

   public static String execForStr(String... cmds) {
      InputStream in = null;
      Process process = null;

      String var4;
      try {
         process = RuntimeUtil.exec(cmds);
         in = process.getInputStream();
         String cmdBackStr = IoUtil.read(in, CharsetUtil.systemCharset());
         process.waitFor();
         var4 = cmdBackStr;
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      } finally {
         IoUtil.close(in);
         RuntimeUtil.destroy(process);
      }

      return var4;
   }

   public static String getCurrentTime() {
      return DateUtil.date(System.currentTimeMillis()).toString("yyyy-MM-dd HH:mm:ss");
   }

   public static String getCurrentTime(Long time) {
      return DateUtil.date(time).toString("yyyy-MM-dd HH:mm:ss");
   }

   public static Long getTimeStamp(String time) throws ParseException {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sdf.parse(time).getTime();
   }

   public static Date getTimeDate(String time) throws ParseException {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sdf.parse(time);
   }

   public static long getObjectSize(Object object) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);

      long var3;
      try {
         if (object != null) {
            oos.writeObject(object);
            oos.flush();
            var3 = (long)baos.size();
            return var3;
         }

         var3 = 0L;
      } finally {
         baos.close();
         oos.close();
      }

      return var3;
   }

   public static byte[] UnBZip(byte[] data) throws Exception {
      ByteArrayInputStream bis = null;
      ByteArrayOutputStream bos = null;
      CompressorInputStream cis = null;

      byte[] var4;
      try {
         bis = new ByteArrayInputStream(data);
         bos = new ByteArrayOutputStream();
         cis = CompressUtil.getIn("bzip2", bis);
         IOUtils.write((InputStream)cis, (OutputStream)bos);
         var4 = bos.toByteArray();
      } finally {
         if (ObjectUtil.isNotNull(bis)) {
            bis.close();
         }

         if (ObjectUtil.isNotNull(bos)) {
            bos.close();
         }

         if (ObjectUtil.isNotNull(cis)) {
            cis.close();
         }

      }

      return var4;
   }

   public static byte[] BZip(byte[] data) throws Exception {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      Throwable var2 = null;

      byte[] var4;
      try {
         CompressorOutputStream cos = CompressUtil.getOut("bzip2", bos);
         cos.write(data);
         cos.flush();
         cos.close();
         var4 = bos.toByteArray();
      } catch (Throwable var13) {
         var2 = var13;
         throw var13;
      } finally {
         if (bos != null) {
            if (var2 != null) {
               try {
                  bos.close();
               } catch (Throwable var12) {
                  var2.addSuppressed(var12);
               }
            } else {
               bos.close();
            }
         }

      }

      return var4;
   }

   public static <T> T[] convertArray(Class<T> targetType, Object[] arrayObjects) {
      if (targetType == null) {
         return (T[]) arrayObjects;
      } else if (arrayObjects == null) {
         return null;
      } else {
         T[] targetArray = (T[]) Array.newInstance(targetType, arrayObjects.length);

         try {
            System.arraycopy(arrayObjects, 0, targetArray, 0, arrayObjects.length);
            return targetArray;
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }
   }
}
