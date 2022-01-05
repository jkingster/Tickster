package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.core.Tickster;
import io.jking.tickster.interaction.core.InteractionSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class SlashSender extends InteractionSender<SlashCommandInteractionEvent> {

    private final boolean ephemeral;

    public SlashSender(Tickster tickster, SlashCommandInteractionEvent event, boolean ephemeral) {
        super(tickster, event);
        this.ephemeral = ephemeral;
    }

    public ReplyCallbackAction reply(String content) {
        return getEvent().reply(content).setEphemeral(ephemeral);
    }

    public ReplyCallbackAction replyFormat(String pattern, Object... objects) {
        return reply(String.format(pattern, objects));
    }

    public ReplyCallbackAction reply(EmbedBuilder embed) {
        return getEvent().replyEmbeds(embed.build()).setEphemeral(ephemeral);
    }

    public ReplyCallbackAction reply(Error error, Object... objects) {
        return reply(EmbedUtil.getError(error, objects));
    }

    public ReplyCallbackAction reply(Success success, Object... objects) {
        return reply(EmbedUtil.getSuccess(success, objects));
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

    public Boolean getBooleanOption(String name) {
        final OptionMapping mapping = getMapping(name);
        return mapping == null ? null : mapping.getAsBoolean();
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
