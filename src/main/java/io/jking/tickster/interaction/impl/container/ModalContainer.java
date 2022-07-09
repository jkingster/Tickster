package io.jking.tickster.interaction.impl.container;

import io.jking.tickster.interaction.InteractionContainer;
import io.jking.tickster.interaction.impl.sender.ModalSender;

public abstract class ModalContainer extends InteractionContainer<ModalSender> {
    public ModalContainer(String interactionKey, String interactionDescription) {
        super(interactionKey, interactionDescription);
    }
}
