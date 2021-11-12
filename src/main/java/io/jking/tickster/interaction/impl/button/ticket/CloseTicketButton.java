package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public class CloseTicketButton implements IButton {
    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(hook -> context.getChannel().sendMessage("Are you sure you want to close this ticket?")
                .setActionRow(
                        Button.of(ButtonStyle.SUCCESS, "yes:ticket_close", "Yes"),
                        Button.of(ButtonStyle.DANGER, "no:ticket_close", "Cancel")
                )
                .queue());
    }

    @Override
    public String componentId() {
        return "close_ticket";
    }

}
