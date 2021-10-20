package io.jking.untitled.core;


import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.event.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.Checks;

import javax.security.auth.login.LoginException;

public class Untitled {

    private final Config config;

    private Untitled(Config config) throws LoginException, InterruptedException {
        this.config = config;
        startInstance();
    }

    public static void build(Config config) throws LoginException, InterruptedException {
        new Untitled(config);
    }

    private void startInstance() throws LoginException, InterruptedException {
        Checks.notNull(config, "Config");

        final String token = config.getObject("bot").getString("token");
        Checks.notEmpty(token, "Token");

        final CommandRegistry commandRegistry = new CommandRegistry();

        MessageEvent messageEvent = new MessageEvent(config);

        JDABuilder.createDefault(token)
                .setEnabledIntents(GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE)
                .addEventListeners(
                        new SlashEvent(commandRegistry, config, messageEvent),
                        new StartEvent(commandRegistry),
                        new InteractionEvent(commandRegistry),
                        new InviteEvent(config),
                        messageEvent
                )
                .build()
                .awaitReady();
    }
}
