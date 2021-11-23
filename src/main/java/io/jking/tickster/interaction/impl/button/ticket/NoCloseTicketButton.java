package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class NoCloseTicketButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(hook -> hook.editOriginal("Okay, I won't close this ticket!")
                .setActionRows(Collections.emptyList())
                .delay(8, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue());
    }

    @Override
    public String componentId() {
        return "no:ticket_close";
    }
}
