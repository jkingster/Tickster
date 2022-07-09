package io.jking.tickster.cache;

import io.jking.tickster.database.Repository;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.Record;

import java.util.concurrent.TimeUnit;

public class CachedRecord<T extends Record> {

    private final ExpiringMap<Long, T> expiringMap;
    private final Repository<T>        repo;

    public CachedRecord(Repository<T> repo) {
        this.expiringMap = ExpiringMap.builder()
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .expiration(10, TimeUnit.MINUTES)
                .build();
        this.repo = repo;
    }

    public T get(final long id) {
        if (expiringMap.containsKey(id))
            return this.expiringMap.get(id);

        return this.expiringMap.computeIfAbsent(id, (absentId) -> {
            return this.repo.retrieve(id);
        });
    }

    public void put(final long id, T record) {
        this.expiringMap.put(id, record);
    }

}
