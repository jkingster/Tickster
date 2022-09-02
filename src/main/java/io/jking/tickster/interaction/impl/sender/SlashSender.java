package io.jking.tickster.interaction.impl.sender;

import io.jking.tickster.interaction.InteractionSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class SlashSender extends InteractionSender<SlashCommandInteractionEvent> {
    public SlashSender(SlashCommandInteractionEvent event) {
        super(event);
    }

    public InteractionHook getHook() {
        return getEvent().getHook();
    }

    public ReplyCallbackAction reply(String content) {
        return getEvent().reply(content);
    }
}
