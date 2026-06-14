package com.chang.netty.until.sync;

public interface NetEvent {
   void connectionFail(String var1);

   void connectionLost(String var1);

   void connectionOk(String var1);
}
