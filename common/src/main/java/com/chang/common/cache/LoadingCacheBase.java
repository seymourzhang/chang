package com.chang.common.cache;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
// Deleted:import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
// Deleted:import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LoadingCacheBase<K, V> {
    private final LoadingCache<K, V> loadingCache;
    private final Write<K, V> write;
    private final Delete<K, V> delete;

    public LoadingCacheBase(long maximumSize, int initialCapacity, long expireAfterAccessDuration, TimeUnit expireAfterAccessUnit, long expireAfterWriteDuration, TimeUnit expireAfterWriteUnit, long refreshAfterWriteDuration, TimeUnit refreshAfterWriteUnit, CacheLoader<K, V> cacheWriter, final Write<K, V> write, final Delete<K, V> delete) {
        this.write = write;
        this.delete = delete;
        CacheLoader<K, V> loaderTemp = ObjectUtil.isNull(cacheWriter) ? new CacheLoader<K, V>() {
            public @Nullable V load(@NonNull K key) throws Exception {
                return null;
            }

            public @NonNull Map<@NonNull K, @NonNull V> loadAll(@NonNull Iterable<? extends @NonNull K> keys) throws Exception {
                return (Map<K, V>) CacheLoader.super.loadAll((Set<? extends K>) keys);
            }

            public @Nullable V reload(@NonNull K key, @NonNull V oldValue) throws Exception {
                return CacheLoader.super.reload(key, oldValue);
            }
        } : cacheWriter;
        this.loadingCache = Caffeine.newBuilder().maximumSize(maximumSize).initialCapacity(initialCapacity).expireAfterAccess(expireAfterAccessDuration, expireAfterAccessUnit).expireAfterWrite(expireAfterWriteDuration, expireAfterWriteUnit).refreshAfterWrite(refreshAfterWriteDuration, refreshAfterWriteUnit).scheduler(Scheduler.forScheduledExecutorService(ThreadUtil.createScheduledExecutor(1024))).recordStats().build(loaderTemp);
    }

    public List<K> getKeys() {
        return new ArrayList(this.loadingCache.asMap().keySet());
    }

    public List<V> getValues() {
        return new ArrayList(this.loadingCache.asMap().values());
    }

    public void delByKeys(List<K> keys) {
        if (ObjectUtil.isNotNull(delete)) {
            for (K key : keys) {
                V value = this.loadingCache.getIfPresent(key);
                delete.explicit(key, value);
            }
        }
        this.loadingCache.invalidateAll(keys);
    }

    public void delByKey(K key) {
        if (ObjectUtil.isNotNull(delete)) {
            V value = this.loadingCache.getIfPresent(key);
            delete.explicit(key, value);
        }
        this.loadingCache.invalidate(key);
    }

    public void delAll() {
        if (ObjectUtil.isNotNull(delete)) {
            for (K key : getKeys()) {
                V value = this.loadingCache.getIfPresent(key);
                delete.explicit(key, value);
            }
        }
        this.loadingCache.invalidateAll();
    }

    public boolean containsKey(K key) {
        return this.loadingCache.asMap().containsKey(key);
    }

    public V get(K key) {
        return this.loadingCache.get(key);
    }

    public void put(K key, V value) {
        if (ObjectUtil.isNotNull(write)) {
            write.write(key, value);
        }
        this.loadingCache.put(key, value);
    }

    public LoadingCache<K, V> getLoadingCache() {
        return this.loadingCache;
    }
}
