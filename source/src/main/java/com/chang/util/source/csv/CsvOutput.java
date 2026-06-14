package com.chang.util.source.csv;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.CommUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.chang.util.source.OutputSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(CsvOutput.class);
   private Long fileIndex = 0L;
   private String fileFullPath;
   private CsvWriter writer;
   private final Long fileMaxSize;
   private final String filePath;
   private final String fileName;
   private Map<String, Object> parm;
   private String keyName;
   private boolean isFirst = true;

   public CsvOutput(String keyName, String filePath, String fileName, Long fileMaxSize, Map<String, Object> parm) {
      this.filePath = filePath;
      this.fileName = fileName;
      this.fileFullPath = filePath + "/" + fileName + "_" + this.fileIndex + ".csv";
      this.fileMaxSize = fileMaxSize;
      this.writer = CsvUtil.getWriter(this.fileFullPath, CharsetUtil.CHARSET_UTF_8, true);
      this.parm = parm;
      this.keyName = keyName;
   }

   public void Output(Object o) {
      if (o instanceof List) {
         List beans = (List)o;
         if (FileUtil.size(FileUtil.file(this.fileFullPath)) > this.fileMaxSize) {
            Long var3 = this.fileIndex;
            Long var4 = this.fileIndex = this.fileIndex + 1L;
            this.fileFullPath = this.filePath + "/" + this.fileName + "_" + this.fileIndex + ".csv";
            if (ObjectUtil.isNotNull(this.writer)) {
               this.writer.flush();
               this.writer.close();
               this.writer = CsvUtil.getWriter(this.fileFullPath, CharsetUtil.CHARSET_UTF_8, true);
               this.isFirst = true;
            }
         }

         Map map;
         if (ObjectUtil.isNotNull(this.writer)) {
            for(Iterator var10 = beans.iterator(); var10.hasNext(); this.writer.writeLine(Convert.toStrArray(map.values()))) {
               Object bean = var10.next();
               map = BeanUtil.beanToMap(bean, new String[0]);
               if (this.isFirst) {
                  this.writer.writeHeaderLine((String[])map.keySet().toArray(new String[0]));
                  this.isFirst = false;
               }
            }
         }
      } else if (BeanUtil.isBean(o.getClass())) {
         if (ObjectUtil.isNotNull(this.writer)) {
            Map<String, Object> map = BeanUtil.beanToMap(o, new String[0]);
            if (this.isFirst) {
               this.writer.writeHeaderLine((String[])map.keySet().toArray(new String[0]));
               this.isFirst = false;
            }

            this.writer.writeLine(Convert.toStrArray(map.values()));
            this.writer.flush();
         }
      } else {
         if (!(o instanceof String)) {
            log.error("keyName: {} Output not Bean List String class", this.keyName);
            throw new RuntimeException("not List class");
         }

         String data = (String)o;
         if (!CommUtils.isJson((String)o)) {
            log.error("keyName: {} Output not json", this.keyName);
            throw new RuntimeException("Output not json");
         }

         JSONObject jsonObject = JSONObject.parseObject(data);
         if (this.isFirst) {
            this.writer.writeHeaderLine((String[])jsonObject.keySet().toArray(new String[0]));
            this.isFirst = false;
         }

         this.writer.writeLine(Convert.toStrArray(jsonObject.values()));
         this.writer.flush();
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      if (ObjectUtil.isNotNull(this.writer)) {
         this.writer.flush();
         this.writer.close();
      }

   }
}
