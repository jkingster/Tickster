package io.jking.tickster.event;


import io.jking.tickster.database.impl.GuildRepo;
import io.jking.tickster.logging.LogType;
import io.jking.tickster.logging.TicksterLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class GuildEvent implements EventListener {

    private final GuildRepo guildRepo = GuildRepo.getInstance();

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
        else if (event instanceof GuildLeaveEvent)
            onGuildLeave((GuildLeaveEvent) event);
        else if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
    }

    private void onGuildJoin(GuildJoinEvent event) {
        registerGuild(event.getGuild());
    }

    private void onGuildReady(GuildReadyEvent event) {
        registerGuild(event.getGuild());
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        long guildId = event.getGuild().getIdLong();
        TicksterLogger.warn(LogType.GUILD_LEAVE, guildId);
        guildRepo.delete(guildId);
    }

    // If the guild does not exist in the GUILD_DATA table, we will register it.
    private void registerGuild(Guild guild) {
        long guildId = guild.getIdLong();
        if (guildRepo.isExisting(guildId))
            return;

        guildRepo.register(guild);
    }

}
