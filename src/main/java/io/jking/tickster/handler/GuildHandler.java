package io.jking.tickster.handler;

import io.jking.tickster.cache.Cache;
import io.jking.tickster.database.Database;
import io.jking.tickster.object.InviteData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import static io.jking.tickster.jooq.tables.GuildData.GUILD_DATA;

public class GuildHandler implements EventListener {

    private final Database database;
    private final Cache cache;

    public GuildHandler(Database database, Cache cache) {
        this.database = database;
        this.cache = cache;
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
        else if (event instanceof GuildMemberJoinEvent)
            onGuildMemberJoin((GuildMemberJoinEvent) event);
    }

    private void onGuildOwnerChange(GuildUpdateOwnerEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long newOwnerId = event.getNewOwnerIdLong();

        database.getDSL().update(GUILD_DATA)
                .set(GUILD_DATA.OWNER_ID, newOwnerId)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync();
    }

    private void onGuildJoin(GuildJoinEvent event) {
        checkPopulation(event.getGuild());
        cacheInvites(event.getGuild());
        insertGuild(event.getGuild());
    }


    private void onGuildReady(GuildReadyEvent event) {
        insertGuild(event.getGuild());
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        final long guildId = event.getGuild().getIdLong();
        cache.getGuildCache().delete(guildId);

        database.getDSL().deleteFrom(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync();
    }

    private void onGuildMemberJoin(GuildMemberJoinEvent event) {

    }

    private void insertGuild(Guild guild) {
        if (guild == null)
            return;

        final long guildId = guild.getIdLong();
        final long ownerId = guild.getOwnerIdLong();

        database.getDSL().insertInto(GUILD_DATA)
                .values(guildId, ownerId)
                .executeAsync();
    }

    private void cacheInvites(Guild guild) {
        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                cache.getInviteCache().put(new InviteData(invite));
            }
        });
    }

    private void checkPopulation(Guild guild) {
        guild.loadMembers().onSuccess(members -> {
            final double size = members.size();
            final double botList = members.parallelStream()
                    .map(Member::getUser)
                    .filter(User::isBot)
                    .collect(Collectors.toUnmodifiableList())
                    .size();

            final double calculation = (botList / size) * 100;
            if (calculation >= 80) {
                guild.leave().queue();
            }
        }).onError(Throwable::printStackTrace);
    }
}
