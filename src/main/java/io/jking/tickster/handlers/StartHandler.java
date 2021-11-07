package io.jking.tickster.handlers;

import io.jking.tickster.core.Tickster;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class StartHandler implements EventListener {

    private final Tickster tickster;

    public StartHandler(Tickster tickster) {
        this.tickster = tickster;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);
    }

    private void onReady(ReadyEvent event) {
    }
}
