package io.jking.tickster.objects.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class CommandContext {

    private final SlashCommandEvent event;

    public CommandContext(SlashCommandEvent event) {
        this.event = event;
    }

    public ReplyAction reply(EmbedBuilder embedBuilder) {
        return event.replyEmbeds(embedBuilder.build());
    }

    public ReplyAction reply(String content) {
        return event.reply(content);
    }
}
