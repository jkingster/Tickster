package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.IReply;
import io.jking.tickster.interaction.core.InteractionSender;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class SlashSender extends InteractionSender<SlashCommandInteractionEvent> implements IReply {
    public SlashSender(SlashCommandInteractionEvent event, Database database, CacheManager cache) {
        super(event, database, cache);
    }

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }

    public String getSubCommandName() {
        return getEvent().getSubcommandName();
    }

    public String getStringOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? null : mapping.getAsString();
    }

    public boolean getBooleanOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping != null && mapping.getAsBoolean();
    }

    public TextChannel getChannelOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? null : (TextChannel) mapping.getAsMessageChannel();
    }

    public Role getRoleOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? null : mapping.getAsRole();
    }

    public User getUserOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? null : mapping.getAsUser();
    }

    public long getLongOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? 0L : mapping.getAsLong();
    }

    @Override
    public InteractionHook getHook() {
        return getEvent().getHook();
    }

    public ReplyCallbackAction deferReply() {
        return getEvent().deferReply();
    }

}
