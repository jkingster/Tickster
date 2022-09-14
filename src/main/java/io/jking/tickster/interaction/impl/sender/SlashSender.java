package io.jking.tickster.interaction.impl.sender;

import io.jking.tickster.interaction.InteractionSender;
import io.jking.tickster.interaction.response.Failure;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.function.Consumer;

public class SlashSender extends InteractionSender<SlashCommandInteractionEvent> {
    public SlashSender(SlashCommandInteractionEvent event) {
        super(event);
    }

    public InteractionHook getHook() {
        return getEvent().getHook();
    }


    private OptionMapping getMapping(String name) {
        return getEvent().getOption(name);
    }

    public GuildChannelUnion getChannel(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsChannel();
    }

    public Role getRole(String name) {
        return getMapping(name) == null ? null : getMapping(name).getAsRole();
    }

    public long getLong(String name) {
        return getMapping(name) == null ? 0L : getMapping(name).getAsLong();
    }

    public ReplyCallbackAction reply(String content, Object... objects) {
        return getEvent().replyFormat(content, objects);
    }

    public ReplyCallbackAction reply(EmbedBuilder embed) {
        return getEvent().replyEmbeds(embed.build());
    }

    public ReplyCallbackAction reply(Failure failure, Object... objects) {
        return reply(failure.getEmbed(objects));
    }

    public ReplyCallbackAction deferReply(boolean ephemeral) {
        return getEvent().deferReply(ephemeral);
    }



}
