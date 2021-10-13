package io.jking.untitled.core;


import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.event.InteractionEvent;
import io.jking.untitled.event.SlashEvent;
import io.jking.untitled.event.StartEvent;
import net.dv8tion.jda.api.JDABuilder;
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

        JDABuilder.createDefault(token)
                .addEventListeners(new SlashEvent(commandRegistry, config), new StartEvent(commandRegistry),
                        new InteractionEvent(commandRegistry))
                .build()
                .awaitReady();
    }
}
