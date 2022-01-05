package io.jking.tickster.event;

import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.InviteCache;
import io.jking.tickster.data.InviteData;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;


public class InviteEvent implements EventListener {

    private final GuildCache guildCache;
    private final InviteCache inviteCache;

    public InviteEvent(GuildCache guildCache, InviteCache inviteCache) {
        this.guildCache = guildCache;
        this.inviteCache = inviteCache;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildInviteCreateEvent)
            createInvite((GuildInviteCreateEvent) event);
        else if (event instanceof GuildInviteDeleteEvent)
            deleteInvite((GuildInviteDeleteEvent) event);
        else if (event instanceof GuildMemberJoinEvent)
            onMemberJoin((GuildMemberJoinEvent) event);
    }

    private void createInvite(GuildInviteCreateEvent event) {
        final Guild guild = event.getGuild();
        if (!checkPermissions(guild.getSelfMember()))
            return;

        if (getInviteChannel(guild) == null)
            return;

        inviteCache.put(event.getInvite());
    }

    private void deleteInvite(GuildInviteDeleteEvent event) {
        final Guild guild = event.getGuild();
        if (!checkPermissions(guild.getSelfMember()))
            return;

        if (getInviteChannel(guild) == null)
            return;

        inviteCache.remote(event.getCode());
    }

    private void onMemberJoin(GuildMemberJoinEvent event) {
        final Guild guild = event.getGuild();
        if (!checkPermissions(guild.getSelfMember()))
            return;

        final TextChannel inviteChannel = getInviteChannel(guild);
        if (inviteChannel == null)
            return;

        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                final String inviteCode = invite.getCode();
                final InviteData inviteData = inviteCache.get(inviteCode);
                if (inviteData == null)
                    continue;

                final int inviteUses = invite.getUses();
                final int cachedUses = inviteData.getUses();

                if (inviteUses == cachedUses)
                    continue;

                if (inviteUses > cachedUses) {
                    inviteData.incrementUses();

                    final User inviter = invite.getInviter();
                    final String inviterTag = inviter == null
                            ? "Unknown" : inviter.getAsTag();

                    sendInviteNotification(
                            inviteChannel,
                            event.getUser(),
                            invite.getUrl(),
                            inviterTag,
                            invite.getUses()
                    );
                }
            }
        });
    }

    private void sendInviteNotification(TextChannel channel, User user, String inviteCode, String inviterTag, int uses) {
        final String pattern = "**%s** used invite: `%s` to join.\n**Invite created by:** `%s`.\n**Total Uses:** `%s`.";
        final EmbedBuilder embed = EmbedUtil.getDefault().setTimestamp(Instant.now())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setAuthor("User Joined")
                .setDescription(String.format(pattern, user.getAsTag(), inviterTag, inviterTag, uses));

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    private boolean checkPermissions(Member self) {
        return self.hasPermission(Permission.MANAGE_SERVER);
    }

    private TextChannel getInviteChannel(Guild guild) {
        final long guildId = guild.getIdLong();
        final GuildDataRecord record = guildCache.fetchOrGet(guildId);
        if (record == null)
            return null;

        final TextChannel channel = guild.getTextChannelById(record.getInviteId());
        if (channel == null)
            return null;

        if (!channel.canTalk())
            return null;

        return channel;
    }


}
