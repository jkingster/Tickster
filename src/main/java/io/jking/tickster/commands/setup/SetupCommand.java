package io.jking.tickster.commands.setup;

import io.jking.tickster.objects.command.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SetupCommand extends Command {

    public SetupCommand() {
        super("setup", "Configure settings for this server.", Category.SETUP, Permission.ADMINISTRATOR);
        addSubcommands(
                new SubcommandData("ticket_manager", "The configured ticket manager role.")
                        .addOption(OptionType.ROLE, "ticket_role", "The mentioned role."),

                new SubcommandData("ticket_channel", "The configured ticket channel.")
                        .addOption(OptionType.CHANNEL, "ticket_channel", "The mentioned channel."),

                new SubcommandData("log_channel", "The configured logs channel.")
                        .addOption(OptionType.CHANNEL, "log_channel", "The mentioned channel.")
        );
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        final String subCommand = ctx.getSubCommand();

        switch (subCommand.toLowerCase()) {
            case "ticket_manager" -> setupTicketManager(ctx, err);
            case "ticket_channel" -> setupTicketChannel(ctx, err);
            case "log_channel" -> setupLogChannel(ctx, err);
        }
    }

    private void setupTicketManager(CommandContext ctx, CommandError err) {
        final Role role = ctx.getOptionRole("ticket_role");
        if (role == null || !ctx.canInteract(role)) {
            err.reply(ErrorType.CANT_INTERACT, ctx.getSelf().getAsTag(), "the mentioned role.");
            return;
        }


    }

    private void setupTicketChannel(CommandContext ctx, CommandError err) {
    }

    private void setupLogChannel(CommandContext ctx, CommandError err) {
    }

}
