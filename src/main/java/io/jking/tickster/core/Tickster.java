package io.jking.tickster.core;

import io.jking.tickster.event.GuildEvent;
import io.jking.tickster.event.InteractionEvent;
import io.jking.tickster.event.JDAEvent;
import io.jking.tickster.interaction.InteractionRegistry;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Tickster {
    private final Config              config;
    private final InteractionRegistry interactionRegistry;
    private       ShardManager        shardManager;

    public Tickster(Config config) throws LoginException {
        this.config = config;
        this.interactionRegistry = new InteractionRegistry();
        this.shardManager = getShardManager();
    }

    public void start() throws LoginException {
        this.shardManager = DefaultShardManagerBuilder.createDefault(config.getString("token"))
                .setShardsTotal(-1)
                .addEventListeners(new JDAEvent(interactionRegistry))
                .disableCache(Arrays.asList(CacheFlag.values()))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.watching(" for new tickets."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }

    public Config getConfig() {
        return config;
    }

    public InteractionRegistry getInteractionRegistry() {
        return interactionRegistry;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }


}
