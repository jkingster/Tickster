package io.jking.tickster.database.impl;

import io.jking.jooq.tables.records.GuildTicketBlacklistRecord;
import io.jking.tickster.database.Repository;

import static io.jking.jooq.tables.GuildTicketBlacklist.GUILD_TICKET_BLACKLIST;

public class BlacklistRepo extends Repository<GuildTicketBlacklistRecord> {

    private static final BlacklistRepo instance = new BlacklistRepo();

    public static BlacklistRepo getInstance() {
        if (instance == null)
            return new BlacklistRepo();
        return instance;
    }

    @Override
    public void save(GuildTicketBlacklistRecord record) {
        dsl().insertInto(GUILD_TICKET_BLACKLIST)
                .set(record)
                .onConflictDoNothing()
                .execute();
    }

    @Override
    public GuildTicketBlacklistRecord retrieve(long id) {
        return dsl().selectFrom(GUILD_TICKET_BLACKLIST)
                .where(GUILD_TICKET_BLACKLIST.GUILD_ID.eq(id))
                .fetchOneInto(GuildTicketBlacklistRecord.class);
    }

    @Override
    public void delete(long id) {
        dsl().deleteFrom(GUILD_TICKET_BLACKLIST)
                .where(GUILD_TICKET_BLACKLIST.GUILD_ID.eq(id))
                .execute();
    }
}
