package com.chang.util.mybatis.util;

public class TenantIdContextHandler {
   private static final ThreadLocal<Long> tenantIdthreadLocal = new ThreadLocal();

   public static void setTenantId(Long id) {
      tenantIdthreadLocal.set(id);
   }

   public static Long getTenantId() {
      return (Long)tenantIdthreadLocal.get();
   }

   public static void remove() {
      tenantIdthreadLocal.remove();
   }
}
