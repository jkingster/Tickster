package io.jking.tickster.event;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class MiscEvent implements EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
    }

    private void onGuildReady(GuildReadyEvent event) {

    }


}
