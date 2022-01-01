package io.jking.tickster.event;

import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.core.Tickster;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.BlacklistDataRecord;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.jooq.TableField;

import static io.jking.tickster.jooq.tables.BlacklistData.BLACKLIST_DATA;
import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class MiscEvent implements EventListener {

    private final Database database;
    private final GuildCache guildCache;
    private final TicketCache ticketCache;

    public MiscEvent(Database database, GuildCache guildCache, TicketCache ticketCache) {
        this.database = database;
        this.guildCache = guildCache;
        this.ticketCache = ticketCache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
        else if (event instanceof GuildLeaveEvent)
            onGuildLeave((GuildLeaveEvent) event);
        else if (event instanceof ChannelDeleteEvent)
            onChannelDelete((ChannelDeleteEvent) event);
    }

    private void onGuildReady(GuildReadyEvent event) {
        final Guild guild = event.getGuild();
        if (isBlacklistedGuild(guild)) {
            leaveGuild(guild);
            return;
        }

        insertGuildIfNotExists(guild);
    }

    private void onGuildJoin(GuildJoinEvent event) {
        final Guild guild = event.getGuild();
        if (isBlacklistedGuild(guild)) {
            leaveGuild(guild);
            return;
        }

        insertGuildIfNotExists(guild);
    }

    private void leaveGuild(Guild guild) {
        guild.leave().queue(
                success -> Tickster.getLogger().info("Leaving Blacklisted Guild: {}", guild.getId()),
                error -> Tickster.getLogger().warn("Could not leave blacklisted guild: {}", guild.getId())
        );
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        guildCache.delete(event.getGuild().getIdLong());
        Tickster.getLogger().info("Leaving Guild: [{} | {}]", event.getGuild().getName(), event.getGuild().getId());
    }

    private void onChannelDelete(ChannelDeleteEvent event) {
        if (event.getChannelType() != ChannelType.TEXT)
            return;

        final long guildId = event.getGuild().getIdLong();
        final long channelId = event.getChannel().getIdLong();

        ticketCache.delete(channelId);
        checkIfDataChannel(channelId, guildId);
    }

    private boolean isBlacklistedGuild(Guild guild) {
        final long guildId = guild.getIdLong();
        final BlacklistDataRecord record = database.getContext()
                .selectFrom(BLACKLIST_DATA)
                .where(BLACKLIST_DATA.GUILD_ID.eq(guildId))
                .fetchOne();

        return record != null;
    }

    private void checkIfDataChannel(long guildId, long channelId) {
        final GuildDataRecord record = guildCache.fetchOrGet(guildId);
        if (record == null)
            return;

        if (channelId == record.getLogId())
            resetChannel(GUILD_DATA.LOG_ID, guildId);
        else if (channelId == record.getInviteId())
            resetChannel(GUILD_DATA.INVITE_ID, guildId);
    }

    private void resetChannel(TableField<GuildDataRecord, Long> field, long guildId) {
        guildCache.update(guildId, field, 0L);
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

            Tickster.getLogger().info("Registered New Guild: [{} | {}]", guild.getName(), guild.getId());
        }
    }


}
