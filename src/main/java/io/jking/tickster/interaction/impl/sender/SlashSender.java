package io.jking.tickster.interaction.impl.sender;

import io.jking.tickster.interaction.InteractionSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashSender extends InteractionSender<SlashCommandInteractionEvent> {
    public SlashSender(SlashCommandInteractionEvent event) {
        super(event);
    }

    void reply(String content) {
        getEvent().reply("lol").queue();
    }
}
