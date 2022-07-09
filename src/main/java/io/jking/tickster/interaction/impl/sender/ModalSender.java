package io.jking.tickster.interaction.impl.sender;

import io.jking.tickster.interaction.InteractionSender;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

public class ModalSender extends InteractionSender<ModalInteractionEvent> {
    public ModalSender(ModalInteractionEvent event) {
        super(event);
    }

    private ModalMapping getMapping(String id) {
        return getEvent().getValue(id);
    }

    public String getStringOption(String id) {
        return getMapping(id) != null
                ? getMapping(id).getAsString()
                : null;
    }

}
