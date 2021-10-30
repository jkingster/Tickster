package io.jking.untitled.cache;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public interface ICache<K,V> {


    void put(K key, V value);

    void delete(K key);

    void retrieve(K key, Consumer<V> value);

    void update(K key, V newValue);

    V get(K key);

}
