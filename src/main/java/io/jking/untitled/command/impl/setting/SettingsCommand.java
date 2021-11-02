package io.jking.untitled.command.impl.setting;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", "Changes settings for certain modules.", Category.SETTINGS, Permission.ADMINISTRATOR);

        final OptionData log = new OptionData(OptionType.CHANNEL, "channel_logs", "Mentioned Log Channel.", true);
        final OptionData mod = new OptionData(OptionType.ROLE, "role_moderator", "Mentioned Moderator Role.", true);
        final OptionData notification = new OptionData(OptionType.CHANNEL, "channel_notifications", "Mentioned Notification Channel.", true);

        addSubcommands(
                new SubcommandData("logs", "Sets the log channel.").addOptions(log),
                new SubcommandData("moderation", "Sets the moderator role.").addOptions(mod),
                new SubcommandData("notifications", "Sets the notifications channel.").addOptions(notification)
        );
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final String subCommand = ctx.getSubcommand();
        System.out.println(subCommand);

        switch (subCommand.toLowerCase()) {
            case "logs" -> onLogs(ctx);
            case "moderation" -> onModeration(ctx);
            case "notifications" -> onNotifications(ctx);
        }
    }

    private void onNotifications(CommandContext ctx) {

    }

    private void onModeration(CommandContext ctx) {
    }

    private void onLogs(CommandContext ctx) {
    }


}
