package io.jking.tickster.interaction.button.impl.ticket.manage;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonSender;

import java.util.concurrent.TimeUnit;

public class DeleteTicketManagerButton extends AbstractButton {

    public DeleteTicketManagerButton() {
        super("button:manage:delete_ticket");
    }

    @Override
    public void onButtonPress(ButtonSender context) {
        final long ticketId = context.getTextChannel().getIdLong();

        context.reply("Deleting this ticket now.")
                .delay(5, TimeUnit.SECONDS)
                .flatMap(hook -> context.getTextChannel().delete())
                .queue();

        context.getTicketCache().delete(ticketId);
    }
}
