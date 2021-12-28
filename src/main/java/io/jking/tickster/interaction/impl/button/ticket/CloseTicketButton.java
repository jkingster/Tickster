package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.object.CButton;

public class CloseTicketButton implements IButton {
    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(hook -> context.getChannel().sendMessage("Are you sure you want to close this ticket?")
                .setActionRow(
                        CButton.SUCCESS.format("yes:ticket_close", "Yes"),
                        CButton.DANGER.format("no:ticket_close", "Cancel")
                )
                .queue());
    }

    @Override
    public String componentId() {
        return "close_ticket";
    }

}
