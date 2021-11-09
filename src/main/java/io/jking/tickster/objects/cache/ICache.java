package io.jking.tickster.objects.cache;


import org.jooq.Field;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ICache<K, V> {

    void put(K key, V value);

    void delete(K key);

    void retrieve(K key, Consumer<V> value, Consumer<Throwable> throwable);

    V get(K key);

    V forceGet(K key);

    <T> void update(K key, Field<T> field, T value, BiConsumer<? super Integer, Throwable> biConsumer);

}
