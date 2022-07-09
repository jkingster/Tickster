package io.jking.tickster.interaction.impl.sender;

import io.jking.tickster.interaction.InteractionSender;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ButtonSender extends InteractionSender<ButtonInteractionEvent> {
    public ButtonSender(ButtonInteractionEvent event) {
        super(event);
    }

}
