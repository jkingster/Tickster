package io.jking.tickster.objects.cache;


import io.jking.tickster.database.Database;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.*;
import org.jooq.Record;

import java.util.Map;
import java.util.function.BiConsumer;

public abstract class CachedObject<K, V extends Record> implements ICache<K, V> {

    private final Database database;
    private final Map<K, V> expiringMap;

    public CachedObject(Database database, ExpirationPolicy expirationPolicy) {
        this.database = database;
        this.expiringMap = ExpiringMap.builder().expirationPolicy(expirationPolicy).build();
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
