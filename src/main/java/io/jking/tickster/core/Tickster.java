package io.jking.tickster.core;

import io.jking.tickster.database.JooqConnector;
import io.jking.tickster.event.GuildEvent;
import io.jking.tickster.event.JDAEvent;
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
import java.io.IOException;
import java.util.Arrays;

public class Tickster {

    private static final Logger logger = LoggerFactory.getLogger(Tickster.class);

    private final Config config;
    private final JooqConnector jooqConnector;

    private ShardManager shardManager;

    public Tickster() {
        this.config = Config.getInstance();
        this.jooqConnector = JooqConnector.getInstance();
    }

    public static Logger getLogger() {
        return logger;
    }

    public void start() throws LoginException, IOException {
        logger.info("Starting Tickster!");
        jooqConnector.createTables();
        configureShardManager();
    }

    private void configureShardManager() throws LoginException {
        this.shardManager = DefaultShardManagerBuilder.createDefault(config.getString("token"))
                .addEventListeners(new JDAEvent(), new GuildEvent())
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_INVITES)
                .disableCache(Arrays.asList(CacheFlag.values()))
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.watching(" for new tickets."))
                .build();
    }

    public Config getConfig() {
        return config;
    }

    public JooqConnector getJooqConnector() {
        return jooqConnector;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
