package io.jking.tickster.interaction.command.impl.ticket_manage;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.SlashContext;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.Button;

public class TManageCommand extends AbstractCommand {

    public TManageCommand() {
        super("tmanage", "Ticket managing command.", CommandCategory.TICKET_MANAGEMENT);
        final SubcommandData summonData = new SubcommandData("summon", "Summons the ticket creation message.");
        addSubcommands(summonData);
    }

    @Override
    public void onSlashCommand(SlashContext context) {
        final String subCommandName = context.getSubCommandName();
        if (subCommandName == null || subCommandName.isEmpty()) {
            context.replyErrorEphemeral(Error.UNKNOWN);
            return;
        }

        switch (subCommandName.toLowerCase()) {
            case "summon" -> onSummonCommand(context);
        }

    }

    private void onSummonCommand(SlashContext context) {
        context.reply(EmbedUtil.getTicketSummoner(context.getSelfUser()))
                .addActionRow(Button.secondary(
                        "button:ticket_create",
                        "Create Ticket")
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDCE9"))
                )
                .queue();
    }


}
