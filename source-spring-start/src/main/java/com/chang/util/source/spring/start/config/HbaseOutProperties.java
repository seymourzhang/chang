package com.chang.util.source.spring.start.config;

import java.util.Map;

public class HbaseOutProperties {
   private String master;
   private String zookeeper;
   private String tableName;
   private String familyName;
   private Boolean isAuth;
   private String user;
   private String krbConfPath;
   private String keytabPath;
   private Map<String, Object> parm;

   public String getMaster() {
      return this.master;
   }

   public String getZookeeper() {
      return this.zookeeper;
   }

   public String getTableName() {
      return this.tableName;
   }

   public String getFamilyName() {
      return this.familyName;
   }

   public Boolean getIsAuth() {
      return this.isAuth;
   }

   public String getUser() {
      return this.user;
   }

   public String getKrbConfPath() {
      return this.krbConfPath;
   }

   public String getKeytabPath() {
      return this.keytabPath;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public void setMaster(String master) {
      this.master = master;
   }

   public void setZookeeper(String zookeeper) {
      this.zookeeper = zookeeper;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public void setFamilyName(String familyName) {
      this.familyName = familyName;
   }

   public void setIsAuth(Boolean isAuth) {
      this.isAuth = isAuth;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public void setKrbConfPath(String krbConfPath) {
      this.krbConfPath = krbConfPath;
   }

   public void setKeytabPath(String keytabPath) {
      this.keytabPath = keytabPath;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HbaseOutProperties)) {
         return false;
      } else {
         HbaseOutProperties other = (HbaseOutProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label119: {
               Object this$isAuth = this.getIsAuth();
               Object other$isAuth = other.getIsAuth();
               if (this$isAuth == null) {
                  if (other$isAuth == null) {
                     break label119;
                  }
               } else if (this$isAuth.equals(other$isAuth)) {
                  break label119;
               }

               return false;
            }

            Object this$master = this.getMaster();
            Object other$master = other.getMaster();
            if (this$master == null) {
               if (other$master != null) {
                  return false;
               }
            } else if (!this$master.equals(other$master)) {
               return false;
            }

            label105: {
               Object this$zookeeper = this.getZookeeper();
               Object other$zookeeper = other.getZookeeper();
               if (this$zookeeper == null) {
                  if (other$zookeeper == null) {
                     break label105;
                  }
               } else if (this$zookeeper.equals(other$zookeeper)) {
                  break label105;
               }

               return false;
            }

            Object this$tableName = this.getTableName();
            Object other$tableName = other.getTableName();
            if (this$tableName == null) {
               if (other$tableName != null) {
                  return false;
               }
            } else if (!this$tableName.equals(other$tableName)) {
               return false;
            }

            label91: {
               Object this$familyName = this.getFamilyName();
               Object other$familyName = other.getFamilyName();
               if (this$familyName == null) {
                  if (other$familyName == null) {
                     break label91;
                  }
               } else if (this$familyName.equals(other$familyName)) {
                  break label91;
               }

               return false;
            }

            Object this$user = this.getUser();
            Object other$user = other.getUser();
            if (this$user == null) {
               if (other$user != null) {
                  return false;
               }
            } else if (!this$user.equals(other$user)) {
               return false;
            }

            label77: {
               Object this$krbConfPath = this.getKrbConfPath();
               Object other$krbConfPath = other.getKrbConfPath();
               if (this$krbConfPath == null) {
                  if (other$krbConfPath == null) {
                     break label77;
                  }
               } else if (this$krbConfPath.equals(other$krbConfPath)) {
                  break label77;
               }

               return false;
            }

            label70: {
               Object this$keytabPath = this.getKeytabPath();
               Object other$keytabPath = other.getKeytabPath();
               if (this$keytabPath == null) {
                  if (other$keytabPath == null) {
                     break label70;
                  }
               } else if (this$keytabPath.equals(other$keytabPath)) {
                  break label70;
               }

               return false;
            }

            Object this$parm = this.getParm();
            Object other$parm = other.getParm();
            if (this$parm == null) {
               if (other$parm != null) {
                  return false;
               }
            } else if (!this$parm.equals(other$parm)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HbaseOutProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $isAuth = this.getIsAuth();
      result = result * 59 + ($isAuth == null ? 43 : $isAuth.hashCode());
      Object $master = this.getMaster();
      result = result * 59 + ($master == null ? 43 : $master.hashCode());
      Object $zookeeper = this.getZookeeper();
      result = result * 59 + ($zookeeper == null ? 43 : $zookeeper.hashCode());
      Object $tableName = this.getTableName();
      result = result * 59 + ($tableName == null ? 43 : $tableName.hashCode());
      Object $familyName = this.getFamilyName();
      result = result * 59 + ($familyName == null ? 43 : $familyName.hashCode());
      Object $user = this.getUser();
      result = result * 59 + ($user == null ? 43 : $user.hashCode());
      Object $krbConfPath = this.getKrbConfPath();
      result = result * 59 + ($krbConfPath == null ? 43 : $krbConfPath.hashCode());
      Object $keytabPath = this.getKeytabPath();
      result = result * 59 + ($keytabPath == null ? 43 : $keytabPath.hashCode());
      Object $parm = this.getParm();
      result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
      return result;
   }

   public String toString() {
      return "HbaseOutProperties(master=" + this.getMaster() + ", zookeeper=" + this.getZookeeper() + ", tableName=" + this.getTableName() + ", familyName=" + this.getFamilyName() + ", isAuth=" + this.getIsAuth() + ", user=" + this.getUser() + ", krbConfPath=" + this.getKrbConfPath() + ", keytabPath=" + this.getKeytabPath() + ", parm=" + this.getParm() + ")";
   }
}
