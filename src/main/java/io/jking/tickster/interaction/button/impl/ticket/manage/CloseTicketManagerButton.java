package io.jking.tickster.interaction.button.impl.ticket.manage;

import io.jking.tickster.interaction.button.AbstractButton;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.Success;
import io.jking.tickster.interaction.core.impl.ButtonContext;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class CloseTicketManagerButton extends AbstractButton {

    public CloseTicketManagerButton() {
        super("button:manage:close_ticket");
    }

    @Override
    public void onButtonPress(ButtonContext context) {
        final GuildTicketsRecord record = context.getTicketRecord();
        if (record == null) {
            context.replyErrorEphemeral(Error.CUSTOM, "This is not a valid ticket or an error occurred.").queue();
            return;
        }

        if (!record.getStatus()) {
            context.replyErrorEphemeral(Error.CUSTOM, "This ticket is already closed...").queue();
            return;
        }

        final TextChannel channel = context.getTextChannel();
        final long channelId = channel.getIdLong();



        context.getTicketCache().update(channelId, GUILD_TICKETS.STATUS, false);
        context.replySuccessEphemeral(Success.UPDATE, "Ticket Status").queue();

        context.sendMessage(EmbedUtil.getDefault()
                .setColor(EmbedUtil.SECONDARY)
                .setDescription(context.getUser().getAsTag() + " closed this ticket!")
                .setTimestamp(Instant.now())
        ).queue();

    }
}
