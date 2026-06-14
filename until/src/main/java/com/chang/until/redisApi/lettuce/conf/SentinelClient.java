package com.chang.until.redisApi.lettuce.conf;

import java.util.List;

public class SentinelClient {
   private String sentinelMasterId;
   List<Node> nodes;
   private String password;
   private int database;

   public String getSentinelMasterId() {
      return this.sentinelMasterId;
   }

   public List<Node> getNodes() {
      return this.nodes;
   }

   public String getPassword() {
      return this.password;
   }

   public int getDatabase() {
      return this.database;
   }

   public void setSentinelMasterId(String sentinelMasterId) {
      this.sentinelMasterId = sentinelMasterId;
   }

   public void setNodes(List<Node> nodes) {
      this.nodes = nodes;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setDatabase(int database) {
      this.database = database;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SentinelClient)) {
         return false;
      } else {
         SentinelClient other = (SentinelClient)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getDatabase() != other.getDatabase()) {
            return false;
         } else {
            label49: {
               Object this$sentinelMasterId = this.getSentinelMasterId();
               Object other$sentinelMasterId = other.getSentinelMasterId();
               if (this$sentinelMasterId == null) {
                  if (other$sentinelMasterId == null) {
                     break label49;
                  }
               } else if (this$sentinelMasterId.equals(other$sentinelMasterId)) {
                  break label49;
               }

               return false;
            }

            Object this$nodes = this.getNodes();
            Object other$nodes = other.getNodes();
            if (this$nodes == null) {
               if (other$nodes != null) {
                  return false;
               }
            } else if (!this$nodes.equals(other$nodes)) {
               return false;
            }

            Object this$password = this.getPassword();
            Object other$password = other.getPassword();
            if (this$password == null) {
               if (other$password != null) {
                  return false;
               }
            } else if (!this$password.equals(other$password)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SentinelClient;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getDatabase();
      Object $sentinelMasterId = this.getSentinelMasterId();
      result = result * 59 + ($sentinelMasterId == null ? 43 : $sentinelMasterId.hashCode());
      Object $nodes = this.getNodes();
      result = result * 59 + ($nodes == null ? 43 : $nodes.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      return result;
   }

   public String toString() {
      return "SentinelClient(sentinelMasterId=" + this.getSentinelMasterId() + ", nodes=" + this.getNodes() + ", password=" + this.getPassword() + ", database=" + this.getDatabase() + ")";
   }
}
