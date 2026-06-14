package com.chang.util.source.mqtt;

import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import com.chang.util.source.OutputSource;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublic {
   private static final Logger log = LoggerFactory.getLogger(MqttPublic.class);

   public static void doWith(String topic, ExecutorService executorService, byte[] message, Map<String, Object> parm, String messageType, Function<Object, Object> function, OutputSource source) {
      executorService.execute(() -> {
         try {
            if (StringUtils.isBlank(topic)) {
               throw new RuntimeException("topic is blank or null");
            }

            if (ObjectUtil.isNull(message)) {
               throw new RuntimeException("payload is null");
            }

            parm.put("topic", topic);
            SourceContext.setExParm(parm);
            Object revData = Util.getRevData(messageType, message);
            if (ObjectUtil.isNotNull(function)) {
               source.Output(function.apply(revData));
            } else {
               source.Output(revData);
            }

            SourceContext.clearExParm();
         } catch (Exception var8) {
            log.error("[MQTT] mArrived err:", var8);
         }

      });
   }
}
