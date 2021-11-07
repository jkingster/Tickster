package io.jking.tickster.core;

import io.jking.tickster.handlers.InteractionHandler;
import io.jking.tickster.handlers.StartHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.Checks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Tickster {

    private final Logger logger = LoggerFactory.getLogger(Tickster.class);

    private final DataObject data;
    private final JDA jda;

    private boolean devMode;

    private Tickster(String configPath) throws IOException, LoginException, InterruptedException {
        this.data = loadConfig(configPath);
        this.jda = startTickster(false);
    }

    private Tickster(String configPath, boolean isDev) throws IOException, LoginException, InterruptedException {
        this.data = loadConfig(configPath);
        this.jda = startTickster(isDev);
    }

    public static Tickster buildDefault(String configPath) throws IOException, LoginException, InterruptedException {
        return new Tickster(configPath);
    }

    public static Tickster buildDev(String configPath) throws IOException, LoginException, InterruptedException {
        return new Tickster(configPath, true);
    }

    private DataObject loadConfig(String configPath) throws IOException {
        Checks.notNull(configPath, "Configuration Path.");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configPath))) {
            return DataObject.fromJson(bufferedReader);
        }
    }

    private JDA startTickster(boolean devMode) throws LoginException, InterruptedException {
        logger.info("Building Tickster...");
        final String token = data.getObject("bot").getString("token", null);
        Checks.notNull(token, "Config Token");

        this.devMode = devMode;

        return JDABuilder.createDefault(token)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .disableCache(Arrays.asList(CacheFlag.values()))
                .addEventListeners(new InteractionHandler(), new StartHandler(this))
                .build()
                .awaitReady();
    }

    public DataObject getData() {
        return data;
    }

    public JDA getJda() {
        return jda;
    }

    public boolean isDevMode() {
        return devMode;
    }
}
