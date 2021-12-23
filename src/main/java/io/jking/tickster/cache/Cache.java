package io.jking.tickster.cache;

import io.jking.tickster.database.Database;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class Cache<K, V extends Record> {

    private final Database database;
    private final Map<K, V> CACHE_MAP;

    public Cache(Database database) {
        this.database = database;
        this.CACHE_MAP = ExpiringMap.builder()
                .expiration(10, TimeUnit.MINUTES)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .build();
    }

    public void put(K key, V value) {
        this.CACHE_MAP.put(key, value);
    }

    public V get(K key) {
        return this.CACHE_MAP.getOrDefault(key, null);
    }

    public <T> void putUpdated(K key, Field<T> field, T value) {
        final V record = get(key);
        if (record == null)
            return;

        record.set(field, value);
        put(key, record);
    }

    public abstract void insert(V value);

    public abstract V fetch(K key);

    public abstract V fetchOrGet(K key);

    public abstract void delete(K key);

    public abstract <T> int update(K key, Field<T> field, T value);

    public Database getDatabase() {
        return database;
    }

    public Map<K, V> getCacheMap() {
        return CACHE_MAP;
    }

    public DSLContext getContext() {
        return getDatabase().getContext();
    }

    public int size() {
        return this.CACHE_MAP.size();
    }
}
