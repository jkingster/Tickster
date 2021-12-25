package io.jking.tickster.interaction.button;

import io.jking.tickster.interaction.button.impl.GarbageButton;
import io.jking.tickster.interaction.button.impl.ticket.CloseTicketButton;
import io.jking.tickster.interaction.button.impl.ticket.CreateTicketButton;
import io.jking.tickster.interaction.button.impl.ticket.OpenTicketButton;
import io.jking.tickster.interaction.button.impl.ticket.manage.CloseTicketManagerButton;
import io.jking.tickster.interaction.core.Registry;

public class ButtonRegistry extends Registry<AbstractButton> {

    public ButtonRegistry() {
        put("button:garbage:id:%s", new GarbageButton());
        put("button:create_ticket", new CreateTicketButton());
        put("button:close_ticket:id:%s", new CloseTicketButton());
        put("button:open_ticket:id:%s", new OpenTicketButton());
        put("button:manage:close_ticket", new CloseTicketManagerButton());
    }

}
