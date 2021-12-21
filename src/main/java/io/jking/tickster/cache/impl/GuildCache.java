package io.jking.tickster.cache.impl;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class GuildCache extends Cache<Long, GuildDataRecord> {

    public GuildCache(Database database) {
        super(database);
    }

    @Override
    public GuildDataRecord fetch(Long key) {
        final GuildDataRecord record = getContext().selectFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(key))
                .fetchOne();

        put(key, record);
        return record;
    }

    @Override
    public GuildDataRecord fetchOrGet(Long key) {
        final GuildDataRecord record = get(key);
        return record == null ? fetch(key) : record;
    }

    @Override
    public void delete(Long key) {
        getContext().deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(key))
                .executeAsync();
    }
}
