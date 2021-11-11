package io.jking.tickster.interaction.button.impl.ticket;

import io.jking.tickster.interaction.button.object.ButtonContext;
import io.jking.tickster.interaction.button.object.IButton;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class NoCloseTicketButton implements IButton {


    @Override
    public void onButtonPress(ButtonContext context) {
        context.getInteraction().deferEdit().queue(hook -> hook.editOriginal("Okay, I won't close this ticket!")
                .setActionRows(Collections.emptyList())
                .delay(8, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue());
    }

    @Override
    public String buttonId() {
        return "no:ticket_close";
    }
}
