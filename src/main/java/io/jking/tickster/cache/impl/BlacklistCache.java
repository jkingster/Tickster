package io.jking.tickster.cache.impl;

import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.BlacklistDataRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.jking.tickster.jooq.tables.BlacklistData.BLACKLIST_DATA;

public class BlacklistCache {

    private final Map<Long, String> BLACKLISTED_CACHE = new ConcurrentHashMap<>();

    public BlacklistCache(Database database) {
        database.getContext().selectFrom(BLACKLIST_DATA)
                .fetch()
                .forEach(this::put);
    }

    private void put(BlacklistDataRecord blacklistDataRecord) {
        this.BLACKLISTED_CACHE.put(
                blacklistDataRecord.getGuildId(),
                blacklistDataRecord.getReason()
        );
    }

    public String get(long guildId) {
        return this.BLACKLISTED_CACHE.getOrDefault(guildId, null);
    }

    public boolean isBlacklisted(long guildId) {
        return get(guildId) != null;
    }

    public Map<Long, String> getCacheMap() {
        return BLACKLISTED_CACHE;
    }
}
