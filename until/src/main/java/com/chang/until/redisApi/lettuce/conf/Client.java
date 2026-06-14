package com.chang.until.redisApi.lettuce.conf;

public class Client {
   private String host;
   private int port;
   private String password;
   private int database;

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getPassword() {
      return this.password;
   }

   public int getDatabase() {
      return this.database;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public void setPort(int port) {
      this.port = port;
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
      } else if (!(o instanceof Client)) {
         return false;
      } else {
         Client other = (Client)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPort() != other.getPort()) {
            return false;
         } else if (this.getDatabase() != other.getDatabase()) {
            return false;
         } else {
            label40: {
               Object this$host = this.getHost();
               Object other$host = other.getHost();
               if (this$host == null) {
                  if (other$host == null) {
                     break label40;
                  }
               } else if (this$host.equals(other$host)) {
                  break label40;
               }

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
      return other instanceof Client;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getPort();
      result = result * 59 + this.getDatabase();
      Object $host = this.getHost();
      result = result * 59 + ($host == null ? 43 : $host.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      return result;
   }

   public String toString() {
      return "Client(host=" + this.getHost() + ", port=" + this.getPort() + ", password=" + this.getPassword() + ", database=" + this.getDatabase() + ")";
   }
}
