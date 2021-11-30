package io.jking.tickster.handler;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.cache.impl.InviteCache;
import io.jking.tickster.object.InviteData;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class InviteHandler implements EventListener {

    private final InviteCache cache;

    public InviteHandler(Cache cache) {
        this.cache = cache.getInviteCache();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildInviteCreateEvent)
            onInviteCreate((GuildInviteCreateEvent) event);
        else if (event instanceof GuildInviteDeleteEvent)
            onInviteDelete((GuildInviteDeleteEvent) event);
    }

    private void onInviteCreate(GuildInviteCreateEvent event) {
        cache.put(new InviteData(event.getInvite()));
    }

    private void onInviteDelete(GuildInviteDeleteEvent event) {
        cache.delete(event.getCode());
    }
}
