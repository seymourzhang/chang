package com.chang.until.thread.threadpool.support;

public class ThreadConfig {
   private String name;
   private String dumpPath;
   private int cores;
   private int threads;
   private int queues;
   private int alive;

   public String getName() {
      return this.name;
   }

   public String getDumpPath() {
      return this.dumpPath;
   }

   public int getCores() {
      return this.cores;
   }

   public int getThreads() {
      return this.threads;
   }

   public int getQueues() {
      return this.queues;
   }

   public int getAlive() {
      return this.alive;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setDumpPath(String dumpPath) {
      this.dumpPath = dumpPath;
   }

   public void setCores(int cores) {
      this.cores = cores;
   }

   public void setThreads(int threads) {
      this.threads = threads;
   }

   public void setQueues(int queues) {
      this.queues = queues;
   }

   public void setAlive(int alive) {
      this.alive = alive;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ThreadConfig)) {
         return false;
      } else {
         ThreadConfig other = (ThreadConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getCores() != other.getCores()) {
            return false;
         } else if (this.getThreads() != other.getThreads()) {
            return false;
         } else if (this.getQueues() != other.getQueues()) {
            return false;
         } else if (this.getAlive() != other.getAlive()) {
            return false;
         } else {
            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            Object this$dumpPath = this.getDumpPath();
            Object other$dumpPath = other.getDumpPath();
            if (this$dumpPath == null) {
               if (other$dumpPath != null) {
                  return false;
               }
            } else if (!this$dumpPath.equals(other$dumpPath)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ThreadConfig;
   }

   public int hashCode() {
      int result = 1;
      result = result * 59 + this.getCores();
      result = result * 59 + this.getThreads();
      result = result * 59 + this.getQueues();
      result = result * 59 + this.getAlive();
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $dumpPath = this.getDumpPath();
      result = result * 59 + ($dumpPath == null ? 43 : $dumpPath.hashCode());
      return result;
   }

   public String toString() {
      return "ThreadConfig(name=" + this.getName() + ", dumpPath=" + this.getDumpPath() + ", cores=" + this.getCores() + ", threads=" + this.getThreads() + ", queues=" + this.getQueues() + ", alive=" + this.getAlive() + ")";
   }
}
