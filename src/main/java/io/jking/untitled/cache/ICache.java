package io.jking.untitled.cache;

import io.jking.untitled.database.Hikari;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.*;
import org.jooq.Record;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;

public interface ICache<K, V> {

    void put(K key, V value);

    void push(K key);

    void delete(K key);

    void retrieve(K key, Consumer<V> value);

    void update(K key, V newValue);

    <T> void update(K key, Field<T> targetField, T value, Consumer<Integer> success, Consumer<Throwable> throwable);

    Record get(K key);

}
