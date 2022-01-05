package io.jking.tickster.event;

import io.jking.tickster.cache.CacheManager;
import io.jking.tickster.core.Tickster;
import io.jking.tickster.core.TicksterInfo;
import io.jking.tickster.database.Database;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.ScheduleUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class JDAEvent implements EventListener {


    private final ShardManager shardManager;
    private final CommandRegistry registry;
    private final Database database;
    private final CacheManager cache;

    public JDAEvent(Tickster tickster) {
        this.shardManager = tickster.getShardManager();
        this.registry = tickster.getCommandRegistry();
        this.database = tickster.getDatabase();
        this.cache = tickster.getCacheManager();
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
        Tickster.getLogger().info("Tickster Ready - Version: {}", TicksterInfo.TICKSTER_VERSION);

        deleteExpiredTickets(shardManager);

        event.getJDA().updateCommands()
                .addCommands(registry.getCommands())
                .queue(success -> Tickster.getLogger().info("Registered Commands Globally."));
    }

    private void deleteExpiredTickets(ShardManager shardManager) {
        ScheduleUtil.scheduleDelayedTask(() -> {
            Tickster.getLogger().info("Attempting to delete expired tickets.");
            final Result<GuildTicketsRecord> expiredTickets = database.getContext().deleteFrom(GUILD_TICKETS)
                    .where("ticket_timestamp + interval '48 hours' < now()", GUILD_TICKETS.TICKET_TIMESTAMP)
                    .returning()
                    .fetch();

            if (expiredTickets.isEmpty()) {
                Tickster.getLogger().warn("No tickets to delete!");
                return;
            }

            Tickster.getLogger().info("Deleting {} tickets from GUILD_TICKETS table.", expiredTickets.size());
            expiredTickets.forEach(record -> {
                final TextChannel channel = shardManager.getTextChannelById(record.getChannelId());
                if (channel == null)
                    return;

                final Member self = channel.getGuild().getSelfMember();
                if (!self.hasPermission(Permission.MANAGE_CHANNEL))
                    return;

                channel.delete().queue(success -> {
                    Tickster.getLogger().info("Successfully deleted expired ticket channel: {}", channel.getIdLong());
                }, new ErrorHandler().ignore(Arrays.asList(ErrorResponse.values())));
            });
        }, 30, 15, TimeUnit.MINUTES);

    }

}
