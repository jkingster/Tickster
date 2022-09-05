package io.jking.tickster.cache;

import io.jking.tickster.cache.impl.BlacklistCache;
import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.database.impl.BlacklistRepo;
import io.jking.tickster.database.impl.GuildRepo;

public class CachedManager {

    private static final CachedManager instance = new CachedManager();

    private final GuildCache     guildCache;
    private final TicketCache    ticketCache;
    private final BlacklistCache blacklistCache;

    private CachedManager() {
        this.guildCache = new GuildCache();
        this.ticketCache = new TicketCache();
        this.blacklistCache = new BlacklistCache();
    }

    public static GuildCache guildCache() {
        return instance.guildCache;
    }

    public static TicketCache ticketCache() {
        return instance.ticketCache;
    }

    public static BlacklistCache blacklistCache() {
        return instance.blacklistCache;
    }

    public static CachedManager getInstance() {
        if (instance == null)
            return new CachedManager();
        return instance;
    }

}
