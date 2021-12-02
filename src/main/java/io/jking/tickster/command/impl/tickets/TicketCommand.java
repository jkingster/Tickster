package io.jking.tickster.command.impl.tickets;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.CommandError;
import io.jking.tickster.command.type.ErrorType;
import io.jking.tickster.command.type.SuccessType;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.object.CButton;
import io.jking.tickster.utility.EmbedFactory;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.jooq.Result;

import static io.jking.tickster.jooq.tables.GuildTickets.GUILD_TICKETS;

public class TicketCommand extends Command {

    public TicketCommand() {
        super("ticket", "Ticket related commands.", Category.TICKETS);

        addSubcommands(
                new SubcommandData("view", "View your open tickets."),
                new SubcommandData("purge", "Purge all your tickets.")
                        .addOption(OptionType.BOOLEAN, "purge_global", "global", true)
        );
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        final String subCommand = ctx.getSubCommand();

        switch (subCommand.toLowerCase()) {
            case "view" -> onTicketView(ctx, err);
            case "purge" -> onTicketPurge(ctx, err);
        }
    }

    private void onTicketPurge(CommandContext ctx, CommandError error) {
        final boolean option = ctx.getOptionBoolean("purge_global");
        final long creatorId = ctx.getAuthor().getIdLong();

        if (option) {
            ctx.getDatabase().getDSL().deleteFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.CREATOR_ID.eq(creatorId))
                    .executeAsync()
                    .thenAcceptAsync(action -> ctx.replySuccess(SuccessType.DELETED, "All your tickets were deleted globally.")
                            .setEphemeral(true)
                            .queue())
                    .exceptionallyAsync(throwable -> {
                        if (throwable != null) {
                            error.reply(ErrorType.CUSTOM, "There was an error deleting your tickets. Please consider joining our support server or contacting the bot developers.");
                        }
                        return null;
                    });
        } else {
            final long guildId = ctx.getGuild().getIdLong();
            ctx.getDatabase().getDSL().deleteFrom(GUILD_TICKETS)
                    .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                    .and(GUILD_TICKETS.CREATOR_ID.eq(creatorId))
                    .executeAsync()
                    .thenAcceptAsync(action -> ctx.replySuccess(SuccessType.DELETED, "All your tickets for this guild were deleted.")
                            .setEphemeral(true)
                            .queue())
                    .exceptionallyAsync(throwable -> {
                        if (throwable != null) {
                            error.reply(ErrorType.CUSTOM, "There was an error deleting your tickets. Please consider joining our support server or contacting the bot developers.");
                        }
                        return null;
                    });
        }
    }

    private void onTicketView(CommandContext ctx, CommandError err) {
        final long guildId = ctx.getGuild().getIdLong();
        final long authorId = ctx.getAuthor().getIdLong();

        ctx.getDatabase().getDSL().selectFrom(GUILD_TICKETS)
                .where(GUILD_TICKETS.GUILD_ID.eq(guildId))
                .and(GUILD_TICKETS.CREATOR_ID.eq(authorId))
                .fetchAsync()
                .thenAcceptAsync(results -> {

                    if (results.isEmpty()) {
                        err.reply(ErrorType.CUSTOM, "You have no tickets to view.");
                        return;
                    }

                    final SelectionMenu.Builder menu = getTicketsMenu(results);
                    ctx.reply(EmbedFactory.getSelectionEmbed(ctx.getMember(), "Click any ticket to view it!"))
                            .addActionRows(
                                    ActionRow.of(menu.build()),
                                    ActionRow.of(CButton.PRIMARY.format("purge_tickets", "Purge Tickets", Emoji.fromUnicode("✂")))
                            )
                            .setEphemeral(true)
                            .queue();

                })
                .exceptionallyAsync(throwable -> {
                    err.reply(ErrorType.CUSTOM, "Could not retrieve open tickets.");
                    return null;
                });
    }

    private SelectionMenu.Builder getTicketsMenu(Result<GuildTicketsRecord> results) {
        final SelectionMenu.Builder menu = SelectionMenu.create("view_tickets");

        for (int i = 0; i < results.size(); i++) {
            menu.addOption(
                    String.format("Ticket #%s", i + 1),
                    String.format("%s", results.get(i).getChannelId()),
                    String.format("Status: [%s]", MiscUtil.getStatus(results.get(i).getOpen()))
            );
        }

        return menu;
    }

}
