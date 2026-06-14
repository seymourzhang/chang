package com.chang.util.rocketmqApi;

public interface RocketMQReplyListener<T, R> {
   R onMessage(T var1);
}
