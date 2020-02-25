package org.dorax.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 一个带TTL的简单Cache，对于过期的entry没有清理
 *
 * @param <E>
 * @author wuchunfu
 * @date 2020-02-24
 */
public class SimpleCache<E> {

    final ConcurrentMap<String, CacheEntry<E>> cache = new ConcurrentHashMap<>();

    private static class CacheEntry<E> {
        final long expireTime;
        final E value;

        public CacheEntry(E value, long expire) {
            this.expireTime = expire;
            this.value = value;
        }
    }

    public void put(String key, E e, long ttlMs) {
        if (key == null || e == null) {
            return;
        }
        CacheEntry<E> entry = new CacheEntry<>(e, System.currentTimeMillis() + ttlMs);
        cache.put(key, entry);
    }

    public E get(String key) {
        CacheEntry<E> entry = cache.get(key);
        if (entry != null && entry.expireTime > System.currentTimeMillis()) {
            return entry.value;
        }
        return null;
    }
}
