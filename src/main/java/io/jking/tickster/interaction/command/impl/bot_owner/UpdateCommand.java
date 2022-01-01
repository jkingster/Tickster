package io.jking.tickster.interaction.command.impl.bot_owner;

import io.jking.tickster.core.Tickster;
import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.List;

public class UpdateCommand extends AbstractCommand {

    private final CommandRegistry registry;

    public UpdateCommand(CommandRegistry registry) {
        super("update", "Update a specific command. Globally or guild.", CommandCategory.BOT_OWNER);

        addSubCommands(
                new SubcommandData("delete", "Delete all global commands."),
                new SubcommandData("update", "Update commands").addOptions(
                        new OptionData(OptionType.STRING, "command-name", "Specific command to update.", true),
                        new OptionData(OptionType.BOOLEAN, "global", "Globally or guild update.", true)
                )
        );

        this.registry = registry;
        setSupportOnly(true);
        getData().setDefaultEnabled(false);
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String subCommand = sender.getSubCommandName();
        switch (subCommand.toLowerCase()) {
            case "delete" -> onDeleteAllCommand(sender);
            case "update" -> onUpdateCommand(sender);
        }
    }

    private void onDeleteAllCommand(SlashSender sender) {
        sender.getJDA().updateCommands().queue(
                success -> Tickster.getLogger().info("Deleted all global commands.")
        );
    }

    private void onUpdateCommand(SlashSender sender) {
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
            if (command.isSupportOnly())
                return;

            sender.getJDA().upsertCommand(command.getData()).queue(success -> {
                sender.replySuccessEphemeral(Success.UPDATE, commandName).queue();
            }, error -> sender.replyErrorEphemeral(Error.UNKNOWN).queue());
        } else {
            if (command.isSupportOnly()) {
                final long guildId = sender.getGuild().getIdLong();
                if (guildId != 926623552227135528L)
                    return;

                final List<CommandPrivilege> privileges = List.of(
                        CommandPrivilege.enableUser(769456676016226314L)
                );

                sender.getGuild().upsertCommand(command.getData())
                        .flatMap(data -> data.updatePrivileges(sender.getGuild(), privileges))
                        .queue(success -> sender.replySuccessEphemeral(Success.UPDATE, commandName).queue());
                return;
            }

            sender.getGuild().upsertCommand(command.getData()).queue(success -> {
                sender.replySuccessEphemeral(Success.UPDATE, commandName).queue();
            }, error -> sender.replyErrorEphemeral(Error.UNKNOWN).queue());
        }
    }
}
