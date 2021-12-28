package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;


public class DeleteTicketButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(deferred -> {
            context.getTicketCache().delete(context.getChannel().getIdLong());
            context.getChannel().delete().queue();
        });
    }

    @Override
    public String componentId() {
        return "ticket_delete";
    }
}
