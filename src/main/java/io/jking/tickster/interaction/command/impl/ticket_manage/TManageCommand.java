package io.jking.tickster.interaction.command.impl.ticket_manage;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.jooq.tables.records.GuildTicketsRecord;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.concurrent.TimeUnit;


public class TManageCommand extends AbstractCommand {

    public TManageCommand() {
        super("tmanage", "Ticket managing command.", CommandCategory.TICKET_MANAGEMENT);
        final SubcommandData summonData = new SubcommandData("summon", "Summons the ticket creation message.");
        final SubcommandData deleteData = new SubcommandData("delete", "Deletes the ticket you're currently viewing.");
        final SubcommandData markData = new SubcommandData("mark", "Marks that you are viewing the current ticket.");
        addSubCommands(summonData, deleteData, markData);
    }

    @Override
    public void onSlashCommand(SlashSender context) {
        final String subCommandName = context.getSubCommandName();
        if (subCommandName == null || subCommandName.isEmpty()) {
            context.replyErrorEphemeral(Error.UNKNOWN);
            return;
        }

        switch (subCommandName.toLowerCase()) {
            case "summon" -> onSummonCommand(context);
            case "delete" -> onDeleteCommand(context);
            case "mark" -> onMarkCommand(context);
        }

    }

    private void onSummonCommand(SlashSender context) {
        context.reply(EmbedUtil.getTicketSummoner(context.getSelfUser()))
                .addActionRow(Button.secondary(
                        "button:create_ticket",
                        "Create Ticket")
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDCE9"))
                )
                .queue();
    }

    private void onDeleteCommand(SlashSender context) {
        final GuildTicketsRecord record = context.getTicketRecord();
        if (record == null) {
            context.replyErrorEphemeral(Error.CUSTOM, "This is not a valid ticket to delete!").queue();
            return;
        }

        final long channelId = context.getTextChannel().getIdLong();
        context.reply("This ticket is now being deleted.").delay(5, TimeUnit.SECONDS)
                .flatMap(hook -> context.getTextChannel().delete())
                .queue(success -> context.getTicketCache().delete(channelId));
    }

    private void onMarkCommand(SlashSender context) {
        final GuildTicketsRecord record = context.getTicketRecord();
        if (record == null) {
            context.replyErrorEphemeral(Error.CUSTOM, "This is not a valid ticket to mark!").queue();
            return;
        }

        final User user = context.getUser();
        context.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(user.getAsTag() + " is viewing this ticket.")
                .setColor(EmbedUtil.SECONDARY)
                .setDescription("Please be patient as they read the provided information.")
                .build())
                .queue();

        context.replyEphemeral(EmbedUtil.getDefault().setDescription("Manage this ticket with any of the buttons below."))
                .addActionRow(Button.secondary("button:manage:close_ticket", "Close Ticket"), Button.danger("button:manage:delete_ticket", "Delete Ticket"))
                .queue();

    }

}
