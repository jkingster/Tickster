package io.jking.tickster.interaction.button;

import io.jking.tickster.interaction.button.impl.GarbageButton;
import io.jking.tickster.interaction.button.impl.ticket.CreateTicketButton;
import io.jking.tickster.interaction.core.Registry;

public class ButtonRegistry extends Registry<AbstractButton> {

    public ButtonRegistry() {
        put("button:garbage", new GarbageButton());
        put("button:create_ticket", new CreateTicketButton());
    }

}
