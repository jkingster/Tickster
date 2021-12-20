package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.interaction.core.InteractionContext;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public class ButtonContext extends InteractionContext<ButtonClickEvent> {
    public ButtonContext(ButtonClickEvent event) {
        super(event);
    }
}
