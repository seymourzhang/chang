package com.chang.store;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.CommUtils;
import com.chang.hbaselib.HbaseDataVo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseDataStore implements DataStore {
   private static final Logger log = LoggerFactory.getLogger(HbaseDataStore.class);
   private boolean ordered;
   private final Map<String, HashMap<String, Object>> linkedMm = new LinkedHashMap(10000);
   private final Map<String, HashMap<String, Object>> HashMm = new HashMap(10000);

   public HbaseDataStore(boolean ordered) {
      this.ordered = ordered;
   }

   public void clearDataStore() {
      if (this.ordered) {
         this.linkedMm.clear();
      } else {
         this.HashMm.clear();
      }

   }

   public Map<String, HashMap<String, Object>> getLinkedMm() {
      return Collections.unmodifiableMap(this.linkedMm);
   }

   public Map<String, HashMap<String, Object>> getHashMm() {
      return Collections.unmodifiableMap(this.HashMm);
   }

   public void add(String rowKey, String key, byte[] value) {
      if (this.ordered) {
         if (!this.linkedMm.containsKey(rowKey)) {
            this.linkedMm.put(rowKey, new HashMap(100));
         }

         ((HashMap)this.linkedMm.get(rowKey)).put(key, value);
      } else {
         if (!this.HashMm.containsKey(rowKey)) {
            this.HashMm.put(rowKey, new HashMap(100));
         }

         ((HashMap)this.HashMm.get(rowKey)).put(key, value);
      }

   }

   public void add(String rowKey, String key, String value) {
      if (this.ordered) {
         if (!this.linkedMm.containsKey(rowKey)) {
            this.linkedMm.put(rowKey, new HashMap(100));
         }

         ((HashMap)this.linkedMm.get(rowKey)).put(key, value);
      } else {
         if (!this.HashMm.containsKey(rowKey)) {
            this.HashMm.put(rowKey, new HashMap(100));
         }

         ((HashMap)this.HashMm.get(rowKey)).put(key, value);
      }

   }

   public Object get(String rowKey, String key) {
      return this.ordered ? ((HashMap)this.linkedMm.get(rowKey)).get(key) : ((HashMap)this.HashMm.get(rowKey)).get(key);
   }

   public <T> T get(String rowKey, Class<T> clazz) {
      HashMap<String, Object> row = null;
      if (this.ordered) {
         row = (HashMap)this.linkedMm.get(rowKey);
      } else {
         row = (HashMap)this.HashMm.get(rowKey);
      }

      if (ObjectUtil.isNull(row)) {
         return null;
      } else if (clazz == JSONObject.class) {
         log.info("Some values cannot be converted. Please note！");
         return (T) new JSONObject(row);
      } else {
         try {
            JSONObject backBean = new JSONObject(true);
            Iterator var5 = row.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry<String, Object> entry = (Map.Entry)var5.next();
               Field field = ClassUtil.getDeclaredField(clazz, (String)entry.getKey());
               if (!ObjectUtil.isNull(field)) {
                  Class<?> type = field.getType();
                  byte[] value = (byte[])((byte[])entry.getValue());
                  if (!ObjectUtil.isNull(value)) {
                     if (BeanUtil.isBean(type)) {
                        JSONObject bean = JSONObject.parseObject(Bytes.toString(value));
                        Object o = JSON.toJavaObject(bean, type);
                        backBean.put((String)entry.getKey(), o);
                     } else if (type == String.class) {
                        backBean.put((String)entry.getKey(), Bytes.toString(value));
                     } else if (type == Byte.TYPE) {
                        backBean.put((String)entry.getKey(), value);
                     } else if (type == Byte.class) {
                        backBean.put((String)entry.getKey(), value);
                     } else if (type == Short.TYPE) {
                        backBean.put((String)entry.getKey(), Bytes.toShort(value));
                     } else if (type == Short.class) {
                        backBean.put((String)entry.getKey(), Bytes.toShort(value));
                     } else if (type == Integer.TYPE) {
                        backBean.put((String)entry.getKey(), Bytes.toInt(value));
                     } else if (type == Integer.class) {
                        backBean.put((String)entry.getKey(), Bytes.toInt(value));
                     } else if (type == Long.TYPE) {
                        backBean.put((String)entry.getKey(), Bytes.toLong(value));
                     } else if (type == Long.class) {
                        backBean.put((String)entry.getKey(), Bytes.toLong(value));
                     } else if (type == Float.TYPE) {
                        backBean.put((String)entry.getKey(), Bytes.toFloat(value));
                     } else if (type == Float.class) {
                        backBean.put((String)entry.getKey(), Bytes.toFloat(value));
                     } else if (type == Double.TYPE) {
                        backBean.put((String)entry.getKey(), Bytes.toDouble(value));
                     } else if (type == Double.class) {
                        backBean.put((String)entry.getKey(), Bytes.toDouble(value));
                     } else if (type == double[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToDouble(value));
                     } else if (type == Double[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToDouble(value));
                     } else if (type == float[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToFloat(value));
                     } else if (type == Float[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToFloat(value));
                     } else if (type == long[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToLong(value));
                     } else if (type == Long[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToLong(value));
                     } else if (type == int[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToInteger(value));
                     } else if (type == Integer[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToInteger(value));
                     } else if (type == short[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToShort(value));
                     } else if (type == Short[].class) {
                        backBean.put((String)entry.getKey(), CommUtils.byteArrayToShort(value));
                     } else if (type == byte[].class) {
                        backBean.put((String)entry.getKey(), value);
                     } else {
                        if (type != Byte[].class) {
                           throw new RuntimeException("type is Err");
                        }

                        backBean.put((String)entry.getKey(), value);
                     }
                  }
               }
            }

            if (backBean.size() > 0) {
               return JSON.toJavaObject(backBean, clazz);
            } else {
               return null;
            }
         } catch (Exception var12) {
            log.error("get Err", var12);
            throw new RuntimeException("get Err", var12);
         }
      }
   }

   public <T> List<T> get(Class<T> clazz) {
      return this.ordered ? (List)this.linkedMm.entrySet().stream().map((x) -> {
         return this.get((String)x.getKey(), clazz);
      }).filter(ObjectUtil::isNotNull).collect(Collectors.toList()) : (List)this.HashMm.entrySet().stream().map((x) -> {
         return this.get((String)x.getKey(), clazz);
      }).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
   }

   public int getCount() {
      return this.ordered ? this.linkedMm.keySet().size() : this.HashMm.keySet().size();
   }

   private JSONObject getPagingQuery(int StartNum, int EndNum) {
      if (EndNum < StartNum) {
         throw new RuntimeException("EndNum < StartNum");
      } else {
         int count = 0;
         JSONObject toJson = null;
         Iterator var5;
         Map.Entry entry;
         if (this.ordered) {
            toJson = new JSONObject(true);
            var5 = this.linkedMm.entrySet().iterator();

            while(var5.hasNext()) {
               entry = (Map.Entry)var5.next();
               if (count < StartNum) {
                  ++count;
               } else {
                  if (count >= StartNum && count < EndNum) {
                     toJson.put((String)entry.getKey(), entry.getValue());
                  }

                  ++count;
                  if (count >= EndNum) {
                     break;
                  }
               }
            }
         } else {
            toJson = new JSONObject();
            var5 = this.HashMm.entrySet().iterator();

            while(var5.hasNext()) {
               entry = (Map.Entry)var5.next();
               if (count < StartNum) {
                  ++count;
               } else {
                  if (count >= StartNum && count < EndNum) {
                     toJson.put((String)entry.getKey(), entry.getValue());
                  }

                  ++count;
                  if (count >= EndNum) {
                     break;
                  }
               }
            }
         }

         return toJson;
      }
   }

   public void remove(String rowKey, String key) {
      if (this.ordered) {
         ((HashMap)this.linkedMm.get(rowKey)).remove(key);
      } else {
         ((HashMap)this.HashMm.get(rowKey)).remove(key);
      }

   }

   public void removeAll() {
      if (this.ordered) {
         this.linkedMm.clear();
      } else {
         this.HashMm.clear();
      }

   }

   public JSONObject toJSONObject() {
      JSONObject toJson = null;
      Iterator var2;
      Map.Entry entry;
      if (this.ordered) {
         toJson = new JSONObject(true);
         var2 = this.linkedMm.entrySet().iterator();

         while(var2.hasNext()) {
            entry = (Map.Entry)var2.next();
            toJson.put((String)entry.getKey(), entry.getValue());
         }
      } else {
         toJson = new JSONObject();
         var2 = this.HashMm.entrySet().iterator();

         while(var2.hasNext()) {
            entry = (Map.Entry)var2.next();
            toJson.put((String)entry.getKey(), entry.getValue());
         }
      }

      return toJson;
   }

   public List<HbaseDataVo> toDataList() {
      List<HbaseDataVo> hbaseDataVos = new ArrayList();
      Iterator var2;
      Map.Entry entry;
      HbaseDataVo dataVo;
      JSONObject data;
      if (this.ordered) {
         var2 = this.linkedMm.entrySet().iterator();

         while(var2.hasNext()) {
            entry = (Map.Entry)var2.next();
            dataVo = new HbaseDataVo();
            dataVo.setRowKey((String)entry.getKey());
            data = new JSONObject((Map)entry.getValue());
            dataVo.setObject(data);
            hbaseDataVos.add(dataVo);
         }
      } else {
         var2 = this.HashMm.entrySet().iterator();

         while(var2.hasNext()) {
            entry = (Map.Entry)var2.next();
            dataVo = new HbaseDataVo();
            dataVo.setRowKey((String)entry.getKey());
            data = new JSONObject((Map)entry.getValue());
            dataVo.setObject(data);
            hbaseDataVos.add(dataVo);
         }
      }

      return hbaseDataVos;
   }

   public Boolean isHas() {
      return this.linkedMm.isEmpty() && this.HashMm.isEmpty() ? false : true;
   }

   public List<String> getRowIds() {
      return this.ordered ? ListUtil.toList(this.linkedMm.keySet()) : ListUtil.toList(this.HashMm.keySet());
   }
}
