package io.jking.tickster.interaction.command.impl.developer;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.interaction.core.responses.Error;
import io.jking.tickster.interaction.core.responses.Success;
import io.jking.tickster.utility.MiscUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static io.jking.tickster.jooq.tables.BlacklistData.BLACKLIST_DATA;

public class BlacklistCommand extends AbstractCommand {

    public BlacklistCommand() {
        super(
                "blacklist",
                "Blacklists a guild from using Tickster.",
                Permission.ADMINISTRATOR,
                CommandCategory.DEVELOPER,
                CommandFlag.ofDeveloper()
        );

        addOptions(
                new OptionData(OptionType.STRING, "guild-id", "targeted guild id", true),
                new OptionData(OptionType.STRING, "reason", "reason for blacklist", true)
        );

        setDefaultEnabled(false);

    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String guildId = sender.getStringOption("guild-id");
        final String reason = sender.getStringOption("reason");
        if (guildId == null || reason == null) {
            sender.reply(Error.ARGUMENTS, this.getName()).queue();
            return;
        }

        if (!MiscUtil.isSnowflake(guildId)) {
            sender.reply(Error.SNOWFLAKE, guildId).queue();
            return;
        }

        final Guild guild = sender.getShardManager().getGuildById(guildId);
        if (guild == null) {
            sender.reply(Error.CUSTOM, "Could not find that guild!").queue();
            return;
        }

        sender.getDatabase().getContext().insertInto(BLACKLIST_DATA)
                .values(guildId, reason)
                .onConflictDoNothing()
                .executeAsync()
                .thenAcceptAsync(action -> sender.reply(Success.ACTION).queue())
                .exceptionallyAsync(null);
    }
}
