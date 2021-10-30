package io.jking.untitled.event;

import io.jking.untitled.command.CommandRegistry;
import io.jking.untitled.database.Hikari;
import io.jking.untitled.jooq.tables.GuildData;
import io.jking.untitled.jooq.tables.records.GuildDataRecord;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jooq.impl.DSL;


import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;


public class StartEvent implements EventListener {

    private final CommandRegistry registry;

    public StartEvent(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent)
            onOwnerChange((GuildUpdateOwnerEvent) event);
    }

    private void onGuildReady(@NotNull GuildReadyEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long ownerId = event.getGuild().getOwnerIdLong();

        Hikari.getInstance().getDSL()
                .insertInto(GUILD_DATA)
                .values(guildId, ownerId)
                .executeAsync();
    }

    private void onOwnerChange(@NotNull GuildUpdateOwnerEvent event) {
        Hikari.getInstance().getDSL()
                .update(GUILD_DATA)
                .set(GUILD_DATA.OWNER_ID, event.getNewOwnerIdLong())
                .executeAsync();
    }


}
