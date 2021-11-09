package io.jking.tickster.command;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.command.type.SuccessType;
import io.jking.tickster.database.Database;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CommandContext {

    private final SlashCommandEvent event;
    private final Database database;
    private final Cache cache;

    public CommandContext(SlashCommandEvent event, Database database, Cache cache) {
        this.event = event;
        this.database = database;
        this.cache = cache;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    public Database getDatabase() {
        return database;
    }

    public JDA getJda() {
        return event.getJDA();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public User getSelf() {
        return event.getJDA().getSelfUser();
    }

    public ReplyAction replyFormatted(String content, Object... objects) {
        return reply(content.formatted(objects));
    }

    public ReplyAction reply(EmbedBuilder embedBuilder) {
        return event.replyEmbeds(embedBuilder.build());
    }

    public ReplyAction reply(String content) {
        return event.reply(content);
    }

    public void replySuccess(SuccessType successType, Object... objects) {
        final long guildId = getGuild().getIdLong();
        final long logId = getGuildCache().get(guildId).getLogChannel();
        final TextChannel channel = getGuild().getTextChannelById(logId);
        final EmbedBuilder embed = EmbedFactory.getSuccess(successType, objects);

        reply(embed).delay(15, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();

        if (channel != null) {
            channel.sendMessageEmbeds(embed.build()).queue(null,
                    new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
        }
    }

    public String getSubCommand() {
        return getEvent().getSubcommandName();
    }

    public TextChannel getOptionChannel(String name) {
        final OptionMapping mapping = getMapping(name);
        if (mapping == null)
            return null;
        return (TextChannel) mapping.getAsMessageChannel();
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public boolean canInteract(Role role) {
        return getSelfMember().canInteract(role);
    }

    public Role getOptionRole(String name) {
        final OptionMapping mapping = getMapping(name);
        if (mapping == null)
            return null;
        return mapping.getAsRole();
    }

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }

    public GuildCache getGuildCache() {
        return cache.getGuildCache();
    }

    public Cache getCache() {
        return cache;

    }
}
