package com.chang.until.thread.utils;

import java.io.OutputStream;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class JVMUtil {
   public static void jstack(OutputStream stream) throws Exception {
      ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
      ThreadInfo[] var2 = threadMxBean.dumpAllThreads(true, true);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ThreadInfo threadInfo = var2[var4];
         stream.write(getThreadDumpString(threadInfo).getBytes());
      }

   }

   private static String getThreadDumpString(ThreadInfo threadInfo) {
      StringBuilder sb = new StringBuilder("\"" + threadInfo.getThreadName() + "\" Id=" + threadInfo.getThreadId() + " " + threadInfo.getThreadState());
      if (threadInfo.getLockName() != null) {
         sb.append(" on " + threadInfo.getLockName());
      }

      if (threadInfo.getLockOwnerName() != null) {
         sb.append(" owned by \"" + threadInfo.getLockOwnerName() + "\" Id=" + threadInfo.getLockOwnerId());
      }

      if (threadInfo.isSuspended()) {
         sb.append(" (suspended)");
      }

      if (threadInfo.isInNative()) {
         sb.append(" (in native)");
      }

      sb.append('\n');
      int i = 0;
      StackTraceElement[] stackTrace = threadInfo.getStackTrace();

      int var7;
      int var8;
      for(MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors(); i < stackTrace.length && i < 32; ++i) {
         StackTraceElement ste = stackTrace[i];
         sb.append("\tat " + ste.toString());
         sb.append('\n');
         if (i == 0 && threadInfo.getLockInfo() != null) {
            Thread.State ts = threadInfo.getThreadState();
            switch (ts) {
               case BLOCKED:
                  sb.append("\t-  blocked on " + threadInfo.getLockInfo());
                  sb.append('\n');
                  break;
               case WAITING:
                  sb.append("\t-  waiting on " + threadInfo.getLockInfo());
                  sb.append('\n');
                  break;
               case TIMED_WAITING:
                  sb.append("\t-  waiting on " + threadInfo.getLockInfo());
                  sb.append('\n');
            }
         }

         MonitorInfo[] var11 = lockedMonitors;
         var7 = lockedMonitors.length;

         for(var8 = 0; var8 < var7; ++var8) {
            MonitorInfo mi = var11[var8];
            if (mi.getLockedStackDepth() == i) {
               sb.append("\t-  locked " + mi);
               sb.append('\n');
            }
         }
      }

      if (i < stackTrace.length) {
         sb.append("\t...");
         sb.append('\n');
      }

      LockInfo[] locks = threadInfo.getLockedSynchronizers();
      if (locks.length > 0) {
         sb.append("\n\tNumber of locked synchronizers = " + locks.length);
         sb.append('\n');
         LockInfo[] var12 = locks;
         var7 = locks.length;

         for(var8 = 0; var8 < var7; ++var8) {
            LockInfo li = var12[var8];
            sb.append("\t- " + li);
            sb.append('\n');
         }
      }

      sb.append('\n');
      return sb.toString();
   }
}
