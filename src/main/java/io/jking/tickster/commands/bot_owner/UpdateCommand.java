package io.jking.tickster.commands.bot_owner;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.Error;
import io.jking.tickster.interaction.core.Success;
import io.jking.tickster.interaction.core.impl.SlashContext;
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
    public void onSlashCommand(SlashContext context) {
        final String commandName = context.getStringOption("command-name");

        if (commandName == null) {
            context.replyErrorEphemeral(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        final AbstractCommand command = registry.get(commandName);
        if (command == null) {
            context.replyErrorEphemeral(Error.CUSTOM, "Could not find that command!").queue();
            return;
        }

        final boolean globalOption = context.getBooleanOption("global");
        if (globalOption) {
            context.getJDA().upsertCommand(command).queue(success -> {
                context.replySuccessEphemeral(Success.UPDATE, commandName).queue();
            }, error -> {
                context.replyErrorEphemeral(Error.UNKNOWN).queue();
            });
        } else {
            context.getGuild().upsertCommand(command).queue(success -> {
                context.replySuccessEphemeral(Success.UPDATE, commandName).queue();
            }, error -> {
                context.replyErrorEphemeral(Error.UNKNOWN).queue();
            });
        }
    }
}
