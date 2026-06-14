package com.chang.until.thread.model;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
   public static final void second(long seconds) {
      try {
         TimeUnit.SECONDS.sleep(seconds);
      } catch (InterruptedException var3) {
      }

   }

   public static final void millisecond(long millisecond) {
      try {
         TimeUnit.MILLISECONDS.sleep(millisecond);
      } catch (InterruptedException var3) {
      }

   }
}
