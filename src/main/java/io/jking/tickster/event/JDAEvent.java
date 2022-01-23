package io.jking.tickster.event;

import io.jking.tickster.core.Tickster;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class JDAEvent implements EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);
        else if (event instanceof ReconnectedEvent)
            onReconnected((ReconnectedEvent) event);
        else if (event instanceof DisconnectEvent)
            onDisconnect((DisconnectEvent) event);
        else if (event instanceof ShutdownEvent)
            onShutdown((ShutdownEvent) event);
    }

    private void onReady(ReadyEvent event) {
        Tickster.getLogger().info("Tickster ready!");
    }

    private void onReconnected(ReconnectedEvent event) {
        Tickster.getLogger().info("Tickster reconnected!");
    }

    private void onDisconnect(DisconnectEvent event) {
        Tickster.getLogger().warn("Tickster disconnected!");
    }

    private void onShutdown(ShutdownEvent event) {
        Tickster.getLogger().warn("Tickster shutdown!");
    }

}
