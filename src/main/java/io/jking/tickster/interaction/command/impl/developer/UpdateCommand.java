package io.jking.tickster.interaction.command.impl.developer;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import net.dv8tion.jda.api.Permission;

public class UpdateCommand extends AbstractCommand {

    private final CommandRegistry registry;

    public UpdateCommand(CommandRegistry registry) {
        super(
                "update",
                "Updates a specific command. Globally or guild.",
                Permission.ADMINISTRATOR,
                CommandCategory.DISABLED,
                CommandFlag.of(CommandFlag.DISABLED | CommandFlag.DEVELOPER)
        );

        this.registry = registry;
        setDefaultEnabled(false);

    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        sender.reply(Error.DISABLED).queue();
    }

}
