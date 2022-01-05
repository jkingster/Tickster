package io.jking.tickster.interaction.button.impl.ticket.manage;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.impl.ButtonSender;

import java.util.concurrent.TimeUnit;

public class DeleteTicketManagerButton extends AbstractButton {

    public DeleteTicketManagerButton() {
        super("button:manage:delete_ticket");
    }

    @Override
    public void onButtonPress(ButtonSender sender) {
        final long ticketId = sender.getTextChannel().getIdLong();

        sender.reply("Deleting this ticket now.")
                .delay(5, TimeUnit.SECONDS)
                .flatMap(hook -> sender.getTextChannel().delete())
                .queue();

        sender.getTicketCache().delete(ticketId);
    }
}
