package com.chang.until.thread.model;

import com.google.common.base.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ReduceTask<F, T> implements Function<F, T> {
   public @Nullable T apply(@Nullable F f) {
      return this.task(f);
   }

   protected abstract T task(@Nullable F var1);
}
