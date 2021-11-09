package io.jking.tickster.objects.cache;

import io.jking.tickster.database.Database;
import io.jking.tickster.objects.cache.impl.GuildCache;

public class Cache {

    private final GuildCache guildCache;

    public Cache(Database database) {
        this.guildCache = new GuildCache(database);
    }

    public GuildCache getGuildCache() {
        return guildCache;
    }
}
