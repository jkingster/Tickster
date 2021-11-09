package io.jking.tickster.button.impl.ticket;

import io.jking.tickster.button.ButtonContext;
import io.jking.tickster.button.IButton;
import net.dv8tion.jda.api.Permission;

public class ReOpenTicketButton implements IButton {
    @Override
    public void onButtonPress(ButtonContext context) {
        context.getInteraction().deferEdit().queue(success -> context.getChannel().upsertPermissionOverride(context.getMember())
                .setAllow(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL)
                .flatMap(ignored -> context.getHook().deleteOriginal())
                .queue());
    }
    @Override
    public String buttonId() {
        return "ticket_reopen";
    }
}
