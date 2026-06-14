package com.chang.until.timeTaskApi.lib;

import cn.hutool.core.lang.Assert;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
   private static final int DEFAULT_INITIAL_CAPACITY = 16;
   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
   private static final ReferenceType DEFAULT_REFERENCE_TYPE;
   private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
   private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
   private final Segment[] segments;
   private final float loadFactor;
   private final ReferenceType referenceType;
   private final int shift;
   private volatile Set<Map.Entry<K, V>> entrySet;

   public ConcurrentReferenceHashMap() {
      this(16, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
   }

   public ConcurrentReferenceHashMap(int initialCapacity) {
      this(initialCapacity, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
      this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
      this(initialCapacity, 0.75F, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
      this(initialCapacity, 0.75F, 16, referenceType);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
      this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
      Assert.isTrue(initialCapacity >= 0, "Initial capacity must not be negative", new Object[0]);
      Assert.isTrue(loadFactor > 0.0F, "Load factor must be positive", new Object[0]);
      Assert.isTrue(concurrencyLevel > 0, "Concurrency level must be positive", new Object[0]);
      Assert.notNull(referenceType, "Reference type must not be null", new Object[0]);
      this.loadFactor = loadFactor;
      this.shift = calculateShift(concurrencyLevel, 65536);
      int size = 1 << this.shift;
      this.referenceType = referenceType;
      int roundedUpSegmentCapacity = (int)(((long)(initialCapacity + size) - 1L) / (long)size);
      int initialSize = 1 << calculateShift(roundedUpSegmentCapacity, 1073741824);
      Segment[] segments = (Segment[])((Segment[])Array.newInstance(Segment.class, size));
      int resizeThreshold = (int)((float)initialSize * this.getLoadFactor());

      for(int i = 0; i < segments.length; ++i) {
         segments[i] = new Segment(initialSize, resizeThreshold);
      }

      this.segments = segments;
   }

   private static boolean arrayEquals(Object o1, Object o2) {
      if (o1 instanceof Object[] && o2 instanceof Object[]) {
         return Arrays.equals((Object[])((Object[])o1), (Object[])((Object[])o2));
      } else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
         return Arrays.equals((boolean[])((boolean[])o1), (boolean[])((boolean[])o2));
      } else if (o1 instanceof byte[] && o2 instanceof byte[]) {
         return Arrays.equals((byte[])((byte[])o1), (byte[])((byte[])o2));
      } else if (o1 instanceof char[] && o2 instanceof char[]) {
         return Arrays.equals((char[])((char[])o1), (char[])((char[])o2));
      } else if (o1 instanceof double[] && o2 instanceof double[]) {
         return Arrays.equals((double[])((double[])o1), (double[])((double[])o2));
      } else if (o1 instanceof float[] && o2 instanceof float[]) {
         return Arrays.equals((float[])((float[])o1), (float[])((float[])o2));
      } else if (o1 instanceof int[] && o2 instanceof int[]) {
         return Arrays.equals((int[])((int[])o1), (int[])((int[])o2));
      } else if (o1 instanceof long[] && o2 instanceof long[]) {
         return Arrays.equals((long[])((long[])o1), (long[])((long[])o2));
      } else {
         return o1 instanceof short[] && o2 instanceof short[] ? Arrays.equals((short[])((short[])o1), (short[])((short[])o2)) : false;
      }
   }

   public static boolean nullSafeEquals(Object o1, Object o2) {
      if (o1 == o2) {
         return true;
      } else if (o1 != null && o2 != null) {
         if (o1.equals(o2)) {
            return true;
         } else {
            return o1.getClass().isArray() && o2.getClass().isArray() ? arrayEquals(o1, o2) : false;
         }
      } else {
         return false;
      }
   }

   protected static int calculateShift(int minimumValue, int maximumValue) {
      int shift = 0;

      for(int value = 1; value < minimumValue && value < maximumValue; ++shift) {
         value <<= 1;
      }

      return shift;
   }

   protected final float getLoadFactor() {
      return this.loadFactor;
   }

   protected final int getSegmentsSize() {
      return this.segments.length;
   }

   protected final Segment getSegment(int index) {
      return this.segments[index];
   }

   protected ReferenceManager createReferenceManager() {
      return new ReferenceManager();
   }

   protected int getHash(Object o) {
      int hash = o != null ? o.hashCode() : 0;
      hash += hash << 15 ^ -12931;
      hash ^= hash >>> 10;
      hash += hash << 3;
      hash ^= hash >>> 6;
      hash += (hash << 2) + (hash << 14);
      hash ^= hash >>> 16;
      return hash;
   }

   public V get(Object key) {
      Reference<K, V> ref = this.getReference(key, Restructure.WHEN_NECESSARY);
      Entry<K, V> entry = ref != null ? ref.get() : null;
      return entry != null ? entry.getValue() : null;
   }

   public V getOrDefault(Object key, V defaultValue) {
      Reference<K, V> ref = this.getReference(key, Restructure.WHEN_NECESSARY);
      Entry<K, V> entry = ref != null ? ref.get() : null;
      return entry != null ? entry.getValue() : defaultValue;
   }

   public boolean containsKey(Object key) {
      Reference<K, V> ref = this.getReference(key, Restructure.WHEN_NECESSARY);
      Entry<K, V> entry = ref != null ? ref.get() : null;
      return entry != null && nullSafeEquals(entry.getKey(), key);
   }

   protected final Reference<K, V> getReference(Object key, Restructure restructure) {
      int hash = this.getHash(key);
      return this.getSegmentForHash(hash).getReference(key, hash, restructure);
   }

   public V put(K key, V value) {
      return this.put(key, value, true);
   }

   public V putIfAbsent(K key, V value) {
      return this.put(key, value, false);
   }

   private V put(K key, final V value, final boolean overwriteExisting) {
      return this.doTask(key, new Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE}) {
         protected V execute(Reference<K, V> ref, Entry<K, V> entry, Entries<V> entries) {
            if (entry != null) {
               V oldValue = entry.getValue();
               if (overwriteExisting) {
                  entry.setValue(value);
               }

               return oldValue;
            } else {
               Assert.state(entries != null, "No entries segment", new Object[0]);
               entries.add(value);
               return null;
            }
         }
      });
   }

   public V remove(Object key) {
      return this.doTask(key, new Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY}) {
         protected V execute(Reference<K, V> ref, Entry<K, V> entry) {
            if (entry != null) {
               if (ref != null) {
                  ref.release();
               }

               return entry.value;
            } else {
               return null;
            }
         }
      });
   }

   public boolean remove(Object key, final Object value) {
      Boolean result = (Boolean)this.doTask(key, new Task<Boolean>(new TaskOption[]{TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY}) {
         protected Boolean execute(Reference<K, V> ref, Entry<K, V> entry) {
            if (entry != null && ConcurrentReferenceHashMap.nullSafeEquals(entry.getValue(), value)) {
               if (ref != null) {
                  ref.release();
               }

               return true;
            } else {
               return false;
            }
         }
      });
      return Boolean.TRUE.equals(result);
   }

   public boolean replace(K key, final V oldValue, final V newValue) {
      Boolean result = (Boolean)this.doTask(key, new Task<Boolean>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY}) {
         protected Boolean execute(Reference<K, V> ref, Entry<K, V> entry) {
            if (entry != null && ConcurrentReferenceHashMap.nullSafeEquals(entry.getValue(), oldValue)) {
               entry.setValue(newValue);
               return true;
            } else {
               return false;
            }
         }
      });
      return Boolean.TRUE.equals(result);
   }

   public V replace(K key, final V value) {
      return this.doTask(key, new Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY}) {
         protected V execute(Reference<K, V> ref, Entry<K, V> entry) {
            if (entry != null) {
               V oldValue = entry.getValue();
               entry.setValue(value);
               return oldValue;
            } else {
               return null;
            }
         }
      });
   }

   public void clear() {
      Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Segment segment = var1[var3];
         segment.clear();
      }

   }

   public void purgeUnreferencedEntries() {
      Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Segment segment = var1[var3];
         segment.restructureIfNecessary(false);
      }

   }

   public int size() {
      int size = 0;
      Segment[] var2 = this.segments;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Segment segment = var2[var4];
         size += segment.getCount();
      }

      return size;
   }

   public boolean isEmpty() {
      Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Segment segment = var1[var3];
         if (segment.getCount() > 0) {
            return false;
         }
      }

      return true;
   }

   public Set<Map.Entry<K, V>> entrySet() {
      Set<Map.Entry<K, V>> entrySet = this.entrySet;
      if (entrySet == null) {
         entrySet = new EntrySet();
         this.entrySet = (Set)entrySet;
      }

      return (Set)entrySet;
   }

   private <T> T doTask(Object key, Task<T> task) {
      int hash = this.getHash(key);
      return this.getSegmentForHash(hash).doTask(hash, key, task);
   }

   private Segment getSegmentForHash(int hash) {
      return this.segments[hash >>> 32 - this.shift & this.segments.length - 1];
   }

   static {
      DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
   }

   protected class ReferenceManager {
      private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue();

      public Reference<K, V> createReference(Entry<K, V> entry, int hash, Reference<K, V> next) {
         return (Reference)(ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK ? new WeakEntryReference(entry, hash, next, this.queue) : new SoftEntryReference(entry, hash, next, this.queue));
      }

      public Reference<K, V> pollForPurge() {
         return (Reference)this.queue.poll();
      }
   }

   private class EntryIterator implements Iterator<Map.Entry<K, V>> {
      private int segmentIndex;
      private int referenceIndex;
      private Reference<K, V>[] references;
      private Reference<K, V> reference;
      private Entry<K, V> next;
      private Entry<K, V> last;

      public EntryIterator() {
         this.moveToNextSegment();
      }

      public boolean hasNext() {
         this.getNextIfNecessary();
         return this.next != null;
      }

      public Entry<K, V> next() {
         this.getNextIfNecessary();
         if (this.next == null) {
            throw new NoSuchElementException();
         } else {
            this.last = this.next;
            this.next = null;
            return this.last;
         }
      }

      private void getNextIfNecessary() {
         while(this.next == null) {
            this.moveToNextReference();
            if (this.reference == null) {
               return;
            }

            this.next = this.reference.get();
         }

      }

      private void moveToNextReference() {
         if (this.reference != null) {
            this.reference = this.reference.getNext();
         }

         while(this.reference == null && this.references != null) {
            if (this.referenceIndex >= this.references.length) {
               this.moveToNextSegment();
               this.referenceIndex = 0;
            } else {
               this.reference = this.references[this.referenceIndex];
               ++this.referenceIndex;
            }
         }

      }

      private void moveToNextSegment() {
         this.reference = null;
         this.references = null;
         if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
            this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
            ++this.segmentIndex;
         }

      }

      public void remove() {
         Assert.state(this.last != null, "No element to remove", new Object[0]);
         ConcurrentReferenceHashMap.this.remove(this.last.getKey());
      }
   }

   private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
      private EntrySet() {
      }

      public Iterator<Map.Entry<K, V>> iterator() {
         return ConcurrentReferenceHashMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         if (o instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry)o;
            Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), Restructure.NEVER);
            Entry<K, V> otherEntry = ref != null ? ref.get() : null;
            if (otherEntry != null) {
               return ConcurrentReferenceHashMap.nullSafeEquals(otherEntry.getValue(), otherEntry.getValue());
            }
         }

         return false;
      }

      public boolean remove(Object o) {
         if (o instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry)o;
            return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
         } else {
            return false;
         }
      }

      public int size() {
         return ConcurrentReferenceHashMap.this.size();
      }

      public void clear() {
         ConcurrentReferenceHashMap.this.clear();
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   private abstract class Task<T> {
      private final EnumSet<TaskOption> options;

      public Task(TaskOption... options) {
         this.options = options.length == 0 ? EnumSet.noneOf(TaskOption.class) : EnumSet.of(options[0], options);
      }

      public boolean hasOption(TaskOption option) {
         return this.options.contains(option);
      }

      protected T execute(Reference<K, V> ref, Entry<K, V> entry, Entries<V> entries) {
         return this.execute(ref, entry);
      }

      protected T execute(Reference<K, V> ref, Entry<K, V> entry) {
         return null;
      }
   }

   protected final class Segment extends ReentrantLock {
      private final ReferenceManager referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
      private final int initialSize;
      private final AtomicInteger count = new AtomicInteger(0);
      private volatile Reference<K, V>[] references;
      private int resizeThreshold;

      public Segment(int initialSize, int resizeThreshold) {
         this.initialSize = initialSize;
         this.references = this.createReferenceArray(initialSize);
         this.resizeThreshold = resizeThreshold;
      }

      public Reference<K, V> getReference(Object key, int hash, Restructure restructure) {
         if (restructure == Restructure.WHEN_NECESSARY) {
            this.restructureIfNecessary(false);
         }

         if (this.count.get() == 0) {
            return null;
         } else {
            Reference<K, V>[] references = this.references;
            int index = this.getIndex(hash, references);
            Reference<K, V> head = references[index];
            return this.findInChain(head, key, hash);
         }
      }

      public <T> T doTask(int hash, Object key, Task<T> task) {
         boolean resize = task.hasOption(TaskOption.RESIZE);
         if (task.hasOption(TaskOption.RESTRUCTURE_BEFORE)) {
            this.restructureIfNecessary(resize);
         }

         if (task.hasOption(TaskOption.SKIP_IF_EMPTY) && this.count.get() == 0) {
            return (T) task.execute((Reference)null, (Entry)null, (Entries)null);
         } else {
            this.lock();

            Object var10;
            try {
               int index = this.getIndex(hash, this.references);
               Reference<K, V> head = this.references[index];
               Reference<K, V> ref = this.findInChain(head, key, hash);
               Entry<K, V> entry = ref != null ? ref.get() : null;
               Entries<V> entries = (value) -> {
                  Entry<K, V> newEntry = new Entry(key, value);
                  Reference<K, V> newReference = this.referenceManager.createReference(newEntry, hash, head);
                  this.references[index] = newReference;
                  this.count.incrementAndGet();
               };
               var10 = task.execute(ref, entry, entries);
            } finally {
               this.unlock();
               if (task.hasOption(TaskOption.RESTRUCTURE_AFTER)) {
                  this.restructureIfNecessary(resize);
               }

            }

            return (T) var10;
         }
      }

      public void clear() {
         if (this.count.get() != 0) {
            this.lock();

            try {
               this.references = this.createReferenceArray(this.initialSize);
               this.resizeThreshold = (int)((float)this.references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
               this.count.set(0);
            } finally {
               this.unlock();
            }

         }
      }

      protected final void restructureIfNecessary(boolean allowResize) {
         int currCount = this.count.get();
         boolean needsResize = allowResize && currCount > 0 && currCount >= this.resizeThreshold;
         Reference<K, V> ref = this.referenceManager.pollForPurge();
         if (ref != null || needsResize) {
            this.restructure(allowResize, ref);
         }

      }

      private void restructure(boolean allowResize, Reference<K, V> ref) {
         this.lock();

         try {
            int countAfterRestructure = this.count.get();
            Set<Reference<K, V>> toPurge = Collections.emptySet();
            if (ref != null) {
               for(toPurge = new HashSet(); ref != null; ref = this.referenceManager.pollForPurge()) {
                  ((Set)toPurge).add(ref);
               }
            }

            countAfterRestructure -= ((Set)toPurge).size();
            boolean needsResize = countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold;
            boolean resizing = false;
            int restructureSize = this.references.length;
            if (allowResize && needsResize && restructureSize < 1073741824) {
               restructureSize <<= 1;
               resizing = true;
            }

            Reference<K, V>[] restructured = resizing ? this.createReferenceArray(restructureSize) : this.references;

            for(int i = 0; i < this.references.length; ++i) {
               ref = this.references[i];
               if (!resizing) {
                  restructured[i] = null;
               }

               for(; ref != null; ref = ref.getNext()) {
                  if (!((Set)toPurge).contains(ref)) {
                     Entry<K, V> entry = ref.get();
                     if (entry != null) {
                        int index = this.getIndex(ref.getHash(), restructured);
                        restructured[index] = this.referenceManager.createReference(entry, ref.getHash(), restructured[index]);
                     }
                  }
               }
            }

            if (resizing) {
               this.references = restructured;
               this.resizeThreshold = (int)((float)this.references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
            }

            this.count.set(Math.max(countAfterRestructure, 0));
         } finally {
            this.unlock();
         }

      }

      private Reference<K, V> findInChain(Reference<K, V> ref, Object key, int hash) {
         for(Reference<K, V> currRef = ref; currRef != null; currRef = currRef.getNext()) {
            if (currRef.getHash() == hash) {
               Entry<K, V> entry = currRef.get();
               if (entry != null) {
                  K entryKey = entry.getKey();
                  if (ConcurrentReferenceHashMap.nullSafeEquals(entryKey, key)) {
                     return currRef;
                  }
               }
            }
         }

         return null;
      }

      private Reference<K, V>[] createReferenceArray(int size) {
         return new Reference[size];
      }

      private int getIndex(int hash, Reference<K, V>[] references) {
         return hash & references.length - 1;
      }

      public final int getSize() {
         return this.references.length;
      }

      public final int getCount() {
         return this.count.get();
      }
   }

   private static final class WeakEntryReference<K, V> extends WeakReference<Entry<K, V>> implements Reference<K, V> {
      private final int hash;
      private final Reference<K, V> nextReference;

      public WeakEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
         super(entry, queue);
         this.hash = hash;
         this.nextReference = next;
      }

      public int getHash() {
         return this.hash;
      }

      public Reference<K, V> getNext() {
         return this.nextReference;
      }

      public void release() {
         this.enqueue();
         this.clear();
      }
   }

   private static final class SoftEntryReference<K, V> extends SoftReference<Entry<K, V>> implements Reference<K, V> {
      private final int hash;
      private final Reference<K, V> nextReference;

      public SoftEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
         super(entry, queue);
         this.hash = hash;
         this.nextReference = next;
      }

      public int getHash() {
         return this.hash;
      }

      public Reference<K, V> getNext() {
         return this.nextReference;
      }

      public void release() {
         this.enqueue();
         this.clear();
      }
   }

   protected static final class Entry<K, V> implements Map.Entry<K, V> {
      private final K key;
      private volatile V value;

      public Entry(K key, V value) {
         this.key = key;
         this.value = value;
      }

      public static int nullSafeHashCode(Object obj) {
         if (obj == null) {
            return 0;
         } else {
            if (obj.getClass().isArray()) {
               if (obj instanceof Object[]) {
                  return nullSafeHashCode((Object[])((Object[])obj));
               }

               if (obj instanceof boolean[]) {
                  return nullSafeHashCode((boolean[])((boolean[])obj));
               }

               if (obj instanceof byte[]) {
                  return nullSafeHashCode((byte[])((byte[])obj));
               }

               if (obj instanceof char[]) {
                  return nullSafeHashCode((char[])((char[])obj));
               }

               if (obj instanceof double[]) {
                  return nullSafeHashCode((double[])((double[])obj));
               }

               if (obj instanceof float[]) {
                  return nullSafeHashCode((float[])((float[])obj));
               }

               if (obj instanceof int[]) {
                  return nullSafeHashCode((int[])((int[])obj));
               }

               if (obj instanceof long[]) {
                  return nullSafeHashCode((long[])((long[])obj));
               }

               if (obj instanceof short[]) {
                  return nullSafeHashCode((short[])((short[])obj));
               }
            }

            return obj.hashCode();
         }
      }

      public K getKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public V setValue(V value) {
         V previous = this.value;
         this.value = value;
         return previous;
      }

      public String toString() {
         return this.key + "=" + this.value;
      }

      public final boolean equals(Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof Map.Entry)) {
            return false;
         } else {
            Map.Entry otherEntry = (Map.Entry)other;
            return ConcurrentReferenceHashMap.nullSafeEquals(this.getKey(), otherEntry.getKey()) && ConcurrentReferenceHashMap.nullSafeEquals(this.getValue(), otherEntry.getValue());
         }
      }

      public final int hashCode() {
         return nullSafeHashCode(this.key) ^ nullSafeHashCode(this.value);
      }
   }

   private interface Entries<V> {
      void add(V var1);
   }

   protected interface Reference<K, V> {
      Entry<K, V> get();

      int getHash();

      Reference<K, V> getNext();

      void release();
   }

   protected static enum Restructure {
      WHEN_NECESSARY,
      NEVER;
   }

   private static enum TaskOption {
      RESTRUCTURE_BEFORE,
      RESTRUCTURE_AFTER,
      SKIP_IF_EMPTY,
      RESIZE;
   }

   public static enum ReferenceType {
      SOFT,
      WEAK;
   }
}
