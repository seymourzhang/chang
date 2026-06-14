package com.chang.util.source.file;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.chang.common.CommUtils;
import com.chang.util.source.OutputSource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(FileOutput.class);
   private String path;
   private final String keyName;
   private final Map<String, Object> parm;

   public FileOutput(String keyName, String path, Map<String, Object> parm) {
      this.keyName = keyName;
      this.path = path;
      this.parm = parm;
   }

   public void Output(Object o) {
      try {
         if (BeanUtil.isBean(o.getClass())) {
            FileUtil.appendUtf8String(CommUtils.getJSONArrayStringFromObject(o), this.path);
         } else if (o instanceof String) {
            FileUtil.appendUtf8String((String)o, this.path);
         } else {
            if (!(o instanceof List)) {
               throw new RuntimeException("type error " + this.keyName);
            }

            Iterator var2 = ((List)o).iterator();

            while(var2.hasNext()) {
               Object jo = var2.next();
               if (BeanUtil.isBean(jo.getClass())) {
                  FileUtil.appendUtf8String(CommUtils.getJSONArrayStringFromObject(o), this.path);
               } else {
                  if (!(jo instanceof String)) {
                     throw new RuntimeException("type error " + this.keyName);
                  }

                  FileUtil.appendUtf8String((String)jo, this.path);
               }
            }
         }
      } catch (Exception var4) {
         log.error("Output keyName: {}", this.keyName, var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
   }

   public void setPath(String path) {
      this.path = path;
   }
}
