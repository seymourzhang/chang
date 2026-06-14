package com.chang.util.mybatis.config;

import java.util.Arrays;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
   prefix = "wg.mybatis.plus.config"
)
public class MybatisPlusProperties {
   private Boolean openPage = true;
   private Boolean openTenant = false;
   private Boolean openDynamicTable = false;
   private Boolean openOptimisticLocker = false;
   private Boolean openIllegalSql = false;
   private Boolean openBlockAttack = false;
   private String tenantId = "tenant_id";
   private String excludeTenantTable;
   private Boolean enableFill;
   private Boolean enableSqlInjector;
   private Boolean enableBaseDdl;
   private String sqlDdlPaths;
   private String[] innerInterceptorBeanNames;
   private Boolean oldPageInfo = false;

   public Boolean getOpenPage() {
      return this.openPage;
   }

   public Boolean getOpenTenant() {
      return this.openTenant;
   }

   public Boolean getOpenDynamicTable() {
      return this.openDynamicTable;
   }

   public Boolean getOpenOptimisticLocker() {
      return this.openOptimisticLocker;
   }

   public Boolean getOpenIllegalSql() {
      return this.openIllegalSql;
   }

   public Boolean getOpenBlockAttack() {
      return this.openBlockAttack;
   }

   public String getTenantId() {
      return this.tenantId;
   }

   public String getExcludeTenantTable() {
      return this.excludeTenantTable;
   }

   public Boolean getEnableFill() {
      return this.enableFill;
   }

   public Boolean getEnableSqlInjector() {
      return this.enableSqlInjector;
   }

   public Boolean getEnableBaseDdl() {
      return this.enableBaseDdl;
   }

   public String getSqlDdlPaths() {
      return this.sqlDdlPaths;
   }

   public String[] getInnerInterceptorBeanNames() {
      return this.innerInterceptorBeanNames;
   }

   public Boolean getOldPageInfo() {
      return this.oldPageInfo;
   }

   public void setOpenPage(final Boolean openPage) {
      this.openPage = openPage;
   }

   public void setOpenTenant(final Boolean openTenant) {
      this.openTenant = openTenant;
   }

   public void setOpenDynamicTable(final Boolean openDynamicTable) {
      this.openDynamicTable = openDynamicTable;
   }

   public void setOpenOptimisticLocker(final Boolean openOptimisticLocker) {
      this.openOptimisticLocker = openOptimisticLocker;
   }

   public void setOpenIllegalSql(final Boolean openIllegalSql) {
      this.openIllegalSql = openIllegalSql;
   }

   public void setOpenBlockAttack(final Boolean openBlockAttack) {
      this.openBlockAttack = openBlockAttack;
   }

   public void setTenantId(final String tenantId) {
      this.tenantId = tenantId;
   }

   public void setExcludeTenantTable(final String excludeTenantTable) {
      this.excludeTenantTable = excludeTenantTable;
   }

   public void setEnableFill(final Boolean enableFill) {
      this.enableFill = enableFill;
   }

   public void setEnableSqlInjector(final Boolean enableSqlInjector) {
      this.enableSqlInjector = enableSqlInjector;
   }

   public void setEnableBaseDdl(final Boolean enableBaseDdl) {
      this.enableBaseDdl = enableBaseDdl;
   }

   public void setSqlDdlPaths(final String sqlDdlPaths) {
      this.sqlDdlPaths = sqlDdlPaths;
   }

   public void setInnerInterceptorBeanNames(final String[] innerInterceptorBeanNames) {
      this.innerInterceptorBeanNames = innerInterceptorBeanNames;
   }

   public void setOldPageInfo(final Boolean oldPageInfo) {
      this.oldPageInfo = oldPageInfo;
   }

   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MybatisPlusProperties)) {
         return false;
      } else {
         MybatisPlusProperties other = (MybatisPlusProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label171: {
               Object this$openPage = this.getOpenPage();
               Object other$openPage = other.getOpenPage();
               if (this$openPage == null) {
                  if (other$openPage == null) {
                     break label171;
                  }
               } else if (this$openPage.equals(other$openPage)) {
                  break label171;
               }

               return false;
            }

            Object this$openTenant = this.getOpenTenant();
            Object other$openTenant = other.getOpenTenant();
            if (this$openTenant == null) {
               if (other$openTenant != null) {
                  return false;
               }
            } else if (!this$openTenant.equals(other$openTenant)) {
               return false;
            }

            Object this$openDynamicTable = this.getOpenDynamicTable();
            Object other$openDynamicTable = other.getOpenDynamicTable();
            if (this$openDynamicTable == null) {
               if (other$openDynamicTable != null) {
                  return false;
               }
            } else if (!this$openDynamicTable.equals(other$openDynamicTable)) {
               return false;
            }

            label150: {
               Object this$openOptimisticLocker = this.getOpenOptimisticLocker();
               Object other$openOptimisticLocker = other.getOpenOptimisticLocker();
               if (this$openOptimisticLocker == null) {
                  if (other$openOptimisticLocker == null) {
                     break label150;
                  }
               } else if (this$openOptimisticLocker.equals(other$openOptimisticLocker)) {
                  break label150;
               }

               return false;
            }

            label143: {
               Object this$openIllegalSql = this.getOpenIllegalSql();
               Object other$openIllegalSql = other.getOpenIllegalSql();
               if (this$openIllegalSql == null) {
                  if (other$openIllegalSql == null) {
                     break label143;
                  }
               } else if (this$openIllegalSql.equals(other$openIllegalSql)) {
                  break label143;
               }

               return false;
            }

            label136: {
               Object this$openBlockAttack = this.getOpenBlockAttack();
               Object other$openBlockAttack = other.getOpenBlockAttack();
               if (this$openBlockAttack == null) {
                  if (other$openBlockAttack == null) {
                     break label136;
                  }
               } else if (this$openBlockAttack.equals(other$openBlockAttack)) {
                  break label136;
               }

               return false;
            }

            Object this$enableFill = this.getEnableFill();
            Object other$enableFill = other.getEnableFill();
            if (this$enableFill == null) {
               if (other$enableFill != null) {
                  return false;
               }
            } else if (!this$enableFill.equals(other$enableFill)) {
               return false;
            }

            label122: {
               Object this$enableSqlInjector = this.getEnableSqlInjector();
               Object other$enableSqlInjector = other.getEnableSqlInjector();
               if (this$enableSqlInjector == null) {
                  if (other$enableSqlInjector == null) {
                     break label122;
                  }
               } else if (this$enableSqlInjector.equals(other$enableSqlInjector)) {
                  break label122;
               }

               return false;
            }

            Object this$enableBaseDdl = this.getEnableBaseDdl();
            Object other$enableBaseDdl = other.getEnableBaseDdl();
            if (this$enableBaseDdl == null) {
               if (other$enableBaseDdl != null) {
                  return false;
               }
            } else if (!this$enableBaseDdl.equals(other$enableBaseDdl)) {
               return false;
            }

            label108: {
               Object this$oldPageInfo = this.getOldPageInfo();
               Object other$oldPageInfo = other.getOldPageInfo();
               if (this$oldPageInfo == null) {
                  if (other$oldPageInfo == null) {
                     break label108;
                  }
               } else if (this$oldPageInfo.equals(other$oldPageInfo)) {
                  break label108;
               }

               return false;
            }

            Object this$tenantId = this.getTenantId();
            Object other$tenantId = other.getTenantId();
            if (this$tenantId == null) {
               if (other$tenantId != null) {
                  return false;
               }
            } else if (!this$tenantId.equals(other$tenantId)) {
               return false;
            }

            Object this$excludeTenantTable = this.getExcludeTenantTable();
            Object other$excludeTenantTable = other.getExcludeTenantTable();
            if (this$excludeTenantTable == null) {
               if (other$excludeTenantTable != null) {
                  return false;
               }
            } else if (!this$excludeTenantTable.equals(other$excludeTenantTable)) {
               return false;
            }

            Object this$sqlDdlPaths = this.getSqlDdlPaths();
            Object other$sqlDdlPaths = other.getSqlDdlPaths();
            if (this$sqlDdlPaths == null) {
               if (other$sqlDdlPaths != null) {
                  return false;
               }
            } else if (!this$sqlDdlPaths.equals(other$sqlDdlPaths)) {
               return false;
            }

            if (!Arrays.deepEquals(this.getInnerInterceptorBeanNames(), other.getInnerInterceptorBeanNames())) {
               return false;
            } else {
               return true;
            }
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return other instanceof MybatisPlusProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $openPage = this.getOpenPage();
      result = result * 59 + ($openPage == null ? 43 : $openPage.hashCode());
      Object $openTenant = this.getOpenTenant();
      result = result * 59 + ($openTenant == null ? 43 : $openTenant.hashCode());
      Object $openDynamicTable = this.getOpenDynamicTable();
      result = result * 59 + ($openDynamicTable == null ? 43 : $openDynamicTable.hashCode());
      Object $openOptimisticLocker = this.getOpenOptimisticLocker();
      result = result * 59 + ($openOptimisticLocker == null ? 43 : $openOptimisticLocker.hashCode());
      Object $openIllegalSql = this.getOpenIllegalSql();
      result = result * 59 + ($openIllegalSql == null ? 43 : $openIllegalSql.hashCode());
      Object $openBlockAttack = this.getOpenBlockAttack();
      result = result * 59 + ($openBlockAttack == null ? 43 : $openBlockAttack.hashCode());
      Object $enableFill = this.getEnableFill();
      result = result * 59 + ($enableFill == null ? 43 : $enableFill.hashCode());
      Object $enableSqlInjector = this.getEnableSqlInjector();
      result = result * 59 + ($enableSqlInjector == null ? 43 : $enableSqlInjector.hashCode());
      Object $enableBaseDdl = this.getEnableBaseDdl();
      result = result * 59 + ($enableBaseDdl == null ? 43 : $enableBaseDdl.hashCode());
      Object $oldPageInfo = this.getOldPageInfo();
      result = result * 59 + ($oldPageInfo == null ? 43 : $oldPageInfo.hashCode());
      Object $tenantId = this.getTenantId();
      result = result * 59 + ($tenantId == null ? 43 : $tenantId.hashCode());
      Object $excludeTenantTable = this.getExcludeTenantTable();
      result = result * 59 + ($excludeTenantTable == null ? 43 : $excludeTenantTable.hashCode());
      Object $sqlDdlPaths = this.getSqlDdlPaths();
      result = result * 59 + ($sqlDdlPaths == null ? 43 : $sqlDdlPaths.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getInnerInterceptorBeanNames());
      return result;
   }

   public String toString() {
      return "MybatisPlusProperties(openPage=" + this.getOpenPage() + ", openTenant=" + this.getOpenTenant() + ", openDynamicTable=" + this.getOpenDynamicTable() + ", openOptimisticLocker=" + this.getOpenOptimisticLocker() + ", openIllegalSql=" + this.getOpenIllegalSql() + ", openBlockAttack=" + this.getOpenBlockAttack() + ", tenantId=" + this.getTenantId() + ", excludeTenantTable=" + this.getExcludeTenantTable() + ", enableFill=" + this.getEnableFill() + ", enableSqlInjector=" + this.getEnableSqlInjector() + ", enableBaseDdl=" + this.getEnableBaseDdl() + ", sqlDdlPaths=" + this.getSqlDdlPaths() + ", innerInterceptorBeanNames=" + Arrays.deepToString(this.getInnerInterceptorBeanNames()) + ", oldPageInfo=" + this.getOldPageInfo() + ")";
   }
}
