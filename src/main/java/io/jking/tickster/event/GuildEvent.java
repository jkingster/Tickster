package io.jking.tickster.event;

import io.jking.jooq.tables.pojos.GuildData;
import io.jking.jooq.tables.pojos.TicketSettings;
import io.jking.tickster.database.repository.GuildRepository;
import io.jking.tickster.database.repository.TicketSettingsRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import static io.jking.jooq.tables.GuildData.GUILD_DATA;

public class GuildEvent implements EventListener {

    private final GuildRepository guildRepository = GuildRepository.getInstance();
    private final TicketSettingsRepository ticketSettingsRepo = TicketSettingsRepository.getInstance();

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent)
            onGuildUpdateOwner((GuildUpdateOwnerEvent) event);
        else if (event instanceof GuildLeaveEvent)
            onGuildLeave((GuildLeaveEvent) event);
    }

    private void onGuildReady(GuildReadyEvent event) {
        saveGuild(event.getGuild());
    }

    private void onGuildJoin(GuildJoinEvent event) {
        saveGuild(event.getGuild());
    }

    private void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long newOwnerId = event.getNewOwnerIdLong();
        guildRepository.update(guildId, GUILD_DATA.OWNER_ID, newOwnerId);
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        guildRepository.delete(event.getGuild().getIdLong());
    }

    private void saveGuild(Guild guild) {
        final long guildId = guild.getIdLong();
        final long ownerId = guild.getOwnerIdLong();
        guildRepository.save(new GuildData(guildId, ownerId, 0L, 0L));
        ticketSettingsRepo.save(new TicketSettings(guildId));
    }

}
