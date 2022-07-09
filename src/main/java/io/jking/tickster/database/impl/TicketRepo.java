package io.jking.tickster.database.impl;

import io.jking.jooq.tables.records.ActiveTicketDataRecord;
import io.jking.tickster.database.Repository;

import static io.jking.jooq.tables.ActiveTicketData.ACTIVE_TICKET_DATA;

public class TicketRepo extends Repository<ActiveTicketDataRecord> {

    private static final TicketRepo instance = new TicketRepo();

    public static TicketRepo getInstance() {
        if (instance == null)
            return new TicketRepo();
        return instance;
    }

    @Override
    public void save(ActiveTicketDataRecord record) {
        dsl().insertInto(ACTIVE_TICKET_DATA)
                .set(record)
                .onConflictDoNothing()
                .execute();
    }

    @Override
    public ActiveTicketDataRecord retrieve(long id) {
        return dsl().selectFrom(ACTIVE_TICKET_DATA)
                .where(ACTIVE_TICKET_DATA.CHANNEL_ID.eq(id))
                .fetchOneInto(ActiveTicketDataRecord.class);
    }

    @Override
    public void delete(long id) {
        dsl().deleteFrom(ACTIVE_TICKET_DATA)
                .where(ACTIVE_TICKET_DATA.CHANNEL_ID.eq(id))
                .execute();
    }
}
