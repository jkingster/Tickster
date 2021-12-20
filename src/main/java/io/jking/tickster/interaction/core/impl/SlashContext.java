package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.interaction.core.InteractionContext;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SlashContext extends InteractionContext<SlashCommandEvent> {
    public SlashContext(SlashCommandEvent event) {
        super(event);
    }

    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }

    public String getStringOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? null : mapping.getAsString();
    }

    public boolean getBooleanOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping != null && mapping.getAsBoolean();
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
