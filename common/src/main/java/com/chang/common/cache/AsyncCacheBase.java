package com.chang.common.cache;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.AsyncCache;
// Deleted:import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
// Deleted:import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AsyncCacheBase<K, V> {
    private final AsyncCache<K, V> asyncCache;
    private final Write<K, V> write;
    private final Delete<K, V> delete;

    public AsyncCacheBase(long maximumSize, int initialCapacity, long expireAfterAccessDuration, TimeUnit expireAfterAccessUnit, long expireAfterWriteDuration, TimeUnit expireAfterWriteUnit, long refreshAfterWriteDuration, TimeUnit refreshAfterWriteUnit, final Write<K, V> write, final Delete<K, V> delete) {
        this.write = write;
        this.delete = delete;
        this.asyncCache = Caffeine.newBuilder().maximumSize(maximumSize).initialCapacity(initialCapacity).expireAfterAccess(expireAfterAccessDuration, expireAfterAccessUnit).expireAfterWrite(expireAfterWriteDuration, expireAfterWriteUnit).refreshAfterWrite(refreshAfterWriteDuration, refreshAfterWriteUnit).scheduler(Scheduler.forScheduledExecutorService(ThreadUtil.createScheduledExecutor(1024))).recordStats().buildAsync();
    }

    public List<K> getKeys() {
        return new ArrayList(this.asyncCache.asMap().keySet());
    }

    public List<CompletableFuture<V>> getValues() {
        return new ArrayList(this.asyncCache.asMap().values());
    }

    public AsyncCache<K, V> getAsyncCache() {
        return this.asyncCache;
    }

    public CompletableFuture<V> get(K key) {
        return this.asyncCache.get(key, (k) -> null);
    }

    public void put(K key, V value) {
        if (ObjectUtil.isNotNull(write)) {
            write.write(key, value);
        }
        this.asyncCache.put(key, (CompletableFuture<? extends V>) value);
    }

    public void invalidate(K key) {
        V value = null;
        try {
            CompletableFuture<V> future = this.asyncCache.asMap().get(key);
            if (future != null) {
                value = future.getNow(null);
            }
        } catch (Exception e) {
        }

        if (ObjectUtil.isNotNull(delete)) {
            delete.explicit(key, value);
        }
        this.asyncCache.asMap().remove(key);
    }

    public void invalidateAll() {
        if (ObjectUtil.isNotNull(delete)) {
            for (K key : getKeys()) {
                V value = null;
                try {
                    CompletableFuture<V> future = this.asyncCache.asMap().get(key);
                    if (future != null) {
                        value = future.getNow(null);
                    }
                } catch (Exception e) {
                }
                delete.explicit(key, value);
            }
        }
        this.asyncCache.asMap().clear();
    }
}
