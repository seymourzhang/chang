package com.chang.until.thread.threadlocal;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class InternalThreadLocal<V> {
   private static final int VARIABLES_TO_REMOVE_INDEX = InternalThreadLocalMap.nextVariableIndex();
   private final int index = InternalThreadLocalMap.nextVariableIndex();

   public static void removeAll() {
      InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
      if (threadLocalMap != null) {
         try {
            Object v = threadLocalMap.indexedVariable(VARIABLES_TO_REMOVE_INDEX);
            if (v != null && v != InternalThreadLocalMap.UNSET) {
               Set<InternalThreadLocal<?>> variablesToRemove = (Set)v;
               InternalThreadLocal<?>[] variablesToRemoveArray = (InternalThreadLocal[])variablesToRemove.toArray(new InternalThreadLocal[variablesToRemove.size()]);
               InternalThreadLocal[] var4 = variablesToRemoveArray;
               int var5 = variablesToRemoveArray.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  InternalThreadLocal<?> tlv = var4[var6];
                  tlv.remove(threadLocalMap);
               }
            }
         } finally {
            InternalThreadLocalMap.remove();
         }

      }
   }

   public static int size() {
      InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
      return threadLocalMap == null ? 0 : threadLocalMap.size();
   }

   public static void destroy() {
      InternalThreadLocalMap.destroy();
   }

   private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, InternalThreadLocal<?> variable) {
      Object v = threadLocalMap.indexedVariable(VARIABLES_TO_REMOVE_INDEX);
      Set variablesToRemove;
      if (v != InternalThreadLocalMap.UNSET && v != null) {
         variablesToRemove = (Set)v;
      } else {
         variablesToRemove = Collections.newSetFromMap(new IdentityHashMap());
         threadLocalMap.setIndexedVariable(VARIABLES_TO_REMOVE_INDEX, variablesToRemove);
      }

      variablesToRemove.add(variable);
   }

   public final void remove() {
      this.remove(InternalThreadLocalMap.getIfSet());
   }

   public final void remove(InternalThreadLocalMap threadLocalMap) {
      if (threadLocalMap != null) {
         Object v = threadLocalMap.removeIndexedVariable(this.index);
         removeFromVariablesToRemove(threadLocalMap, this);
         if (v != InternalThreadLocalMap.UNSET) {
            try {
               this.onRemoval((V) v);
            } catch (Exception var4) {
               throw new RuntimeException(var4);
            }
         }

      }
   }

   private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, InternalThreadLocal<?> variable) {
      Object v = threadLocalMap.indexedVariable(VARIABLES_TO_REMOVE_INDEX);
      if (v != InternalThreadLocalMap.UNSET && v != null) {
         Set<InternalThreadLocal<?>> variablesToRemove = (Set)v;
         variablesToRemove.remove(variable);
      }
   }

   public final V get() {
      InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
      Object v = threadLocalMap.indexedVariable(this.index);
      return v != InternalThreadLocalMap.UNSET ? (V) v : this.initialize(threadLocalMap);
   }

   public final void set(V value) {
      if (value != null && value != InternalThreadLocalMap.UNSET) {
         InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
         if (threadLocalMap.setIndexedVariable(this.index, value)) {
            addToVariablesToRemove(threadLocalMap, this);
         }
      } else {
         this.remove();
      }

   }

   private V initialize(InternalThreadLocalMap threadLocalMap) {
      V v = null;

      try {
         v = this.initialValue();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }

      threadLocalMap.setIndexedVariable(this.index, v);
      addToVariablesToRemove(threadLocalMap, this);
      return v;
   }

   protected V initialValue() throws Exception {
      return null;
   }

   protected void onRemoval(V value) throws Exception {
   }
}
