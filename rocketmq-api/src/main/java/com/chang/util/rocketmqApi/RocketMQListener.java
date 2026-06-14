package com.chang.util.rocketmqApi;

public interface RocketMQListener<T> {
   void onMessage(T var1);
}
