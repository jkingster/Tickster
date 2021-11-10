package io.jking.tickster.cache.impl;

import io.jking.tickster.cache.CachedObject;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import net.jodah.expiringmap.ExpirationPolicy;
import org.jooq.Field;
import org.jooq.UpdateResultStep;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;
import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class TicketCache extends CachedObject<Long, GuildTicketsRecord> {

    private final Database database;

    public TicketCache(Database database) {
        super(ExpirationPolicy.CREATED, 5, TimeUnit.MINUTES);
        this.database = database;
    }

    @Override
    public void retrieve(Long key, Consumer<GuildTicketsRecord> value, Consumer<Throwable> throwable) {
        final GuildTicketsRecord record = get(key);
        if (record == null) {
            database.getDSL().selectFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.GUILD_ID.eq(key))
                    .fetchAsync()
                    .whenCompleteAsync((records, error) -> {
                        if (throwable != null) {
                            throwable.accept(error);
                            return;
                        }

                        final GuildTicketsRecord retrievedRecord = records.get(0);
                        value.accept(retrievedRecord);
                        put(key, retrievedRecord);
                    });
            return;
        }

        value.accept(record);
    }

    @Override
    public GuildTicketsRecord forceGet(Long key) {
        return null;
    }

    @Override
    public <T> void update(Long key, Field<T> field, T value, BiConsumer<? super Integer, Throwable> biConsumer) {
        final UpdateResultStep<GuildTicketsRecord> record = database.getDSL()
                .update(GUILD_TICKETS).set(field, value).where(GUILD_DATA.GUILD_ID.eq(key))
                .returning();

        record.stream().parallel().findFirst().ifPresent(it -> put(key, it));
        record.executeAsync().whenCompleteAsync(biConsumer);
    }


}
