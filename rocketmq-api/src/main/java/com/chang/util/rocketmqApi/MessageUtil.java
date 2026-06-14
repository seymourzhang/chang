package com.chang.util.rocketmqApi;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageAccessor;

public class MessageUtil {
   public static Message createReplyMessage(Message requestMessage, byte[] body) throws MQClientException {
      if (requestMessage != null) {
         Message replyMessage = new Message();
         String cluster = requestMessage.getProperty("CLUSTER");
         String replyTo = requestMessage.getProperty("REPLY_TO_CLIENT");
         String correlationId = requestMessage.getProperty("CORRELATION_ID");
         String ttl = requestMessage.getProperty("TTL");
         replyMessage.setBody(body);
         if (cluster != null) {
            String replyTopic = MixAll.getReplyTopic(cluster);
            replyMessage.setTopic(replyTopic);
            MessageAccessor.putProperty(replyMessage, "MSG_TYPE", "reply");
            MessageAccessor.putProperty(replyMessage, "CORRELATION_ID", correlationId);
            MessageAccessor.putProperty(replyMessage, "REPLY_TO_CLIENT", replyTo);
            MessageAccessor.putProperty(replyMessage, "TTL", ttl);
            return replyMessage;
         } else {
            throw new MQClientException(10007, "create reply message fail, requestMessage error, property[CLUSTER] is null.");
         }
      } else {
         throw new MQClientException(10007, "create reply message fail, requestMessage cannot be null.");
      }
   }

   public static String getReplyToClient(Message msg) {
      return msg.getProperty("REPLY_TO_CLIENT");
   }
}
