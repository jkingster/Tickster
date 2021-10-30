package io.jking.untitled.cache;

import io.jking.untitled.cache.impl.GuildCache;
import net.dv8tion.jda.api.JDA;

public class Cache {

    private final JDA jda;

    private final GuildCache guildCache;

    public Cache(JDA jda) {
        this.jda = jda;

        final int guilds = jda.getGuilds().size();

        this.guildCache = new GuildCache();
    }
}
