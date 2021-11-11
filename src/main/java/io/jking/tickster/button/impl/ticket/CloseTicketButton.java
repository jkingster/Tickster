package io.jking.tickster.button.impl.ticket;

import io.jking.tickster.button.ButtonContext;
import io.jking.tickster.button.IButton;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public class CloseTicketButton implements IButton {
    @Override
    public void onButtonPress(ButtonContext context) {
        context.getInteraction().deferEdit().queue(hook -> context.getChannel().sendMessage("Are you sure you want to close this ticket?")
                .setActionRow(
                        Button.of(ButtonStyle.SUCCESS, "yes:ticket_close", "Yes"),
                        Button.of(ButtonStyle.DANGER, "no:ticket_close", "Cancel")
                )
                .queue());
    }

    @Override
    public String buttonId() {
        return "close_ticket";
    }

}
