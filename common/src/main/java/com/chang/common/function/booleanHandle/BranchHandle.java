package com.chang.common.function.booleanHandle;

@FunctionalInterface
public interface BranchHandle<T> {
   T trueOrFalseHandle(LfElseSupplier<T> var1, LfElseSupplier<T> var2);
}
