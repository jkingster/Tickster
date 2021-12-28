package io.jking.tickster.cache.impl;

import io.jking.tickster.cache.CachedObject;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildReportsRecord;
import net.jodah.expiringmap.ExpirationPolicy;

import java.util.function.Consumer;

import static io.jking.tickster.jooq.tables.GuildReports.GUILD_REPORTS;

public class ReportCache extends CachedObject<String, GuildReportsRecord> {

    private final Database database;

    public ReportCache(Database database) {
        super(ExpirationPolicy.CREATED);
        this.database = database;
    }


    @Override
    public void retrieve(String key, Consumer<GuildReportsRecord> value, Consumer<Throwable> throwable) {
        final GuildReportsRecord record = get(key);
        if (record == null) {
            database.getDSL().selectFrom(GUILD_REPORTS)
                    .where(GUILD_REPORTS.UUID.eq(key))
                    .fetchAsync()
                    .whenCompleteAsync(((records, error) -> {
                        if (error != null) {
                            throwable.accept(error);
                            return;
                        }

                        put(key, records.get(0));
                        value.accept(records.get(0));
                    }));
            return;
        }
        value.accept(record);
    }
}
