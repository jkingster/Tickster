package io.jking.tickster.cache.impl;

import io.jking.jooq.tables.records.ActiveTicketDataRecord;
import io.jking.tickster.cache.CachedRecord;
import io.jking.tickster.database.Repository;
import io.jking.tickster.database.impl.TicketRepo;

public class TicketCache extends CachedRecord<ActiveTicketDataRecord> {
    public TicketCache() {
        super(TicketRepo.getInstance());
    }
}
