package io.jking.tickster.handlers;

import io.jking.tickster.database.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;

public class GuildHandler implements EventListener {

    private final Logger logger = LoggerFactory.getLogger(GuildHandler.class);
    private final Database database;

    public GuildHandler(Database database) {
        this.database = database;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent)
            onGuildOwnerChange((GuildUpdateOwnerEvent) event);
        else if (event instanceof GuildLeaveEvent)
            onGuildLeave((GuildLeaveEvent) event);
    }

    private void onGuildOwnerChange(GuildUpdateOwnerEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long oldOwnerId = event.getOldOwnerIdLong();
        final long newOwnerId = event.getNewOwnerIdLong();

        database.getDSL().update(GUILD_DATA)
                .set(GUILD_DATA.OWNER_ID, newOwnerId)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync()
                .whenCompleteAsync((unused, throwable) -> {
                    if (throwable != null) {
                        logger.warn(throwable.getMessage());
                        return;
                    }

                    logger.info("{}: Owner Changed: {} -> {}", guildId, oldOwnerId, newOwnerId);
                });
    }

    private void onGuildJoin(GuildJoinEvent event) {
        event.getGuild().loadMembers().onSuccess(members -> {
            final double size = members.size();
            final double botList = members.parallelStream()
                    .map(Member::getUser)
                    .filter(User::isBot)
                    .collect(Collectors.toUnmodifiableList())
                    .size();

            final double calculation = (botList / size) * 100;
            if (calculation >= 80) {
                event.getGuild().leave().queue(null, throwable -> logger.warn(throwable.getMessage()));
            }
        }).onError(Throwable::printStackTrace);

        insertGuild(event.getGuild());
    }

    private void onGuildReady(GuildReadyEvent event) {
        insertGuild(event.getGuild());
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        final long guildId = event.getGuild().getIdLong();
        database.getDSL().deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync()
                .whenCompleteAsync((unused, throwable) -> {
                    if (throwable != null) {
                        logger.warn(throwable.getMessage());
                        return;
                    }

                    logger.info("Deleted {} from GUILD_DATA table.", guildId);
                });
    }

    private void insertGuild(Guild guild) {
        if (guild == null)
            return;

        final long guildId = guild.getIdLong();
        final long ownerId = guild.getOwnerIdLong();

        database.getDSL().insertInto(GUILD_DATA)
                .set(GUILD_DATA.GUILD_ID, guildId)
                .set(GUILD_DATA.OWNER_ID, ownerId)
                .onConflictDoNothing()
                .executeAsync()
                .whenCompleteAsync((unused, throwable) -> {
                    if (throwable != null) {
                        logger.warn(throwable.getMessage());
                        return;
                    }

                    logger.info("{} was inserted into the GUILD_DATA table.", guildId);
                });
    }
}
