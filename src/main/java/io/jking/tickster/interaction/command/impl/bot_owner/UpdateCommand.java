package io.jking.tickster.interaction.command.impl.bot_owner;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class UpdateCommand extends AbstractCommand {

    private final CommandRegistry registry;

    public UpdateCommand(CommandRegistry registry) {
        super("update", "Update a specific command. Globally or guild.", CommandCategory.BOT_OWNER);
        addOption(OptionType.STRING, "command-name", "Specific command to update.", true);
        addOption(OptionType.BOOLEAN, "global", "Globally or guild update.", true);
        this.registry = registry;
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String commandName = sender.getStringOption("command-name");

        if (commandName == null) {
            sender.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        final AbstractCommand command = registry.get(commandName);
        if (command == null) {
            sender.replyErrorEphemeral(Error.CUSTOM, "Could not find that command!").queue();
            return;
        }

        final boolean globalOption = sender.getBooleanOption("global");
        if (globalOption) {
            sender.getJDA().upsertCommand(command.getData()).queue(success -> {
                sender.replySuccessEphemeral(Success.UPDATE, commandName).queue();
            }, error -> {
                sender.replyErrorEphemeral(Error.UNKNOWN).queue();
            });
        } else {
            sender.getGuild().upsertCommand(command.getData()).queue(success -> {
                sender.replySuccessEphemeral(Success.UPDATE, commandName).queue();
            }, error -> {
                sender.replyErrorEphemeral(Error.UNKNOWN).queue();
            });
        }
    }
}
