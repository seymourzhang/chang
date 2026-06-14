package com.chang.util.source.spring.start.config;

public class DbProperties {
   private String url;
   private String username;
   private String password;

   public String getUrl() {
      return this.url;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DbProperties)) {
         return false;
      } else {
         DbProperties other = (DbProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$url = this.getUrl();
               Object other$url = other.getUrl();
               if (this$url == null) {
                  if (other$url == null) {
                     break label47;
                  }
               } else if (this$url.equals(other$url)) {
                  break label47;
               }

               return false;
            }

            Object this$username = this.getUsername();
            Object other$username = other.getUsername();
            if (this$username == null) {
               if (other$username != null) {
                  return false;
               }
            } else if (!this$username.equals(other$username)) {
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
      return other instanceof DbProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $url = this.getUrl();
      result = result * 59 + ($url == null ? 43 : $url.hashCode());
      Object $username = this.getUsername();
      result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      return result;
   }

   public String toString() {
      return "DbProperties(url=" + this.getUrl() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ")";
   }
}
