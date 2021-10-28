package io.jking.untitled.command.impl.bot_owner;


import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.CommandRegistry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.print.DocFlavor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateCommand extends Command {

    private final CommandRegistry commandRegistry;

    public UpdateCommand(CommandRegistry commandRegistry) {
        super("update", "Updates commands.", Category.BOT_OWNER);
        setDefaultEnabled(false);
        addOption(OptionType.BOOLEAN, "global", "Update globally.");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final boolean option = ctx.getBooleanOption("global");

        if (option) {
            updateGlobally(ctx);
        } else {
            updateByGuild(ctx.getGuild());
        }






    }

    private void updateGlobally(CommandContext ctx) {
        commandRegistry.getCommands().forEach(command -> ctx.getJDA().upsertCommand(command).queue());
    }

    private void updateByGuild(Guild guild) {
        commandRegistry.getCommands().forEach(command -> guild.upsertCommand(command).queue());
    }
}
