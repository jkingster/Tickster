package io.jking.untitled.core;


import io.jking.untitled.cache.Cache;
import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.event.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.Checks;

import javax.security.auth.login.LoginException;

public class Untitled {

    private final Config config;

    private boolean devMode;

    private Untitled(Config config, boolean devMode) throws LoginException, InterruptedException {
        this.config = config;
        this.devMode = devMode;
        startInstance();
    }

    private Untitled(Config config) throws LoginException, InterruptedException {
        this.config = config;
        startInstance();
    }

    public static void build(Config config) throws LoginException, InterruptedException {
        new Untitled(config);
    }

    public static void build(Config config, boolean devMode) throws LoginException, InterruptedException {
        new Untitled(config, devMode);
    }

    private void startInstance() throws LoginException, InterruptedException {
        Checks.notNull(config, "Config");

        final String token = config.getObject("bot").getString("token");
        Checks.notEmpty(token, "Token");

        final Cache cache = new Cache();
        final CommandRegistry commandRegistry = new CommandRegistry();

        JDABuilder.createDefault(token)
                .setEnabledIntents(GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE)
                .addEventListeners(
                        new MessageEvent(cache),
                        new SlashEvent(commandRegistry, config, cache),
                        new GuildEvent(commandRegistry, cache, this),
                        new InteractionEvent(commandRegistry),
                        new InviteEvent(cache)
                )
                .build()
                .awaitReady();

    }

    public boolean isDevMode() {
        return devMode;
    }

    public Config getConfig() {
        return config;
    }
}
