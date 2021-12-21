package io.jking.tickster.interaction.core.impl;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.core.InteractionContext;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public class ButtonContext extends InteractionContext<ButtonClickEvent> {
    public ButtonContext(ButtonClickEvent event, Database database, CacheManager cache) {
        super(event, database, cache);
    }
}
