package io.jking.untitled.command.impl.setting;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.error.CommandError;
import io.jking.untitled.database.Hikari;
import io.jking.untitled.jooq.tables.GuildData;
import io.jking.untitled.jooq.tables.records.GuildDataRecord;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jooq.Field;

import java.util.List;

import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", "Changes settings for certain modules.", Category.SETTINGS, Permission.ADMINISTRATOR);

        final OptionData log = new OptionData(OptionType.CHANNEL, "log_channel", "Mentioned Log Channel.");
        final OptionData mod = new OptionData(OptionType.ROLE, "mod_role", "Mentioned Moderator Role.");
        final OptionData notification = new OptionData(OptionType.CHANNEL, "noti_channel", "Mentioned Notification Channel.");

        addSubcommands(
                new SubcommandData("logs", "Sets the log channel.").addOptions(log),
                new SubcommandData("moderator", "Sets the moderator role.").addOptions(mod),
                new SubcommandData("notifications", "Sets the notifications channel.").addOptions(notification)
        );
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final String subCommand = ctx.getSubcommand();
        switch (subCommand.toLowerCase()) {
            case "moderator" -> onModerator(ctx);
            case "logs" -> onLogs(ctx);
            case "notifications" -> onNotifications(ctx);
        }
    }

    private void onModerator(CommandContext ctx) {


    }

    private void onNotifications(CommandContext ctx) {
    }

    private void onLogs(CommandContext ctx) {
        final TextChannel channel = ctx.getChannelOption("log_channel");

        if (channel == null) {
            ctx.replyError(CommandError.ARGUMENTS);
            return;
        }

        final long guildId = ctx.getGuild().getIdLong();

        ctx.getGuildCache().update(guildId, GUILD_DATA.LOGS_ID, channel.getIdLong(), null, null);
        // TODO SEND SUCCESS/ERROR.
    }
}
