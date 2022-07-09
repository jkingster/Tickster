package io.jking.tickster.event;

import io.jking.tickster.interaction.InteractionRegistry;
import io.jking.tickster.logging.LogType;
import io.jking.tickster.logging.TicksterLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class JDAEvent implements EventListener {

    private final InteractionRegistry interactionRegistry;

    public JDAEvent(InteractionRegistry interactionRegistry) {
        this.interactionRegistry = interactionRegistry;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);

        if (event instanceof DisconnectEvent)
            onDisconnect((DisconnectEvent) event);

        if (event instanceof ReconnectedEvent)
            onReconnect((ReconnectedEvent) event);

        if (event instanceof ShutdownEvent)
            onShutdown((ShutdownEvent) event);
    }

    private void onReady(ReadyEvent event) {
        TicksterLogger.log(LogType.TICKSTER_READY);

        JDA shard = event.getJDA();
        int shardId = shard.getShardInfo().getShardId();

        if (shardId == 0) {
            interactionRegistry.registerInteractions(shard);
        }

        shard.addEventListener(new GuildEvent(), new InteractionEvent(interactionRegistry));
    }

    private void onDisconnect(DisconnectEvent event) {
        TicksterLogger.warn(LogType.TICKSTER_DISCONNECTED);
    }

    private void onReconnect(ReconnectedEvent event) {
        TicksterLogger.log(LogType.TICKSTER_RECONNECTED);
    }

    private void onShutdown(ShutdownEvent event) {
        TicksterLogger.warn(LogType.TICKSTER_SHUTDOWN);
    }
}
