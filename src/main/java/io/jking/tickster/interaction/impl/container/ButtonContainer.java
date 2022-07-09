package io.jking.tickster.interaction.impl.container;

import io.jking.tickster.interaction.InteractionContainer;
import io.jking.tickster.interaction.impl.sender.ButtonSender;

public abstract class ButtonContainer extends InteractionContainer<ButtonSender> {

    private final boolean requiresTicketMaster;

    public ButtonContainer(String interactionKey, String interactionDescription, boolean requiresTicketMaster) {
        super(interactionKey, interactionDescription);
        this.requiresTicketMaster = requiresTicketMaster;
    }

    public boolean requiresTicketMaster() {
        return requiresTicketMaster;
    }
}
