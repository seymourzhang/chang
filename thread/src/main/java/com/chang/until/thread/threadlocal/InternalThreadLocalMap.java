package com.chang.until.thread.threadlocal;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class InternalThreadLocalMap {
   private Object[] indexedVariables = newIndexedVariableTable();
   private static ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal();
   private static final AtomicInteger NEXT_INDEX = new AtomicInteger();
   public static final Object UNSET = new Object();

   public static InternalThreadLocalMap getIfSet() {
      Thread thread = Thread.currentThread();
      return thread instanceof InternalThread ? ((InternalThread)thread).threadLocalMap() : (InternalThreadLocalMap)slowThreadLocalMap.get();
   }

   public static InternalThreadLocalMap get() {
      Thread thread = Thread.currentThread();
      return thread instanceof InternalThread ? fastGet((InternalThread)thread) : slowGet();
   }

   public static void remove() {
      Thread thread = Thread.currentThread();
      if (thread instanceof InternalThread) {
         ((InternalThread)thread).setThreadLocalMap((InternalThreadLocalMap)null);
      } else {
         slowThreadLocalMap.remove();
      }

   }

   public static void destroy() {
      slowThreadLocalMap = null;
   }

   public static int nextVariableIndex() {
      int index = NEXT_INDEX.getAndIncrement();
      if (index < 0) {
         NEXT_INDEX.decrementAndGet();
         throw new IllegalStateException("Too many thread-local indexed variables");
      } else {
         return index;
      }
   }

   public static int lastVariableIndex() {
      return NEXT_INDEX.get() - 1;
   }

   private static Object[] newIndexedVariableTable() {
      Object[] array = new Object[32];
      Arrays.fill(array, UNSET);
      return array;
   }

   private InternalThreadLocalMap() {
   }

   public Object indexedVariable(int index) {
      Object[] lookup = this.indexedVariables;
      return index < lookup.length ? lookup[index] : UNSET;
   }

   private void expandIndexedVariableTableAndSet(int index, Object value) {
      Object[] oldArray = this.indexedVariables;
      int oldCapacity = oldArray.length;
      int newCapacity = index | index >>> 1;
      newCapacity |= newCapacity >>> 2;
      newCapacity |= newCapacity >>> 4;
      newCapacity |= newCapacity >>> 8;
      newCapacity |= newCapacity >>> 16;
      ++newCapacity;
      Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
      Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
      newArray[index] = value;
      this.indexedVariables = newArray;
   }

   public boolean setIndexedVariable(int index, Object value) {
      Object[] lookup = this.indexedVariables;
      if (index < lookup.length) {
         Object oldValue = lookup[index];
         lookup[index] = value;
         return oldValue == UNSET;
      } else {
         this.expandIndexedVariableTableAndSet(index, value);
         return true;
      }
   }

   public int size() {
      int count = 0;
      Object[] var2 = this.indexedVariables;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object o = var2[var4];
         if (o != UNSET) {
            ++count;
         }
      }

      return count - 1;
   }

   public Object removeIndexedVariable(int index) {
      Object[] lookup = this.indexedVariables;
      if (index < lookup.length) {
         Object v = lookup[index];
         lookup[index] = UNSET;
         return v;
      } else {
         return UNSET;
      }
   }

   private static InternalThreadLocalMap fastGet(InternalThread thread) {
      InternalThreadLocalMap threadLocalMap = thread.threadLocalMap();
      if (threadLocalMap == null) {
         thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());
      }

      return threadLocalMap;
   }

   private static InternalThreadLocalMap slowGet() {
      ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = InternalThreadLocalMap.slowThreadLocalMap;
      InternalThreadLocalMap ret = (InternalThreadLocalMap)slowThreadLocalMap.get();
      if (ret == null) {
         ret = new InternalThreadLocalMap();
         slowThreadLocalMap.set(ret);
      }

      return ret;
   }
}
