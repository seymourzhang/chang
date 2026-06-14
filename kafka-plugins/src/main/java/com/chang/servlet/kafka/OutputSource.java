package com.chang.servlet.kafka;

import java.util.Map;

public interface OutputSource extends Base {
   void Output(Object var1);

   default Map<String, Object> getSourceExParm() {
      return null;
   }

   default void setTopic(String topic) {
   }
}
