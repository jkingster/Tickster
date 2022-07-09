package io.jking.tickster.cache.impl;

import io.jking.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.cache.CachedRecord;
import io.jking.tickster.database.Repository;
import io.jking.tickster.database.impl.GuildRepo;

public class GuildCache extends CachedRecord<GuildDataRecord> {
    public GuildCache() {
        super(GuildRepo.getInstance());
    }
}
