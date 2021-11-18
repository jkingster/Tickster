package io.jking.tickster.command;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.command.type.SuccessType;
import io.jking.tickster.database.Database;
import io.jking.tickster.jooq.tables.records.GuildDataRecord;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandContext {

    private final Logger logger = LoggerFactory.getLogger(CommandContext.class);

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

    public Member getMember() {
        return getEvent().getMember();
    }

    public User getAuthor() {
        return getEvent().getUser();
    }

    public InteractionHook getHook() {
        return getEvent().getHook();
    }

    public Interaction getInteraction() {
        return getEvent().getInteraction();
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

    public void replySuccess(SuccessType type, boolean log, Object... objects) {
        final EmbedBuilder embed = EmbedFactory.getSuccess(type, objects);

        reply(embed).delay(15, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();

        if (log)
            sendLog(embed);
    }

    public RestAction<Message> retrieveOriginal() {
        return getEvent().getHook().retrieveOriginal();
    }

    private void sendLog(EmbedBuilder embed) {
        getGuildCache().retrieve(getGuild().getIdLong(), record -> {
            final long logsId = record.getLogChannel();
            final TextChannel channel = getGuild().getTextChannelById(logsId);

            if (channel == null || !channel.canTalk())
                return;

            channel.sendMessageEmbeds(embed.build()).queue();
        }, null);
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

    public String getOptionString(String name) {
        final OptionMapping mapping = getMapping(name);
        if (mapping == null)
            return null;
        return mapping.getAsString();
    }

    public List<OptionMapping> getOptionsByType(OptionType optionType) {
        return getEvent().getOptionsByType(optionType);
    }

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }

    public Cache getCache() {
        return cache;

    }

    public GuildCache getGuildCache() {
        return cache.getGuildCache();
    }

    public void retrieveRecord(Consumer<GuildDataRecord> record, Consumer<Throwable> throwable) {
        final long guildId = getGuild().getIdLong();
        getGuildCache().retrieve(guildId, record, throwable);
    }

    public Logger getLogger() {
        return logger;
    }
}
