package io.jking.untitled.event;

import io.jking.untitled.cache.Cache;
import io.jking.untitled.core.Config;
import io.jking.untitled.data.MessageData;
import io.jking.untitled.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MessageEvent extends ListenerAdapter {

    private final Map<Long, MessageData> MESSAGE_MAP = new ConcurrentHashMap<>();
    private final Map<Long, MessageData> DELETED_MAP = new ConcurrentHashMap<>();

    private final Cache cache;

    public MessageEvent(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage())
            return;

        MESSAGE_MAP.put(event.getMessageIdLong(), new MessageData(event.getMessage()));
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        final MessageData data = MESSAGE_MAP.getOrDefault(event.getMessageIdLong(), null);
        if (data == null)
            return;

        final long guildId = event.getGuild().getIdLong();
        cache.getGuildCache().retrieve(guildId, guildData -> {
            final TextChannel logChannel = event.getGuild().getTextChannelById(guildData.getLogsId());
            if (logChannel == null || !logChannel.canTalk())
                return;

            final EmbedBuilder embed = EmbedUtil.getDefault().setColor(Color.RED)
                    .setAuthor("Author ID: " + data.getAuthorId())
                    .setDescription(String.format("**Message Deleted:** ```%s```", data.getOriginalContent()));

            if (data.getEditedContent() != null) {
                embed.appendDescription(String.format("**Edited Content:** ```%s```", data.getEditedContent()));
            }

            logChannel.sendMessageEmbeds(embed.build()).queue();
        }, null);
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        System.out.println("FIRING");
        final MessageData data = MESSAGE_MAP.getOrDefault(event.getMessageIdLong(), null);
        if (data == null)
            return;

        final long guildId = event.getGuild().getIdLong();

        cache.getGuildCache().retrieve(guildId, guildData -> {

            final TextChannel logChannel = event.getGuild().getTextChannelById(guildData.getLogsId());
            if (logChannel == null || !logChannel.canTalk())
                return;

            final String editedContent = data.getEditedContent();
            if (editedContent != null) {
                data.setOriginalContent(editedContent);
                data.setEditedContent(event.getMessage().getContentDisplay());

                final EmbedBuilder embed = EmbedUtil.getDefault()
                        .setDescription(String.format(
                                "**Message Updated**\n**Original:** ```%s```\n**Updated:** ```%s```",
                                data.getOriginalContent(), data.getEditedContent()
                        ))
                        .setAuthor(event.getAuthor().getAsTag(), null, event.getAuthor().getEffectiveAvatarUrl())
                        .setColor(Color.CYAN);

                logChannel.sendMessageEmbeds(embed.build()).queue();
                return;
            }

            data.setEditedContent(event.getMessage().getContentDisplay());
            final EmbedBuilder embed = EmbedUtil.getDefault()
                    .setDescription(String.format(
                            "**Message Updated**\n**Original:** ```%s```\n**Updated:** ```%s```",
                            data.getOriginalContent(), data.getEditedContent()
                    ))
                    .setAuthor(event.getAuthor().getAsTag(), null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Color.CYAN);

            logChannel.sendMessageEmbeds(embed.build()).queue();
        }, null);
    }

    public List<MessageData> getCachedMessages() {
        return MESSAGE_MAP.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public MessageData getMostRecentDeleted(long channelId) {
        return DELETED_MAP.getOrDefault(channelId, null);
    }
}
