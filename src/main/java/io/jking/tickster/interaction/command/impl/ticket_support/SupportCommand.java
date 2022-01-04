package io.jking.tickster.interaction.command.impl.ticket_support;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.concurrent.TimeUnit;


public class SupportCommand extends AbstractCommand {

    public SupportCommand() {
        super(
                "support",
                "Ticket support commands.",
                Permission.MESSAGE_SEND,
                CommandCategory.TICKET_SUPPORT,
                CommandFlag.of(CommandFlag.EPHEMERAL | CommandFlag.TICKET)
        );
        requiresSupportRole();

        final SubcommandData summonData = new SubcommandData("summon", "Summons the ticket creation message.");
        final SubcommandData deleteData = new SubcommandData("delete", "Deletes the ticket you're currently viewing.");
        final SubcommandData markData = new SubcommandData("mark", "Marks that you are viewing the current ticket.");
        addSubcommands(summonData, deleteData, markData);
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String subCommandName = sender.getSubCommandName();
        if (subCommandName == null || subCommandName.isEmpty()) {
            sender.reply(Error.UNKNOWN).queue();
            return;
        }

        switch (subCommandName.toLowerCase()) {
            case "summon" -> onSummonCommand(sender);
            case "delete" -> onDeleteCommand(sender);
            case "mark" -> onMarkCommand(sender);
        }

    }

    private void onSummonCommand(SlashSender sender) {
        sender.reply(EmbedUtil.getTicketSummoner(sender.getSelfUser()))
                .addActionRow(Button.secondary(
                        "button:create_ticket",
                        "Create Ticket")
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDCE9"))
                )
                .queue();
    }

    private void onDeleteCommand(SlashSender sender) {
        final GuildTicketsRecord record = sender.getTicketRecord();
        if (record == null) {
            sender.reply(Error.CUSTOM, "This is not a valid ticket to delete!").queue();
            return;
        }

        final long channelId = sender.getTextChannel().getIdLong();
        sender.reply("This ticket is now being deleted.").delay(5, TimeUnit.SECONDS)
                .flatMap(hook -> sender.getTextChannel().delete())
                .queue(success -> sender.getTicketCache().delete(channelId));
    }

    private void onMarkCommand(SlashSender sender) {
        final GuildTicketsRecord record = sender.getTicketRecord();
        if (record == null) {
            sender.reply(Error.CUSTOM, "This is not a valid ticket to mark!").queue();
            return;
        }

        final User user = sender.getUser();
        sender.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(user.getAsTag() + " is viewing this ticket.")
                .setColor(EmbedUtil.SECONDARY)
                .setDescription("Please be patient as they read the provided information.")
                .build())
                .queue();

        sender.reply(EmbedUtil.getDefault().setDescription("Manage this ticket with any of the buttons below."))
                .addActionRow(Button.secondary("button:manage:close_ticket", "Close Ticket"), Button.danger("button:manage:delete_ticket", "Delete Ticket"))
                .queue();

    }

}
