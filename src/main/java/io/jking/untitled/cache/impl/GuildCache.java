package io.jking.untitled.cache.impl;

import io.jking.untitled.cache.ICache;
import io.jking.untitled.database.Hikari;
import io.jking.untitled.jooq.tables.GuildData;
import io.jking.untitled.jooq.tables.records.GuildDataRecord;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jooq.*;
import org.jooq.Record;

import java.util.Map;
import java.util.function.Consumer;

import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;


public class GuildCache implements ICache<Long, GuildDataRecord> {

    private final Map<Long, GuildDataRecord> guildMap;

    public GuildCache() {
        this.guildMap = ExpiringMap.builder()
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .build();
    }


    @Override
    public void put(Long key, GuildDataRecord value) {
        guildMap.put(key, value);
    }

    @Override
    public void push(Long key) {
        final GuildDataRecord record = get(key).into(GUILD_DATA);
        put(key, record);
    }

    @Override
    public void delete(Long key) {
        guildMap.remove(key);
        Hikari.getInstance().getDSL()
                .deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(key))
                .executeAsync();
    }

    @Override
    public void retrieve(Long key, Consumer<GuildDataRecord> value) {
        final GuildDataRecord record = guildMap.getOrDefault(key, null);
        if (record == null) {
            final GuildDataRecord syncRecord = get(key).into(GUILD_DATA);
            put(key, syncRecord);
            value.accept(syncRecord);
            return;
        }
        value.accept(record);
    }

    @Override
    public void update(Long key, GuildDataRecord newValue) {
        guildMap.put(key, newValue);
        Hikari.getInstance().getDSL()
                .update(GUILD_DATA)
                .set(newValue)
                .where(GUILD_DATA.GUILD_ID.eq(key))
                .executeAsync();
    }

    @Override
    public <T> void update(Long key, Field<T> targetField, T value,
                           Consumer<Integer> success,
                           Consumer<Throwable> throwableConsumer) {

        final UpdateResultStep<GuildDataRecord> record = Hikari.getInstance().getDSL()
                .update(GUILD_DATA)
                .set(targetField, value)
                .returning();


        record.stream().parallel().findFirst().ifPresent(it -> put(key, it));
        record.executeAsync().whenCompleteAsync((integer, throwable) -> {
            if (throwable != null) {
                throwableConsumer.accept(throwable);
                return;
            }
            success.accept(integer);
        });
    }


    @Override
    public Record get(Long key) {
        return Hikari.getInstance()
                .getDSL()
                .selectFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(key))
                .fetchOne();
    }
}
