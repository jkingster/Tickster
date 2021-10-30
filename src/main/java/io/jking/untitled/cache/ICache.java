package io.jking.untitled.cache;


import org.jooq.*;
import org.jooq.Record;


import java.util.function.Consumer;


public interface ICache<K, V> {

    void put(K key, V value);

    void push(K key);

    void delete(K key);

    void retrieve(K key, Consumer<V> value);

    void update(K key, V newValue);

    <T> void update(K key, Field<T> targetField, T value, Consumer<Integer> success, Consumer<Throwable> throwable);

    Record get(K key);

}
