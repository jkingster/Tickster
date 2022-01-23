package io.jking.tickster.database.repository;

import io.jking.jooq.tables.pojos.TicketData;

import java.time.LocalDateTime;
import java.util.List;

import static io.jking.jooq.tables.TicketData.TICKET_DATA;

public class TicketRepository extends Repository<TicketData> {

    public static TicketRepository getInstance() {
        return new TicketRepository();
    }

    @Override
    public int save(TicketData wrapper) {
        return dsl().executeInsert(dsl().newRecord(TICKET_DATA, wrapper));
    }

    @Override
    public int delete(long id) {
        return dsl()
                .deleteFrom(TICKET_DATA)
                .where(TICKET_DATA.CHANNEL_ID.eq(id))
                .execute();
    }

    @Override
    public TicketData fetch(long id) {
        return dsl().selectFrom(TICKET_DATA)
                .where(TICKET_DATA.CHANNEL_ID.eq(id))
                .fetchOneInto(TicketData.class);
    }

    @Override
    public List<TicketData> getAll() {
        return dsl().selectFrom(TICKET_DATA).fetchInto(TicketData.class);
    }

    public List<TicketData> getExpired() {
        return getAll().stream().filter(data -> {
            final LocalDateTime expiry = data.getExpiry();
            return expiry.isBefore(LocalDateTime.now());
        }).toList();
    }
}
