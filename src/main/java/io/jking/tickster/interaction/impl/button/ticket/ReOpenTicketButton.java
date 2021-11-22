package io.jking.tickster.interaction.impl.button.ticket;


import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import net.dv8tion.jda.api.Permission;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class ReOpenTicketButton implements IButton {
    @Override
    public void onInteraction(ButtonContext context) {
        context.deferEdit().queue(success -> context.getChannel().upsertPermissionOverride(context.getMember())
                .setAllow(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL)
                .flatMap(ignored -> context.getHook().deleteOriginal())
                .queue(opened -> {
                    final long guildId = context.getGuild().getIdLong();
                    final long channelId = context.getChannel().getIdLong();
                    // TODO: Update using TicketCache#update
                    context.getDatabase().getDSL().update(GUILD_TICKETS)
                            .set(GUILD_TICKETS.OPEN, true)
                            .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                            .and(GUILD_TICKETS.CHANNEL_ID.eq(channelId))
                            .executeAsync(context.getDatabase().getExecutor());
                }));
    }
    @Override
    public String componentId() {
        return "ticket_reopen";
    }
}
