package io.jking.tickster.cache;


import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.Record;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public abstract class CachedObject<K, V extends Record> implements ICache<K, V> {

    private final Map<K, V> expiringMap;

    public CachedObject(ExpirationPolicy expirationPolicy) {
        this.expiringMap = ExpiringMap.builder()
                .expirationPolicy(expirationPolicy)
                .build();
    }

    public CachedObject(ExpirationPolicy expirationPolicy, int expireTime, TimeUnit timeUnit) {
        this.expiringMap = ExpiringMap.builder()
                .expirationPolicy(expirationPolicy)
                .expiration(expireTime, timeUnit)
                .build();
    }

    public Map<K, V> getMap() {
        return expiringMap;
    }

    @Override
    public void put(K key, V value) {
        if (key == null)
            return;
        expiringMap.put(key, value);
    }

    @Override
    public void delete(K key) {
        if (key == null)
            return;
        expiringMap.remove(key);
    }

    @Override
    public V get(K key) {
        return expiringMap.getOrDefault(key, null);
    }

}
