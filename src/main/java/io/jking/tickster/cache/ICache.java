package io.jking.tickster.cache;


import org.jooq.Field;

import java.util.function.Consumer;

public interface ICache<K, V> {

    void put(K key, V value);

    void delete(K key);

    void retrieve(K key, Consumer<V> value, Consumer<Throwable> throwable);

    V get(K key);

    V forceGet(K key);

    <T> void update(Long key, Field<T> field, T value, Consumer<V> record, Consumer<Throwable> throwable);


}