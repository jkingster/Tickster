package io.jking.untitled.event;

import io.jking.untitled.core.Config;
import io.jking.untitled.data.MessageData;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageEvent extends ListenerAdapter {

    /**
     * TODO- Once we implement database support to have logging channels per server,
     * we then can send to the particular channel necessary if this module is enabled.
     * Otherwise the functionality is done and everything seems to be working.
     */

    private final Map<Long, MessageData> MESSAGE_MAP = new ConcurrentHashMap<>();

    private final Config config;

    public MessageEvent(Config config) {
        this.config = config;
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

        final long logId = config.getObject("bot").getLong("log_id", 0L);
        if (logId == 0L)
            return;

        final TextChannel channel = event.getGuild().getTextChannelById(logId);
        if (channel == null)
            return;

        channel.sendMessageFormat("**Message deleted:** %s", data).queue();
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        final MessageData data = MESSAGE_MAP.getOrDefault(event.getMessageIdLong(), null);
        if (data == null)
            return;

        final long logId = config.getObject("bot").getLong("log_id", 0L);
        if (logId == 0L)
            return;

        final TextChannel channel = event.getGuild().getTextChannelById(logId);
        if (channel == null)
            return;

        final String editedContent = data.getEditedContent();
        if (editedContent != null) {
            data.setOriginalContent(editedContent);
            data.setEditedContent(event.getMessage().getContentDisplay());
            channel.sendMessageFormat("**Message Updated:** %s", data).queue();
            return;
        }

        data.setEditedContent(event.getMessage().getContentDisplay());
        channel.sendMessageFormat("**Message Updated:** %s", data).queue();
    }
}
