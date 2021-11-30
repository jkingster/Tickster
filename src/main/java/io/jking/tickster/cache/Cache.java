package io.jking.tickster.cache;

import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.InviteCache;
import io.jking.tickster.cache.impl.ReportCache;
import io.jking.tickster.cache.impl.TicketCache;
import io.jking.tickster.database.Database;

public class Cache {

    private final GuildCache guildCache;
    private final ReportCache reportCache;
    private final TicketCache ticketCache;
    private final InviteCache inviteCache;

    public Cache(Database database) {
        this.guildCache = new GuildCache(database);
        this.reportCache = new ReportCache(database);
        this.ticketCache = new TicketCache(database);
        this.inviteCache = new InviteCache();
    }

    public GuildCache getGuildCache() {
        return guildCache;
    }

    public ReportCache getReportCache() {
        return reportCache;
    }

    public TicketCache getTicketCache() {
        return ticketCache;
    }

    public InviteCache getInviteCache() {
        return inviteCache;
    }
}
