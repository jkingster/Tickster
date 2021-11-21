package io.jking.tickster.cache;

import io.jking.tickster.cache.impl.GuildCache;
import io.jking.tickster.cache.impl.ReportCache;
import io.jking.tickster.database.Database;

public class Cache {

    private final GuildCache guildCache;
    private final ReportCache reportCache;


    public Cache(Database database) {
        this.guildCache = new GuildCache(database);
        this.reportCache = new ReportCache(database);

    }

    public GuildCache getGuildCache() {
        return guildCache;
    }

    public ReportCache getReportCache() {
        return reportCache;
    }
}
