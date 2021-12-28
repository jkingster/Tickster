package io.jking.tickster.cache.impl;

import io.jking.tickster.cache.CachedObject;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import net.jodah.expiringmap.ExpirationPolicy;
import org.jooq.Field;

import java.util.function.Consumer;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class GuildCache extends CachedObject<Long, GuildDataRecord> {

    private final Database database;

    public GuildCache(Database database) {
        super(ExpirationPolicy.ACCESSED);
        this.database = database;
    }

    @Override
    public void retrieve(Long key, Consumer<GuildDataRecord> value, Consumer<Throwable> throwable) {
        final GuildDataRecord record = get(key);
        if (record == null) {
            database.getDSL().selectFrom(GUILD_DATA)
                    .where(GUILD_DATA.GUILD_ID.eq(key))
                    .fetchAsync()
                    .whenCompleteAsync(((guildDataRecords, error) -> {
                        if (error != null) {
                            throwable.accept(error);
                            return;
                        }

                        put(key, guildDataRecords.get(0));
                        value.accept(guildDataRecords.get(0));
                    }));
            return;
        }
        value.accept(record);
    }

    @Override
    public GuildDataRecord forceGet(Long key) {
        final GuildDataRecord record = database.getDSL().selectFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(key))
                .fetchOne();

        put(key, record);
        return record;
    }

    @Override
    public <T> void update(Long key, Field<T> field, T value, Consumer<GuildDataRecord> record, Consumer<Throwable> throwable) {
        database.getDSL()
                .update(GUILD_DATA).set(field, value).where(GUILD_DATA.GUILD_ID.eq(key))
                .returning()
                .fetchAsync(database.getExecutor())
                .whenCompleteAsync((result, error) -> {
                    if (error != null) {
                        throwable.accept(error);
                        return;
                    }

                    final GuildDataRecord retrievedResult = result.get(0);
                    if (retrievedResult == null)
                        return;

                    record.accept(retrievedResult);
                    put(key, retrievedResult);
                });
    }
}
