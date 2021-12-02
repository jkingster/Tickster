package io.jking.tickster.handler;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.InviteCache;
import io.jking.tickster.database.Database;
import io.jking.tickster.object.InviteData;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class GuildHandler implements EventListener {

    private final Database database;
    private final Cache cache;

    public GuildHandler(Database database, Cache cache) {
        this.database = database;
        this.cache = cache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent)
            onGuildOwnerChange((GuildUpdateOwnerEvent) event);
        else if (event instanceof GuildLeaveEvent)
            onGuildLeave((GuildLeaveEvent) event);
        else if (event instanceof GuildMemberJoinEvent)
            onGuildMemberJoin((GuildMemberJoinEvent) event);
    }

    private void onGuildOwnerChange(GuildUpdateOwnerEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long newOwnerId = event.getNewOwnerIdLong();

        database.getDSL().update(GUILD_DATA)
                .set(GUILD_DATA.OWNER_ID, newOwnerId)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync();
    }

    private void onGuildJoin(GuildJoinEvent event) {
        final Guild guild = event.getGuild();
        checkPopulation(guild);
        insertGuild(guild);

        final Member self = event.getGuild().getSelfMember();
        if (self.hasPermission(Permission.MANAGE_SERVER))
            cacheInvites(guild);
    }


    private void onGuildReady(GuildReadyEvent event) {
        final Guild guild = event.getGuild();
        insertGuild(guild);
        cacheInvites(guild);

        System.out.println(event + " ready");
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        final long guildId = event.getGuild().getIdLong();
        cache.getGuildCache().delete(guildId);

        database.getDSL().deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync();
    }

    private void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final Member self = event.getGuild().getSelfMember();
        if (!self.hasPermission(Permission.MANAGE_SERVER) || event.getMember().getUser().isBot())
            return;

        event.getGuild().retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                final String inviteCode = invite.getCode();
                final InviteCache inviteCache = cache.getInviteCache();
                final InviteData inviteData = inviteCache.get(inviteCode);

                if (inviteData == null)
                    continue;

                if (invite.getUses() == inviteData.getUses())
                    continue;

                final int cachedUses = inviteData.getUses();
                if (invite.getUses() > cachedUses) {
                    attemptLogging(event, invite, cache.getGuildCache(), inviteData.incrementUses());
                    break;
                }
            }
        });
    }

    private void insertGuild(Guild guild) {
        if (guild == null)
            return;

        final long guildId = guild.getIdLong();
        final long ownerId = guild.getOwnerIdLong();

        database.getDSL().insertInto(GUILD_DATA)
                .set(GUILD_DATA.GUILD_ID, guildId)
                .set(GUILD_DATA.OWNER_ID, ownerId)
                .executeAsync();
    }

    private void cacheInvites(Guild guild) {
        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                cache.getInviteCache().put(new InviteData(invite));
            }
        });
    }

    private void checkPopulation(Guild guild) {
        guild.loadMembers().onSuccess(members -> {
            final double size = members.size();
            final double botList = members.parallelStream()
                    .map(Member::getUser)
                    .filter(User::isBot)
                    .collect(Collectors.toUnmodifiableList())
                    .size();

            final double calculation = (botList / size) * 100;
            if (calculation >= 80) {
                guild.leave().queue();
            }
        }).onError(Throwable::printStackTrace);
    }

    private void attemptLogging(GuildMemberJoinEvent event, Invite invite, GuildCache guildCache, InviteData inviteData) {
        final long guildId = event.getGuild().getIdLong();
        guildCache.retrieve(guildId, guildRecord -> {
            final long logId = guildRecord.getLogChannel();
            final TextChannel channel = event.getGuild().getTextChannelById(logId);

            if (channel == null || !channel.canTalk())
                return;

            final String inviter = invite.getInviter() == null ? "Unknown." : invite.getInviter().getAsTag();
            final EmbedBuilder inviteEmbed = EmbedFactory.getInvite(event.getMember(), inviter, inviteData);

            channel.sendMessageEmbeds(inviteEmbed.build()).queue();
        }, error -> {
        });
    }
}
