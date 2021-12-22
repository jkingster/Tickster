package io.jking.tickster.buttons.ticket;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonContext;

public class CreateTicketButton extends AbstractButton {

    public CreateTicketButton() {
        super("button:create_ticket");
    }

    @Override
    public void onButtonPress(ButtonContext context) {
        context.replyEphemeral("I am creating your ticket now... please wait.").queue();
    }
}
