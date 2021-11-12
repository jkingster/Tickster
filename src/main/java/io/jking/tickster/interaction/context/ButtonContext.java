package io.jking.tickster.interaction.context;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.AbstractContext;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;


public class ButtonContext extends AbstractContext<ButtonClickEvent> {
    public ButtonContext(ButtonClickEvent interactionEvent, Database database, Cache cache) {
        super(interactionEvent, database, cache);
    }
}
