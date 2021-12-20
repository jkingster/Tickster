package io.jking.tickster.commands.bot_owner;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.impl.SlashContext;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class UpdateCommand extends AbstractCommand {
    public UpdateCommand() {
        super("update", "Update a specific command. Globally or guild.", CommandCategory.BOT_OWNER);
        addOption(OptionType.STRING, "command-name", "Specific command to update.", true);
        addOption(OptionType.BOOLEAN, "global", "Globally or guild update.", true);
    }

    @Override
    public void onSlashCommand(SlashContext context) {
        final String commandName = context.getStringOption("command-name");

        if (commandName == null) {
            context.replyErrorEphemeral(Error.CUSTOM, "Could not find that command!").queue();
            return;
        }

        final boolean globalOption = context.getBooleanOption("global");
        if (globalOption) {

        } else {

        }
    }
}
