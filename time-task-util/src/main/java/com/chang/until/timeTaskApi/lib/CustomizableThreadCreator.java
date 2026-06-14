package com.chang.until.timeTaskApi.lib;

import cn.hutool.core.lang.Assert;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomizableThreadCreator implements Serializable {
   public static final String CGLIB_CLASS_SEPARATOR = "$$";
   private static final char PACKAGE_SEPARATOR = '.';
   private static final char INNER_CLASS_SEPARATOR = '$';
   private final AtomicInteger threadCount = new AtomicInteger(0);
   private String threadNamePrefix;
   private int threadPriority = 5;
   private boolean daemon = false;
   private ThreadGroup threadGroup;

   public CustomizableThreadCreator() {
      this.threadNamePrefix = this.getDefaultThreadNamePrefix();
   }

   public CustomizableThreadCreator(String threadNamePrefix) {
      this.threadNamePrefix = threadNamePrefix != null ? threadNamePrefix : this.getDefaultThreadNamePrefix();
   }

   public String getThreadNamePrefix() {
      return this.threadNamePrefix;
   }

   public void setThreadNamePrefix(String threadNamePrefix) {
      this.threadNamePrefix = threadNamePrefix != null ? threadNamePrefix : this.getDefaultThreadNamePrefix();
   }

   public int getThreadPriority() {
      return this.threadPriority;
   }

   public void setThreadPriority(int threadPriority) {
      this.threadPriority = threadPriority;
   }

   public boolean isDaemon() {
      return this.daemon;
   }

   public void setDaemon(boolean daemon) {
      this.daemon = daemon;
   }

   public void setThreadGroupName(String name) {
      this.threadGroup = new ThreadGroup(name);
   }

   public ThreadGroup getThreadGroup() {
      return this.threadGroup;
   }

   public void setThreadGroup(ThreadGroup threadGroup) {
      this.threadGroup = threadGroup;
   }

   public Thread createThread(Runnable runnable) {
      Thread thread = new Thread(this.getThreadGroup(), runnable, this.nextThreadName());
      thread.setPriority(this.getThreadPriority());
      thread.setDaemon(this.isDaemon());
      return thread;
   }

   protected String nextThreadName() {
      return this.getThreadNamePrefix() + this.threadCount.incrementAndGet();
   }

   public String getQualifiedName(Class<?> clazz) {
      Assert.notNull(clazz, "Class must not be null", new Object[0]);
      return clazz.getTypeName();
   }

   public String getShortName(String className) {
      int lastDotIndex = className.lastIndexOf(46);
      int nameEndIndex = className.indexOf("$$");
      if (nameEndIndex == -1) {
         nameEndIndex = className.length();
      }

      String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
      shortName = shortName.replace('$', '.');
      return shortName;
   }

   public String getShortName(Class<?> clazz) {
      return this.getShortName(this.getQualifiedName(clazz));
   }

   protected String getDefaultThreadNamePrefix() {
      return this.getShortName(this.getClass()) + "-";
   }
}
