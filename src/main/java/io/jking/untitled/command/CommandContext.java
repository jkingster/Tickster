package io.jking.untitled.command;

import io.jking.untitled.core.Config;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class CommandContext {

    private final SlashCommandEvent event;
    private final Config config;

    public CommandContext(SlashCommandEvent event, Config config) {
        this.event = event;
        this.config = config;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    public Config getConfig() {
        return config;
    }

    private Guild getGuild() {
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

    public String getStringOption(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsString();
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

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }
}
