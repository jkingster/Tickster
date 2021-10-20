package io.jking.untitled.event;

import io.jking.untitled.core.Config;
import io.jking.untitled.data.InviteData;
import io.jking.untitled.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InviteEvent implements EventListener {

    private final Map<String, InviteData> INVITE_MAP = new ConcurrentHashMap<>();

    private final Config config;

    public InviteEvent(Config config) {
        this.config = config;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildInviteCreateEvent)
            onInviteCreate((GuildInviteCreateEvent) event);
        else if (event instanceof GuildInviteDeleteEvent)
            onInviteDelete((GuildInviteDeleteEvent) event);
        else if (event instanceof GuildMemberJoinEvent)
            onMemberJoin((GuildMemberJoinEvent) event);
        else if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
    }

    private void onMemberJoin(GuildMemberJoinEvent event) {
        final Guild guild = event.getGuild();
        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                final InviteData data = INVITE_MAP.getOrDefault(invite.getCode(), null);
                if (data == null)
                    return;

                final int cachedUses = data.getUses();
                if (invite.getUses() > cachedUses) {
                    sendInformation(event.getMember(), invite);
                    INVITE_MAP.put(invite.getCode(), data.incrementUses());
                }
            }
        });

    }

    private void onInviteDelete(GuildInviteDeleteEvent event) {
        final String code = event.getCode();
        INVITE_MAP.remove(code);
    }

    private void onInviteCreate(GuildInviteCreateEvent event) {
        final Invite invite = event.getInvite();
        INVITE_MAP.put(invite.getCode(), new InviteData(invite));
    }

    private void onGuildReady(GuildReadyEvent event) {
        final Guild guild = event.getGuild();
        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                INVITE_MAP.put(invite.getCode(), new InviteData(invite));
            }
        });
    }

    private void sendInformation(Member member, Invite invite) {
        final long logId = config.getObject("bot").getLong("log_id", 0L);
        if (logId == 0L)
            return;

        final Guild guild = member.getGuild();
        final TextChannel textChannel = guild.getTextChannelById(logId);
        if (textChannel == null)
            return;

        final User user = member.getUser();

        final String inviter = invite.getInviter() == null ? "Unknown" : invite.getInviter().getAsTag();
        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setColor(Color.ORANGE)
                .setAuthor(user.getAsTag() + " has joined.", null, user.getEffectiveAvatarUrl())
                .setDescription(String.format("**Code Used:** `%s`\n**Uses:** `%s`\n**Invite Creator:** `%s`",
                        invite.getCode(), invite.getUses(), inviter))
                .setFooter("ID: " + user.getIdLong());

        textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
