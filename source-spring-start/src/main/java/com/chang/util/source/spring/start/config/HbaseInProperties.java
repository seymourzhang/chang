package com.chang.util.source.spring.start.config;

import com.chang.util.source.common.HbaseFindType;
import com.chang.util.source.common.TaskModeType;

import java.util.HashMap;
import java.util.Map;

public class HbaseInProperties {
   private String master;
   private String zookeeper;
   private String tableName;
   private String familyName;
   private String clazzName;
   private HbaseFindType hbaseFindType;
   private String startRow;
   private String endRow;
   private String rowKeyPrefix;
   private String time;
   private TaskModeType taskModeType;
   private Boolean isAuth;
   private String user;
   private String krbConfPath;
   private String keytabPath;
   private Map<String, Object> parm = new HashMap();

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

   public String getClazzName() {
      return this.clazzName;
   }

   public HbaseFindType getHbaseFindType() {
      return this.hbaseFindType;
   }

   public String getStartRow() {
      return this.startRow;
   }

   public String getEndRow() {
      return this.endRow;
   }

   public String getRowKeyPrefix() {
      return this.rowKeyPrefix;
   }

   public String getTime() {
      return this.time;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
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

   public void setClazzName(String clazzName) {
      this.clazzName = clazzName;
   }

   public void setHbaseFindType(HbaseFindType hbaseFindType) {
      this.hbaseFindType = hbaseFindType;
   }

   public void setStartRow(String startRow) {
      this.startRow = startRow;
   }

   public void setEndRow(String endRow) {
      this.endRow = endRow;
   }

   public void setRowKeyPrefix(String rowKeyPrefix) {
      this.rowKeyPrefix = rowKeyPrefix;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public void setTaskModeType(TaskModeType taskModeType) {
      this.taskModeType = taskModeType;
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
      } else if (!(o instanceof HbaseInProperties)) {
         return false;
      } else {
         HbaseInProperties other = (HbaseInProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label203: {
               Object this$isAuth = this.getIsAuth();
               Object other$isAuth = other.getIsAuth();
               if (this$isAuth == null) {
                  if (other$isAuth == null) {
                     break label203;
                  }
               } else if (this$isAuth.equals(other$isAuth)) {
                  break label203;
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

            Object this$zookeeper = this.getZookeeper();
            Object other$zookeeper = other.getZookeeper();
            if (this$zookeeper == null) {
               if (other$zookeeper != null) {
                  return false;
               }
            } else if (!this$zookeeper.equals(other$zookeeper)) {
               return false;
            }

            label182: {
               Object this$tableName = this.getTableName();
               Object other$tableName = other.getTableName();
               if (this$tableName == null) {
                  if (other$tableName == null) {
                     break label182;
                  }
               } else if (this$tableName.equals(other$tableName)) {
                  break label182;
               }

               return false;
            }

            label175: {
               Object this$familyName = this.getFamilyName();
               Object other$familyName = other.getFamilyName();
               if (this$familyName == null) {
                  if (other$familyName == null) {
                     break label175;
                  }
               } else if (this$familyName.equals(other$familyName)) {
                  break label175;
               }

               return false;
            }

            label168: {
               Object this$clazzName = this.getClazzName();
               Object other$clazzName = other.getClazzName();
               if (this$clazzName == null) {
                  if (other$clazzName == null) {
                     break label168;
                  }
               } else if (this$clazzName.equals(other$clazzName)) {
                  break label168;
               }

               return false;
            }

            Object this$hbaseFindType = this.getHbaseFindType();
            Object other$hbaseFindType = other.getHbaseFindType();
            if (this$hbaseFindType == null) {
               if (other$hbaseFindType != null) {
                  return false;
               }
            } else if (!this$hbaseFindType.equals(other$hbaseFindType)) {
               return false;
            }

            label154: {
               Object this$startRow = this.getStartRow();
               Object other$startRow = other.getStartRow();
               if (this$startRow == null) {
                  if (other$startRow == null) {
                     break label154;
                  }
               } else if (this$startRow.equals(other$startRow)) {
                  break label154;
               }

               return false;
            }

            Object this$endRow = this.getEndRow();
            Object other$endRow = other.getEndRow();
            if (this$endRow == null) {
               if (other$endRow != null) {
                  return false;
               }
            } else if (!this$endRow.equals(other$endRow)) {
               return false;
            }

            label140: {
               Object this$rowKeyPrefix = this.getRowKeyPrefix();
               Object other$rowKeyPrefix = other.getRowKeyPrefix();
               if (this$rowKeyPrefix == null) {
                  if (other$rowKeyPrefix == null) {
                     break label140;
                  }
               } else if (this$rowKeyPrefix.equals(other$rowKeyPrefix)) {
                  break label140;
               }

               return false;
            }

            Object this$time = this.getTime();
            Object other$time = other.getTime();
            if (this$time == null) {
               if (other$time != null) {
                  return false;
               }
            } else if (!this$time.equals(other$time)) {
               return false;
            }

            Object this$taskModeType = this.getTaskModeType();
            Object other$taskModeType = other.getTaskModeType();
            if (this$taskModeType == null) {
               if (other$taskModeType != null) {
                  return false;
               }
            } else if (!this$taskModeType.equals(other$taskModeType)) {
               return false;
            }

            label119: {
               Object this$user = this.getUser();
               Object other$user = other.getUser();
               if (this$user == null) {
                  if (other$user == null) {
                     break label119;
                  }
               } else if (this$user.equals(other$user)) {
                  break label119;
               }

               return false;
            }

            label112: {
               Object this$krbConfPath = this.getKrbConfPath();
               Object other$krbConfPath = other.getKrbConfPath();
               if (this$krbConfPath == null) {
                  if (other$krbConfPath == null) {
                     break label112;
                  }
               } else if (this$krbConfPath.equals(other$krbConfPath)) {
                  break label112;
               }

               return false;
            }

            Object this$keytabPath = this.getKeytabPath();
            Object other$keytabPath = other.getKeytabPath();
            if (this$keytabPath == null) {
               if (other$keytabPath != null) {
                  return false;
               }
            } else if (!this$keytabPath.equals(other$keytabPath)) {
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
      return other instanceof HbaseInProperties;
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
      Object $clazzName = this.getClazzName();
      result = result * 59 + ($clazzName == null ? 43 : $clazzName.hashCode());
      Object $hbaseFindType = this.getHbaseFindType();
      result = result * 59 + ($hbaseFindType == null ? 43 : $hbaseFindType.hashCode());
      Object $startRow = this.getStartRow();
      result = result * 59 + ($startRow == null ? 43 : $startRow.hashCode());
      Object $endRow = this.getEndRow();
      result = result * 59 + ($endRow == null ? 43 : $endRow.hashCode());
      Object $rowKeyPrefix = this.getRowKeyPrefix();
      result = result * 59 + ($rowKeyPrefix == null ? 43 : $rowKeyPrefix.hashCode());
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      Object $taskModeType = this.getTaskModeType();
      result = result * 59 + ($taskModeType == null ? 43 : $taskModeType.hashCode());
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
      return "HbaseInProperties(master=" + this.getMaster() + ", zookeeper=" + this.getZookeeper() + ", tableName=" + this.getTableName() + ", familyName=" + this.getFamilyName() + ", clazzName=" + this.getClazzName() + ", hbaseFindType=" + this.getHbaseFindType() + ", startRow=" + this.getStartRow() + ", endRow=" + this.getEndRow() + ", rowKeyPrefix=" + this.getRowKeyPrefix() + ", time=" + this.getTime() + ", taskModeType=" + this.getTaskModeType() + ", isAuth=" + this.getIsAuth() + ", user=" + this.getUser() + ", krbConfPath=" + this.getKrbConfPath() + ", keytabPath=" + this.getKeytabPath() + ", parm=" + this.getParm() + ")";
   }
}
