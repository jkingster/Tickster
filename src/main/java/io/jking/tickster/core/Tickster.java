package io.jking.tickster.core;

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

    private final CommandRegistry commandRegistry = new CommandRegistry();

    private final Config config;
    private final ShardManager shardManager;

    public Tickster(String configPath) throws FileNotFoundException, LoginException {
        this.config = new Config(configPath);
        this.shardManager = buildShardManager();
    }

    private ShardManager buildShardManager() throws LoginException {
        final String token = config.getString("token");
        return DefaultShardManagerBuilder.createDefault(token)
                .addEventListeners(new InteractionEvent(commandRegistry), new MiscEvent())
                .disableCache(Arrays.asList(CacheFlag.values()))
                .setShardsTotal(-1)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setActivity(Activity.watching(" for new tickets."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

}
