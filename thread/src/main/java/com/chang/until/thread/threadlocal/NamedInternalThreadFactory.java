package com.chang.until.thread.threadlocal;

import com.chang.until.thread.utils.NamedThreadFactory;

public class NamedInternalThreadFactory extends NamedThreadFactory {
   public NamedInternalThreadFactory() {
   }

   public NamedInternalThreadFactory(String prefix) {
      super(prefix, false);
   }

   public NamedInternalThreadFactory(String prefix, boolean daemon) {
      super(prefix, daemon);
   }

   public Thread newThread(Runnable runnable) {
      String name = this.mPrefix + this.mThreadNum.getAndIncrement();
      InternalThread ret = new InternalThread(this.mGroup, runnable, name, 0L);
      ret.setDaemon(this.mDaemon);
      return ret;
   }
}
