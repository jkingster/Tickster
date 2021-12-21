package io.jking.tickster.cache.impl;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class TicketCache extends Cache<Long, GuildTicketsRecord> {

    public TicketCache(Database database) {
        super(database);
    }

    @Override
    public GuildTicketsRecord fetch(Long key) {
        final GuildTicketsRecord record = getContext().selectFrom(GUILD_TICKETS)
                .where(GUILD_TICKETS.GUILD_ID.eq(key))
                .fetchOne();

        put(key, record);
        return record;
    }

    @Override
    public GuildTicketsRecord fetchOrGet(Long key) {
        final GuildTicketsRecord record = get(key);
        return record == null ? fetch(key) : record;
    }

    @Override
    public void delete(Long key) {
        getContext().deleteFrom(GUILD_TICKETS)
                .where(GUILD_TICKETS.CHANNEL_ID.eq(key))
                .executeAsync();
    }
}
