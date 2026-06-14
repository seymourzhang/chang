package com.chang.util.source.page;

import com.chang.common.CommUtils;
import com.chang.common.cache.Delete;

public class RemoveDoWith<T> implements Delete<String, T[]> {
   private final ICompleteDoWith<T> doWith;
   private final Class<T> aClass;

   public RemoveDoWith(ICompleteDoWith<T> doWith, Class<T> tClass) {
      this.doWith = doWith;
      this.aClass = tClass;
   }

   public void expired(String key, T[] value) {
      if (PackageTools.isComplete(value)) {
         this.doWith.completeDoWith(key, CommUtils.convertArray(this.aClass, value));
      } else {
         this.doWith.expiredDoWith(key, CommUtils.convertArray(this.aClass, value));
      }

   }

   public void explicit(String key, T[] value) {
   }

   public void replaced(String key, T[] value) {
   }

   public void size(String key, T[] value) {
   }
}
