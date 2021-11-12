package io.jking.tickster.interaction.impl.button.ticket;

import io.jking.tickster.interaction.InteractionImpl;
import io.jking.tickster.interaction.context.ButtonContext;
import io.jking.tickster.utility.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class YesCloseTicketButton implements InteractionImpl<ButtonContext> {

    @Override
    public void onInteraction(ButtonContext context) {

        context.deferEdit().queue(hook -> {
            final EmbedBuilder embed = EmbedFactory.getDefault()
                    .setDescription("I am closing this ticket now! Click any of the button(s) below.")
                    .setFooter("Please note, this ticket will automatically delete 24 hours from now.");

            context.getChannel().upsertPermissionOverride(context.getMember())
                    .setDeny(Permission.MESSAGE_WRITE)
                    .queue(success -> {
                                hook.editOriginal("").setEmbeds(embed.build()).setActionRow(
                                        Button.of(ButtonStyle.PRIMARY, "ticket_reopen", "Re-Open Ticket", Emoji.fromUnicode("\uD83D\uDD13")),
                                        Button.of(ButtonStyle.DANGER, "ticket_delete", "Delete Ticket", Emoji.fromUnicode("⚪")),
                                        Button.of(ButtonStyle.SECONDARY, "ticket_transcript", "Transcript", Emoji.fromUnicode("\uD83D\uDCDD"))
                                ).queue();

                                final long guildId = context.getGuild().getIdLong();
                                final long channelId = context.getChannel().getIdLong();

                                context.getDatabase().getDSL().update(GUILD_TICKETS)
                                        .set(GUILD_TICKETS.OPEN, false)
                                        .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                                        .and(GUILD_TICKETS.CHANNEL_ID.eq(channelId))
                                        .executeAsync(context.getDatabase().getExecutor());
                            }

                    );
        });
    }

    @Override
    public String componentId() {
        return "yes:ticket_close";
    }
}
