package io.jking.tickster.interaction.impl.slash.object;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.AbstractContext;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public class SelectionContext extends AbstractContext<SelectionMenuEvent> {
    public SelectionContext(SelectionMenuEvent interactionEvent, Database database, Cache cache) {
        super(interactionEvent, database, cache);
    }
}
