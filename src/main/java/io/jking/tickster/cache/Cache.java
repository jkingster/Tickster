package io.jking.tickster.cache;

import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.database.Database;

public class Cache {

    private final GuildCache guildCache;

    public Cache(Database database) {
        this.guildCache = new GuildCache(database);
    }

    public GuildCache getGuildCache() {
        return guildCache;
    }
}
