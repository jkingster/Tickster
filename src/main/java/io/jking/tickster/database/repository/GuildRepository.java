package io.jking.tickster.database.repository;

import io.jking.jooq.tables.pojos.GuildData;
import org.jooq.Field;

import java.util.List;

import static io.jking.jooq.tables.GuildData.GUILD_DATA;

public class GuildRepository extends Repository<GuildData> {

    public static GuildRepository getInstance() {
        return new GuildRepository();
    }

    @Override
    public int save(GuildData wrapper) {
        return dsl().insertInto(GUILD_DATA)
                .set(dsl().newRecord(GUILD_DATA, wrapper))
                .onConflictDoNothing()
                .execute();
    }

    @Override
    public int delete(long id) {
        return dsl()
                .deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(id))
                .execute();
    }

    @Override
    public GuildData fetch(long id) {
        return dsl().selectFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(id))
                .fetchOneInto(GuildData.class);
    }

    public <T> int update(long id, Field<T> field, T newValue) {
        return dsl().update(GUILD_DATA)
                .set(field, newValue)
                .execute();
    }

    @Override
    public List<GuildData> getAll() {
        return dsl().selectFrom(GUILD_DATA).fetchInto(GuildData.class);
    }
}
