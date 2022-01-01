package io.jking.tickster.cache;

import io.jking.tickster.cache.impl.BlacklistCache;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.database.Database;

public class CacheManager {

    private final BlacklistCache blacklistCache;
    private final GuildCache guildCache;
    private final TicketCache ticketCache;

    public CacheManager(Database database) {
        this.blacklistCache = new BlacklistCache(database);
        this.guildCache = new GuildCache(database);
        this.ticketCache = new TicketCache(database);
    }

    public GuildCache getGuildCache() {
        return guildCache;
    }

    public TicketCache getTicketCache() {
        return ticketCache;
    }

    public BlacklistCache getBlacklistCache() {
        return blacklistCache;
    }
}
