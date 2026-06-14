package com.chang.until.thread.threadpool.support;

import com.chang.until.thread.utils.JVMUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {
   private static final Logger log = LoggerFactory.getLogger(AbortPolicyWithReport.class);
   private final String threadName;
   private final String dumpPath;
   private static volatile long lastPrintTime = 0L;
   private static final long TEN_MINUTES_MILLS = 600000L;
   private static final String OS_WIN_PREFIX = "win";
   private static final String OS_NAME_KEY = "os.name";
   private static final String WIN_DATETIME_FORMAT = "yyyy-MM-dd_HH-mm-ss";
   private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd_HH:mm:ss";
   private static Semaphore guard = new Semaphore(1);

   public AbortPolicyWithReport(String threadName, String dumpPath) {
      this.threadName = threadName;
      this.dumpPath = dumpPath;
   }

   public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
      String msg = String.format("Thread pool is EXHAUSTED! Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d), Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)", this.threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
   }

   private void dumpJStack() {
      long now = System.currentTimeMillis();
      if (now - lastPrintTime >= 600000L) {
         if (guard.tryAcquire()) {
            ExecutorService pool = Executors.newSingleThreadExecutor();
            pool.execute(() -> {
               String os = System.getProperty("os.name").toLowerCase();
               SimpleDateFormat sdf;
               if (os.contains("win")) {
                  sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
               } else {
                  sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
               }

               String dateStr = sdf.format(new Date());

               try {
                  FileOutputStream jStackStream = new FileOutputStream(new File(this.dumpPath, "Thread_JStack.log." + dateStr));
                  Throwable var5 = null;

                  try {
                     JVMUtil.jstack(jStackStream);
                  } catch (Throwable var23) {
                     var5 = var23;
                     throw var23;
                  } finally {
                     if (jStackStream != null) {
                        if (var5 != null) {
                           try {
                              jStackStream.close();
                           } catch (Throwable var22) {
                              var5.addSuppressed(var22);
                           }
                        } else {
                           jStackStream.close();
                        }
                     }

                  }
               } catch (Throwable var25) {
                  log.error("dump jStack error", var25);
               } finally {
                  guard.release();
               }

               lastPrintTime = System.currentTimeMillis();
            });
            pool.shutdown();
         }
      }
   }
}
