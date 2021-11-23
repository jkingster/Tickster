package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.Arrays;

public class DeleteTicketButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(deferred -> {
            context.getTicketCache().delete(context.getChannel().getIdLong());
            context.getChannel().delete().queue(null, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
        });
    }

    @Override
    public String componentId() {
        return "ticket_delete";
    }
}
