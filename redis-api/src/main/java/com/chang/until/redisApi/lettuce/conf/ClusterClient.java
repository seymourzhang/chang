package com.chang.until.redisApi.lettuce.conf;

import java.util.List;

public class ClusterClient {
   private int maxRedirects;
   private String password;
   private int database;
   List<Node> nodes;

   public int getMaxRedirects() {
      return this.maxRedirects;
   }

   public String getPassword() {
      return this.password;
   }

   public int getDatabase() {
      return this.database;
   }

   public List<Node> getNodes() {
      return this.nodes;
   }

   public void setMaxRedirects(int maxRedirects) {
      this.maxRedirects = maxRedirects;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setDatabase(int database) {
      this.database = database;
   }

   public void setNodes(List<Node> nodes) {
      this.nodes = nodes;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClusterClient)) {
         return false;
      } else {
         ClusterClient other = (ClusterClient)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getMaxRedirects() != other.getMaxRedirects()) {
            return false;
         } else if (this.getDatabase() != other.getDatabase()) {
            return false;
         } else {
            label40: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label40;
                  }
               } else if (this$password.equals(other$password)) {
                  break label40;
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

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ClusterClient;
   }

   public int hashCode() {
      int result = 1;
      result = result * 59 + this.getMaxRedirects();
      result = result * 59 + this.getDatabase();
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $nodes = this.getNodes();
      result = result * 59 + ($nodes == null ? 43 : $nodes.hashCode());
      return result;
   }

   public String toString() {
      return "ClusterClient(maxRedirects=" + this.getMaxRedirects() + ", password=" + this.getPassword() + ", database=" + this.getDatabase() + ", nodes=" + this.getNodes() + ")";
   }
}
