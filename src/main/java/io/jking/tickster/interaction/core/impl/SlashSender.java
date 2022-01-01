package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.core.Tickster;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.InteractionSender;
import io.jking.tickster.interaction.core.reply.ICommandReply;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SlashSender extends InteractionSender<SlashCommandInteractionEvent> implements ICommandReply<SlashCommandInteractionEvent> {
    public SlashSender(Tickster tickster, SlashCommandInteractionEvent event, Database database, CacheManager cache) {
        super(tickster, event, database, cache);
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


}
