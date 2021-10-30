package io.jking.untitled.command;

import io.jking.untitled.cache.Cache;
import io.jking.untitled.cache.impl.GuildCache;
import io.jking.untitled.command.error.CommandError;
import io.jking.untitled.core.Config;
import io.jking.untitled.event.MessageEvent;
import io.jking.untitled.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class CommandContext {

    private final SlashCommandEvent event;
    private final Config config;
    private final MessageEvent messageEvent;
    private final Cache cache;

    public CommandContext(SlashCommandEvent event, Config config, MessageEvent messageEvent, Cache cache) {
        this.event = event;
        this.config = config;
        this.messageEvent = messageEvent;
        this.cache = cache;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    public Config getConfig() {
        return config;
    }

    public JDA getJDA() {
        return getEvent().getJDA();
    }

    public Guild getGuild() {
        return getEvent().getGuild();
    }

    public Member getMember() {
        return getEvent().getMember();
    }

    public User getAuthor() {
        return getMember().getUser();
    }

    public InteractionHook getHook() {
        return getEvent().getHook();
    }

    public TextChannel getChannel() {
        return getEvent().getTextChannel();
    }

    public Member getSelf() {
        return getGuild().getSelfMember();
    }

    public User getSelfUser() {
        return getSelf() == null ? null : getSelf().getUser();
    }

    public MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public String getSubcommand() {
        return getEvent().getSubcommandName();
    }

    public String getStringOption(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsString();
    }

    public TextChannel getChannelOption(String name) {
        return getMapping(name) == null ? null : (TextChannel) getMapping(name).getAsMessageChannel();
    }

    public User getUserOption(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsUser();
    }

    public IMentionable getMentionableOption(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsMentionable();
    }

    public boolean getBooleanOption(String name) {
        if (getMapping(name) == null)
            return false;
        return getMapping(name).getAsBoolean();
    }

    public long getLongOption(String name) {
        return getMapping(name) == null ? 0L : getMapping(name).getAsLong();
    }

    public Member getMemberOption(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsMember();
    }

    public ReplyAction reply(String content) {
        return getEvent().reply(content);
    }

    public ReplyAction reply(MessageEmbed embed) {
        return getEvent().replyEmbeds(embed);
    }

    public ReplyAction replySuccess(EmbedBuilder embedBuilder) {
        return reply(embedBuilder.build());
    }

    public ReplyAction replyError(CommandError error, Object... objects) {
        return reply(EmbedUtil.getError(error, objects).build());
    }

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }

    public GuildCache getGuildCache() {
        return cache.getGuildCache();
    }
}
