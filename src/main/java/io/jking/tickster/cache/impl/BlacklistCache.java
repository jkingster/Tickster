package io.jking.tickster.cache.impl;

import io.jking.jooq.tables.records.GuildTicketBlacklistRecord;
import io.jking.tickster.cache.CachedRecord;
import io.jking.tickster.database.Repository;
import io.jking.tickster.database.impl.BlacklistRepo;

public class BlacklistCache extends CachedRecord<GuildTicketBlacklistRecord> {
    public BlacklistCache() {
        super(BlacklistRepo.getInstance());
    }
}
