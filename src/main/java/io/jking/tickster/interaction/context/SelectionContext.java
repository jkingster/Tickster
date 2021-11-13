package io.jking.tickster.interaction.context;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.command.CommandRegistry;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.AbstractContext;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class SelectionContext extends AbstractContext<SelectionMenuEvent> {
    public SelectionContext(SelectionMenuEvent interactionEvent, CommandRegistry registry, Database database, Cache cache) {
        super(interactionEvent, registry, database, cache);
    }

    public SelectOption getSelectedOption() {
        if (getEvent().getSelectedOptions() == null)
            return null;

        if (getEvent().getSelectedOptions().isEmpty())
            return null;

        return getEvent().getSelectedOptions().get(0);
    }

}
