package io.jking.untitled.cache;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.Record;
import org.jooq.Result;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public interface ICache<K, V>{


    void put(K key, V value);

    void push(K key);

    void delete(K key);

    void retrieve(K key, Consumer<V> value);

    void update(K key, V newValue);

    Record get(K key);

}
