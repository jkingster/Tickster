package io.jking.tickster.database.repository;

import io.jking.jooq.tables.pojos.TicketSettings;
import org.jooq.Field;

import java.util.List;

import static io.jking.jooq.tables.TicketSettings.TICKET_SETTINGS;

public class TicketSettingsRepository extends Repository<TicketSettings> {

    public static TicketSettingsRepository getInstance() {
        return new TicketSettingsRepository();
    }

    @Override
    public int save(TicketSettings wrapper) {
        return dsl()
                .insertInto(TICKET_SETTINGS)
                .set(dsl().newRecord(TICKET_SETTINGS, wrapper))
                .execute();
    }

    @Override
    public int delete(long id) {
        return dsl().deleteFrom(TICKET_SETTINGS)
                .where(TICKET_SETTINGS.GUILD_ID.eq(id))
                .execute();
    }

    @Override
    public TicketSettings fetch(long id) {
        return dsl().selectFrom(TICKET_SETTINGS)
                .where(TICKET_SETTINGS.GUILD_ID.eq(id))
                .fetchOneInto(TicketSettings.class);
    }

    @Override
    public List<TicketSettings> getAll() {
        return dsl().selectFrom(TICKET_SETTINGS)
                .fetchInto(TicketSettings.class);
    }

    public <T> int update(long id, Field<T> field, T newValue) {
        return dsl().update(TICKET_SETTINGS)
                .set(field, newValue)
                .execute();
    }
}
