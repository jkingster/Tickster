package io.jking.untitled.cache;

import io.jking.untitled.cache.impl.GuildCache;
import net.dv8tion.jda.api.JDA;

public class Cache {

    private final GuildCache guildCache;

    public Cache() {
        this.guildCache = new GuildCache();
    }

    public GuildCache getGuildCache() {
        return guildCache;
    }
}
