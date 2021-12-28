package io.jking.tickster.cache.impl;

import io.jking.tickster.cache.CachedObject;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import net.jodah.expiringmap.ExpirationPolicy;
import org.jooq.Field;

import java.util.function.Consumer;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class TicketCache extends CachedObject<Long, GuildTicketsRecord> {

    private final Database database;

    public TicketCache(Database database) {
        super(ExpirationPolicy.CREATED);
        this.database = database;
    }

    @Override
    public void retrieve(Long key, Consumer<GuildTicketsRecord> value, Consumer<Throwable> throwable) {
        final GuildTicketsRecord record = get(key);
        if (record == null) {
            database.getDSL().selectFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.CHANNEL_ID.eq(key))
                    .fetchAsync()
                    .whenCompleteAsync((result, error) -> {
                        if (error != null) {
                            throwable.accept(error);
                            return;
                        }
                        put(key, result.get(0));
                        value.accept(result.get(0));
                    });
            return;
        }
        value.accept(record);
    }

    @Override
    public <T> void update(Long key, Field<T> field, T value, Consumer<GuildTicketsRecord> record, Consumer<Throwable> throwable) {
        database.getDSL()
                .update(GUILD_TICKETS).set(field, value).where(GUILD_TICKETS.CHANNEL_ID.eq(key))
                .returning()
                .fetchAsync(database.getExecutor())
                .whenCompleteAsync((result, error) -> {
                    if (error != null) {
                        throwable.accept(error);
                        return;
                    }

                    final GuildTicketsRecord retrievedResult = result.get(0);
                    if (retrievedResult == null)
                        return;

                    record.accept(retrievedResult);
                    put(key, retrievedResult);
                });
    }

    @Override
    public <T> void update(Long key, Field<T> field, T value) {
        database.getDSL().update(GUILD_TICKETS)
                .set(field, value)
                .where(GUILD_TICKETS.CHANNEL_ID.eq(key))
                .executeAsync();
    }
}
