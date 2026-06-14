package com.chang.util.source.log;

import com.chang.common.CommUtils;
import com.chang.util.source.OutputSource;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(LogOutput.class);

   public void Output(Object o) {
      if (o instanceof String) {
         log.info((String)o);
      } else {
         log.info(CommUtils.getJSONStringFromObject(o));
      }

   }

   public Map<String, Object> getSourceExParm() {
      return null;
   }

   public void close() {
   }
}
