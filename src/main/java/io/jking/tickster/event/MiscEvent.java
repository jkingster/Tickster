package io.jking.tickster.event;

import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class MiscEvent implements EventListener {

    private final GuildCache guildCache;

    public MiscEvent(GuildCache guildCache) {
        this.guildCache = guildCache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
    }

    private void onGuildReady(GuildReadyEvent event) {
        insertGuildIfNotExists(event.getGuild());
    }

    private void onGuildJoin(GuildJoinEvent event) {
        insertGuildIfNotExists(event.getGuild());
    }

    private void insertGuildIfNotExists(Guild guild) {
        final Long guildId = guild.getIdLong();
        final GuildDataRecord record = guildCache.fetchOrGet(guildId);

        if (record == null) {
            final Long ownerId = guild.getOwnerIdLong();
            final GuildDataRecord newRecord = GUILD_DATA.newRecord().setGuildId(guildId)
                    .setOwnerId(ownerId).setLogId(0L).setSupportId(0L)
                    .setInviteId(0L).setCategoryId(0L).setTicketId(0L);
            guildCache.insert(newRecord);
        }
    }


}
