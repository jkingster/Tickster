package io.jking.tickster.core;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.command.CommandRegistry;
import io.jking.tickster.command.impl.info.AboutCommand;
import io.jking.tickster.command.impl.report.ReportCommand;
import io.jking.tickster.command.impl.setup.SetupCommand;
import io.jking.tickster.command.impl.tickets.TicketCommand;
import io.jking.tickster.command.impl.utility.PingCommand;
import io.jking.tickster.command.impl.utility.TestCommand;
import io.jking.tickster.database.Database;
import io.jking.tickster.database.Hikari;
import io.jking.tickster.handler.GuildHandler;
import io.jking.tickster.handler.InteractionHandler;
import io.jking.tickster.handler.InviteHandler;
import io.jking.tickster.handler.StartHandler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
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

    private final CommandRegistry commandRegistry = new CommandRegistry()
            .addCommands(new TestCommand(), new PingCommand())
            .addCommands(new AboutCommand(), new SetupCommand())
            .addCommands(new ReportCommand(), new TicketCommand());


    private final DataObject data;
    private final Database database;
    private final Cache cache;

    private ShardManager shardManager;

    private Tickster(String configPath) throws IOException {
        this.data = loadConfig(configPath);
        this.database = new Database(new Hikari(data)).createTables(
                "sql/guild_data.sql",
                "sql/guild_tickets.sql",
                "sql/guild_reports.sql"
        );
        this.cache = new Cache(database);
    }

    public static void build(String configPath) throws LoginException, InterruptedException, IOException {
        new Tickster(configPath).startTickster();
    }

    private DataObject loadConfig(String configPath) throws IOException {
        Checks.notNull(configPath, "Configuration Path.");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configPath))) {
            return DataObject.fromJson(bufferedReader);
        }
    }

    private void startTickster() throws LoginException {
        logger.info("Building Tickster...");
        final String token = data.getString("token", null);
        Checks.notNull(token, "Config Token");

        this.shardManager = DefaultShardManagerBuilder.createDefault(token)
                .setShardsTotal(-1)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_INVITES)
                .disableCache(Arrays.asList(CacheFlag.values()))
                .addEventListeners(
                        new InteractionHandler(this, commandRegistry, database, cache),
                        new StartHandler(this, cache),
                        new GuildHandler(database, cache),
                        new InviteHandler(cache)
                )
                .build();

    }

    // TODO: Re-do scheduling to delete expired tickets/reports.

    public DataObject getData() {
        return data;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Database getDatabase() {
        return database;
    }


}
