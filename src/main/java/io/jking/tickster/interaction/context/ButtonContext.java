package io.jking.tickster.interaction.context;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.ReportCache;
import io.jking.tickster.command.CommandRegistry;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.AbstractContext;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;


public class ButtonContext extends AbstractContext<ButtonClickEvent> {

    private final Cache cache;

    public ButtonContext(ButtonClickEvent interactionEvent, CommandRegistry registry, Database database, Cache cache) {
        super(interactionEvent, registry, database, cache);
        this.cache = cache;
    }

    public ReportCache getReportCache() {
        return cache.getReportCache();
    }
}
