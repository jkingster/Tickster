package io.jking.tickster.database.impl;

import io.jking.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.database.Repository;
import io.jking.tickster.logging.LogType;
import io.jking.tickster.logging.TicksterLogger;
import net.dv8tion.jda.api.entities.Guild;
import org.jooq.Field;
import org.jooq.TableField;

import static io.jking.jooq.tables.GuildData.GUILD_DATA;

public class GuildRepo extends Repository<GuildDataRecord> {

    private static final GuildRepo instance = new GuildRepo();

    public static GuildRepo getInstance() {
        if (instance == null)
            return new GuildRepo();
        return instance;
    }

    @Override
    public void save(GuildDataRecord record) {
        dsl().insertInto(GUILD_DATA)
                .set(record)
                .onConflictDoNothing()
                .execute();
    }

    @Override
    public GuildDataRecord retrieve(long id) {
        return dsl().selectFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(id))
                .fetchOneInto(GuildDataRecord.class);
    }

    @Override
    public void delete(long id) {
        dsl().deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(id))
                .execute();
    }

    public boolean isExisting(long id) {
        return dsl().fetchExists(GUILD_DATA, GUILD_DATA.GUILD_ID.eq(id));
    }

    public void register(Guild guild) {
        long guildId = guild.getIdLong(), ownerId = guild.getOwnerIdLong();
        TicksterLogger.log(LogType.GUILD_REGISTER, guildId, ownerId);

        GuildDataRecord record = dsl().newRecord(GUILD_DATA)
                .setGuildId(guildId)
                .setOwnerId(ownerId);

        save(record);
    }

    public <T> void update(long id, TableField<GuildDataRecord, T> field, T newValue) {
        dsl().update(GUILD_DATA)
                .set(field, newValue)
                .execute();
    }
}
