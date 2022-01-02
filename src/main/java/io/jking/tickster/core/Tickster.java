package io.jking.tickster.core;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.database.Database;
import io.jking.tickster.event.GuildEvent;
import io.jking.tickster.event.InteractionEvent;
import io.jking.tickster.event.InviteEvent;
import io.jking.tickster.event.JDAEvent;
import io.jking.tickster.interaction.button.ButtonRegistry;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.select.SelectRegistry;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Tickster {

    private static final Logger logger = LoggerFactory.getLogger(Tickster.class);

    private final Config config;
    private final CommandRegistry commandRegistry;
    private final ButtonRegistry buttonRegistry;
    private final SelectRegistry selectRegistry;
    private final Database database;
    private final CacheManager cacheManager;
    private final ShardManager shardManager;

    public Tickster(String configPath) throws FileNotFoundException, LoginException {
        this.config = new Config(configPath);
        this.commandRegistry = new CommandRegistry();
        this.buttonRegistry = new ButtonRegistry();
        this.selectRegistry = new SelectRegistry(commandRegistry);
        this.database = new Database(config);
        this.cacheManager = new CacheManager(database);
        this.shardManager = buildShardManager();
    }

    public static Logger getLogger() {
        return logger;
    }

    private ShardManager buildShardManager() throws LoginException {
        final String token = config.getString("token");
        return DefaultShardManagerBuilder.createDefault(token)
                .addEventListeners(
                        new JDAEvent(commandRegistry),
                        new InteractionEvent(
                                this,
                                commandRegistry,
                                buttonRegistry,
                                selectRegistry,
                                database,
                                cacheManager
                        ),
                        new GuildEvent(
                                commandRegistry,
                                cacheManager.getGuildCache(),
                                cacheManager.getTicketCache(),
                                cacheManager.getBlacklistCache()
                        ),
                        new InviteEvent(
                                cacheManager.getGuildCache(),
                                cacheManager.getInviteCache()
                        )
                )
                .disableCache(Arrays.asList(CacheFlag.values()))
                .setShardsTotal(-1)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_INVITES
                )
                .setMemberCachePolicy(MemberCachePolicy.OWNER)
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
