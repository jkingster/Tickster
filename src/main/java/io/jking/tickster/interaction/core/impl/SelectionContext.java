package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.interaction.core.InteractionContext;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public class SelectionContext extends InteractionContext<SelectionMenuEvent> {
    public SelectionContext(SelectionMenuEvent event) {
        super(event);
    }
}
