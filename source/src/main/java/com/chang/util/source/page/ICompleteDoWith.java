package com.chang.util.source.page;

public interface ICompleteDoWith<T> {
   void completeDoWith(String var1, T[] var2);

   void expiredDoWith(String var1, T[] var2);
}
