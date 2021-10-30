package io.jking.untitled.command.impl.setting;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.error.CommandError;
import io.jking.untitled.database.Hikari;
import io.jking.untitled.jooq.tables.GuildData;
import io.jking.untitled.jooq.tables.records.GuildDataRecord;
import io.jking.untitled.utility.EmbedUtil;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jooq.Field;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.jking.untitled.jooq.tables.GuildData.GUILD_DATA;

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
            case "moderation" -> onModerator(ctx);
            case "logs", "notifications" -> onChannel(ctx, subCommand);
        }
    }

    private void onModerator(CommandContext ctx) {


    }

    private void onChannel(CommandContext ctx, String subCommand) {
        final TextChannel channel = subCommand.equalsIgnoreCase("logs") ?
                ctx.getChannelOption("channel_logs") : ctx.getChannelOption("channel_notifications");

        if (channel == null) {
            ctx.replyError(CommandError.UNKNOWN)
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final long guildId = ctx.getGuild().getIdLong();


        if (subCommand.equalsIgnoreCase("logs")) {

            ctx.getGuildCache().update(guildId, GUILD_DATA.LOGS_ID, channel.getIdLong(), success -> {

                final EmbedBuilder successEmbed = EmbedUtil.getSuccess("The logs channel has been configured to: %s", channel.getAsMention());
                final EmbedBuilder defaultEmbed = EmbedUtil.getSuccess("This channel is now the configured logs channel.");

                ctx.reply(successEmbed.build())
                        .setEphemeral(true)
                        .queue();

                channel.sendMessageEmbeds(defaultEmbed.build()).queue();
            }, error -> ctx.replyError(CommandError.UNKNOWN, error).queue());


        } else if (subCommand.equalsIgnoreCase("notifications")) {
            ctx.getGuildCache().update(guildId, GUILD_DATA.NOTIFICATIONS_ID, channel.getIdLong(), null, null);
        }
    }

}
