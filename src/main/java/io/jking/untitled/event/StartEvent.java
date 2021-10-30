package io.jking.untitled.event;

import io.jking.untitled.cache.Cache;
import io.jking.untitled.cache.impl.GuildCache;
import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.core.Untitled;
import io.jking.untitled.database.Hikari;
import io.jking.untitled.jooq.tables.records.GuildDataRecord;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;


public class StartEvent implements EventListener {

    private final Logger LOGGER = LoggerFactory.getLogger(StartEvent.class);

    private final CommandRegistry registry;

    private final Cache cache;

    private final Untitled untitled;

    public StartEvent(CommandRegistry registry, Cache cache, Untitled untitled) {
        this.registry = registry;
        this.cache = cache;
        this.untitled = untitled;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent)
            onOwnerChange((GuildUpdateOwnerEvent) event);
        else if (event instanceof GuildJoinEvent)
            onNewGuild((GuildJoinEvent) event);
    }

    private void onReady(ReadyEvent event) {
        if (untitled.isDevMode()) {
            final long devId = untitled.getConfig().getObject("bot").getLong("dev_id");
            final Guild guild = event.getJDA().getGuildById(devId);
            if (guild == null) {
                LOGGER.warn("DEV GUILD IS NULL!");
                return;
            }

            registry.getCommands().forEach(command -> guild.upsertCommand(command).queue());
        }
    }

    private void onNewGuild(GuildJoinEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long ownerId = event.getGuild().getOwnerIdLong();

        Hikari.getInstance().getDSL()
                .insertInto(GUILD_DATA)
                .values(guildId, ownerId)
                .executeAsync();

        cache.getGuildCache().put(guildId, new GuildDataRecord()
                .value1(guildId)
                .value2(ownerId)
        );
    }

    private void onGuildReady(@NotNull GuildReadyEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final GuildCache guildCache = cache.getGuildCache();
        guildCache.push(guildId);
    }

    private void onOwnerChange(@NotNull GuildUpdateOwnerEvent event) {
        Hikari.getInstance().getDSL()
                .update(GUILD_DATA)
                .set(GUILD_DATA.OWNER_ID, event.getNewOwnerIdLong())
                .executeAsync();
    }


}
