package io.jking.tickster.core;

import io.jking.tickster.database.Database;
import io.jking.tickster.database.Hikari;
import io.jking.tickster.handlers.GuildHandler;
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
    private final Database database;

    private boolean isDev;

    private Tickster(String configPath, boolean isDev) throws IOException, LoginException, InterruptedException {
        this.data = loadConfig(configPath);
        this.jda = startTickster(isDev);
        this.isDev = isDev;
        this.database = new Database(new Hikari(data)).createTables(
                "sql/guild_data.sql"
        );
    }

    public static void buildProduction(String configPath) throws LoginException, InterruptedException, IOException {
        new Tickster(configPath, false);
    }

    public static void buildDev(String configPath) throws IOException, LoginException, InterruptedException {
        new Tickster(configPath, true);
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

        final Hikari hikari = new Hikari(data);
        final Database database = new Database(hikari);

        return JDABuilder.createDefault(token)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .disableCache(Arrays.asList(CacheFlag.values()))
                .addEventListeners(new InteractionHandler(database), new StartHandler(this), new GuildHandler(database))
                .build()
                .awaitReady();
    }

    public DataObject getData() {
        return data;
    }

    public JDA getJda() {
        return jda;
    }

    public Database getDatabase() {
        return database;
    }

    public boolean isDev() {
        return isDev;
    }
}
