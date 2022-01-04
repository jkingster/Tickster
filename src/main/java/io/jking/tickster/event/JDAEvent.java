package io.jking.tickster.event;

import io.jking.tickster.core.Tickster;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.ScheduleUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class JDAEvent implements EventListener {


    private final ShardManager shardManager;
    private final CommandRegistry registry;
    private final Database database;

    public JDAEvent(Tickster tickster) {
        this.shardManager = tickster.getShardManager();
        this.registry = tickster.getCommandRegistry();
        this.database = tickster.getDatabase();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);
        else if (event instanceof DisconnectEvent)
            onDisconnect((DisconnectEvent) event);
        else if (event instanceof ShutdownEvent)
            onShutdown((ShutdownEvent) event);
        else if (event instanceof ReconnectedEvent)
            onReconnect((ReconnectedEvent) event);
    }

    private void onReconnect(ReconnectedEvent event) {
        Tickster.getLogger().info("Tiskter Reconnected");
    }

    private void onShutdown(ShutdownEvent event) {
        Tickster.getLogger().info("Tickster Shutdown");
    }

    private void onDisconnect(DisconnectEvent event) {
        Tickster.getLogger().info("Tickster Disconnected");
    }

    private void onReady(ReadyEvent event) {
        Tickster.getLogger().info("Tickster Ready");

        deleteExpiredTickets(shardManager);

        event.getJDA().updateCommands()
                .addCommands(registry.getCommands())
                .queue(success -> Tickster.getLogger().info("Registered Commands Globally."));
    }

    private void deleteExpiredTickets(ShardManager shardManager) {
        ScheduleUtil.scheduleDelayedTask(() -> {
            Tickster.getLogger().info("Deleting Expired Tickets");
            final LocalDateTime now = LocalDateTime.now().plusHours(48);
            final Result<GuildTicketsRecord> result = database.getContext().deleteFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.TICKET_TIMESTAMP.ge(now))
                    .returning()
                    .fetch();

            if (result.isEmpty()) {
                Tickster.getLogger().info("No tickets were deleted!");
                return;
            }

            Tickster.getLogger().info("Deleting {} Tickets.", result.size());
            for (GuildTicketsRecord record : result) {
                final long guildId = record.getGuildId();
                final Guild guild = shardManager.getGuildById(guildId);
                if (guild == null)
                    continue;

                final long ticketId = record.getChannelId();
                final TextChannel channel = guild.getTextChannelById(ticketId);
                if (channel == null)
                    continue;

                channel.delete().queue(null, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
            }
        }, 30, 15, TimeUnit.MINUTES);
    }

}
