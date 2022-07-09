package io.jking.tickster.interaction;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public abstract class InteractionContainer<T extends InteractionSender<? extends GenericInteractionCreateEvent>> {

    private final String interactionKey;
    private final String interactionDescription;

    public InteractionContainer(String interactionKey, String interactionDescription) {
        this.interactionKey = interactionKey;
        this.interactionDescription = interactionDescription;
    }

    public abstract void onInteraction(T sender);

    public String getInteractionKey() {
        return interactionKey.toLowerCase();
    }

    public String getInteractionDescription() {
        return interactionDescription;
    }
}
