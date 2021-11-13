package io.jking.tickster.core;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.command.CommandRegistry;
import io.jking.tickster.command.impl.info.AboutCommand;
import io.jking.tickster.command.impl.setup.SetupCommand;
import io.jking.tickster.command.impl.utility.PingCommand;
import io.jking.tickster.command.impl.utility.TestCommand;
import io.jking.tickster.database.Database;
import io.jking.tickster.database.Hikari;
import io.jking.tickster.handler.GuildHandler;
import io.jking.tickster.handler.InteractionHandler;
import io.jking.tickster.handler.StartHandler;
import io.jking.tickster.object.ScheduledTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class Tickster {

    private final Logger logger = LoggerFactory.getLogger(Tickster.class);

    private final CommandRegistry commandRegistry = new CommandRegistry()
            .addCommands(new TestCommand(), new PingCommand())
            .addCommands(new AboutCommand(), new SetupCommand());


    private final DataObject data;
    private final Database database;
    private final Cache cache;

    private JDA jda;

    private Tickster(String configPath) throws IOException, LoginException, InterruptedException {
        this.data = loadConfig(configPath);
        this.database = new Database(new Hikari(data)).createTables(
                "sql/guild_data.sql", "sql/guild_tickets.sql", "sql/guild_reports.sql"
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

    private void startTickster() throws LoginException, InterruptedException {
        logger.info("Building Tickster...");
        final String token = data.getObject("bot").getString("token", null);
        Checks.notNull(token, "Config Token");

        this.jda = JDABuilder.createDefault(token)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setChunkingFilter(ChunkingFilter.NONE)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .disableCache(Arrays.asList(CacheFlag.values()))
                .addEventListeners(
                        new InteractionHandler(commandRegistry, database, cache),
                        new StartHandler(this, cache),
                        new GuildHandler(commandRegistry, database, cache)
                )
                .build()
                .awaitReady();

        startScheduledTasks();
    }

    // This deletes entries/channels from the database table/guild if they are 1 week old.
    // TODO: Once we begin sharding, this will be non-functional and will have to be adjusted.
    private void startScheduledTasks() {
        if (jda == null)
            return;

        ScheduledTask.scheduleTask(() -> {
            final LocalDateTime future = LocalDateTime.now().plusDays(7);
            logger.info("Running Scheduled Task");
            database.getDSL().deleteFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.TICKET_TIMESTAMP.ge(future))
                    .returning()
                    .fetchAsync()
                    .whenCompleteAsync((result, throwable) -> {
                        if (throwable != null)
                            return;

                        final List<Long> channelIds = result.getValues(GUILD_TICKETS.CHANNEL_ID);
                        if (channelIds.isEmpty())
                            return;

                        channelIds.forEach(channelId -> {
                            final TextChannel channel = jda.getTextChannelById(channelId);
                            if (channel != null) {
                                channel.delete().queue(null, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
                            }
                        });
                    });
        }, 1, TimeUnit.MINUTES);

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


}
