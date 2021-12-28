package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.interaction.type.IButton;
import io.jking.tickster.object.CButton;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class YesCloseTicketButton implements IButton {

    @Override
    public void onInteraction(ButtonContext context) {

        context.deferEdit().queue(deferred -> {
            final EmbedBuilder embed = EmbedFactory.getDefault()
                    .setDescription("I am closing this ticket now! Click any of the button(s) below.")
                    .setFooter("Please note, this ticket will automatically delete 24 hours from now.");

            context.getChannel().upsertPermissionOverride(context.getMember())
                    .setDeny(Permission.MESSAGE_WRITE)
                    .flatMap(success -> deferred.editOriginal("").setEmbeds(embed.build()).setActionRow(
                            CButton.PRIMARY.format("ticket_reopen", "Re-Open Ticket", Emoji.fromUnicode("\uD83D\uDD13")),
                            CButton.DANGER.format("ticket_delete", "Delete Ticket", Emoji.fromUnicode("⚪")),
                            CButton.SECONDARY.format("ticket_transcript", "Transcript", Emoji.fromUnicode("\uD83D\uDCDD"))
                    ))
                    .queue(success -> context.getTicketCache().update(context.getChannel().getIdLong(), GUILD_TICKETS.OPEN, false));
        });
    }

    @Override
    public String componentId() {
        return "yes:ticket_close";
    }
}
