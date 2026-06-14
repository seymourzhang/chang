package com.chang.until.redisApi.lettuce.conf;

public class Node {
   private String host;
   private int port;

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Node)) {
         return false;
      } else {
         Node other = (Node)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPort() != other.getPort()) {
            return false;
         } else {
            Object this$host = this.getHost();
            Object other$host = other.getHost();
            if (this$host == null) {
               if (other$host != null) {
                  return false;
               }
            } else if (!this$host.equals(other$host)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Node;
   }

   public int hashCode() {
      int result = 1;
      result = result * 59 + this.getPort();
      Object $host = this.getHost();
      result = result * 59 + ($host == null ? 43 : $host.hashCode());
      return result;
   }

   public String toString() {
      return "Node(host=" + this.getHost() + ", port=" + this.getPort() + ")";
   }
}
