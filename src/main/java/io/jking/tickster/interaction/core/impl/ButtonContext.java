package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.InteractionContext;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;

public class ButtonContext extends InteractionContext<ButtonClickEvent> {
    public ButtonContext(ButtonClickEvent event, Database database, CacheManager cache) {
        super(event, database, cache);
    }


    public UpdateInteractionAction deferEdit() {
        return getEvent().deferEdit();
    }

    public String getComponentId() {
        return getEvent().getComponentId();
    }
}
