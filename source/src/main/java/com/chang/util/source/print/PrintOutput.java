package com.chang.util.source.print;

import com.chang.util.source.OutputSource;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(PrintOutput.class);

   public void Output(Object o) {
      System.out.println(o);
   }

   public Map<String, Object> getSourceExParm() {
      return null;
   }
}
