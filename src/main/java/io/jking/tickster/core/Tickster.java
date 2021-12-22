package io.jking.tickster.core;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.event.InteractionEvent;
import io.jking.tickster.event.MiscEvent;
import io.jking.tickster.interaction.command.CommandRegistry;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Tickster {

    private final Config config;
    private final CommandRegistry commandRegistry;
    private final Database database;
    private final CacheManager cacheManager;
    private final ShardManager shardManager;

    public Tickster(String configPath) throws FileNotFoundException, LoginException {
        this.config = new Config(configPath);
        this.commandRegistry = new CommandRegistry();
        this.database = new Database(config);
        this.cacheManager = new CacheManager(database);
        this.shardManager = buildShardManager();
    }

    private ShardManager buildShardManager() throws LoginException {
        final String token = config.getString("token");
        return DefaultShardManagerBuilder.createDefault(token)
                .addEventListeners(
                        new InteractionEvent(commandRegistry, database, cacheManager),
                        new MiscEvent(cacheManager.getGuildCache())
                )
                .disableCache(Arrays.asList(CacheFlag.values()))
                .setShardsTotal(-1)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setActivity(Activity.watching(" for new tickets."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }

    public Config getConfig() {
        return config;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public Database getDatabase() {
        return database;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
