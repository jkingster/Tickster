package io.jking.untitled.command.impl.setting;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", "Changes settings for certain modules.", Category.SETTINGS, Permission.ADMINISTRATOR);

        final List<OptionData> logOptions = List.of(
          new OptionData(OptionType.CHANNEL, "mentioned_channel", "Mentioned Log Channel."),
          new OptionData(OptionType.INTEGER, "channel_id", "Log Channel ID.")
        );

        addSubcommands(
          new SubcommandData("logs", "Log Channel ID.").addOptions(logOptions)
        );
    }

    @Override
    public void onCommand(CommandContext ctx) {

    }
}
