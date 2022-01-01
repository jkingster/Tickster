package io.jking.tickster.interaction.command.impl.bot_owner;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateCommand extends AbstractCommand {

    // TODO: This entire class needs a rework honestly.

    private final CommandRegistry registry;

    public UpdateCommand(CommandRegistry registry) {
        super("update", "Update a specific command. Globally or guild.", CommandCategory.BOT_OWNER);

        addSubCommands(
                new SubcommandData("delete", "Delete all global commands."),
                new SubcommandData("all", "Update all commands globally."),
                new SubcommandData("command", "Update commands").addOptions(
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
            case "all" -> onAllCommand(sender);
            case "command" -> onUpdateCommand(sender);
        }
    }

    private void onDeleteAllCommand(SlashSender sender) {
        sender.getJDA().updateCommands().queue(
                success -> sender.replySuccessEphemeral(Success.ACTION).queue(),
                error -> sender.replyErrorEphemeral(Error.UNKNOWN).queue()
        );
    }


    private void onAllCommand(SlashSender sender) {
        final List<SlashCommandData> commandList = registry.getCommands()
                .stream()
                .map(AbstractCommand::getData)
                .collect(Collectors.toUnmodifiableList());

        final CommandListUpdateAction updateAction = sender.getJDA().updateCommands();

        updateAction.addCommands(commandList).queue(
                success -> sender.replySuccessEphemeral(Success.ACTION).queue(),
                error -> sender.replyError(Error.UNKNOWN).queue()
        );
    }

    private void onUpdateCommand(SlashSender sender) {
        sender.deferReply(true).queue(deferred -> {
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

            if (command.isSupportOnly()) {
                final long guildId = sender.getGuild().getIdLong();
                if (guildId != 926623552227135528L && guildId != 819689270893346846L)
                    return;

                final List<CommandPrivilege> privileges = List.of(CommandPrivilege.enableUser(769456676016226314L));
                sender.getGuild().upsertCommand(command.getData())
                        .flatMap(cmd -> cmd.updatePrivileges(sender.getGuild(), privileges))
                        .queue(
                                success -> sender.replySuccessEphemeral(Success.UPDATE, command.getName()),
                                error -> sender.replyErrorEphemeral(Error.UNKNOWN)
                        );
                return;
            }

            final boolean isGlobal = sender.getBooleanOption("global");
            if (isGlobal) {
                sender.getJDA().upsertCommand(command.getData()).queue(
                        success -> sender.replySuccessEphemeral(Success.UPDATE, command.getName()).queue(),
                        error -> sender.replyErrorEphemeral(Error.UNKNOWN).queue()
                );
                return;
            }

            sender.getGuild().upsertCommand(command.getData()).queue(
                    success -> sender.replySuccessEphemeral(Success.UPDATE, command.getName()),
                    error -> sender.replyErrorEphemeral(Error.UNKNOWN).queue()
            );
        });
    }
}
