package io.jking.tickster.interaction.command.impl.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.command.CommandFlag;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class StatsCommand extends AbstractCommand {

    public StatsCommand() {

        super(
                "stats",
                "Shows stats about me.",
                CommandCategory.UTILITY,
                CommandFlag.ofEphemeral()
        );
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final EmbedBuilder embed = EmbedUtil.getDefault()
                .setTimestamp(Instant.now());

        embed.addField("Executions", String.format(
                "**Commands:** `%s`\n**Buttons:** `%s`\n**Selection Menus:** `%s`\n**Current Shard:** `%s`",
                sender.getCommandRegistry().getUses(),
                sender.getButtonRegistry().getUses(),
                sender.getSelectRegistry().getUses(),
                sender.getJDA().getShardInfo().getShardId()
        ), true);

        final int totalThreads = ManagementFactory.getThreadMXBean().getThreadCount();
        final long totalGuilds = sender.getShardManager().getGuildCache().size();
        final long totalShards = sender.getShardManager().getShardsTotal();
        final Runtime runtime = Runtime.getRuntime();
        final long memory = (runtime.totalMemory() - runtime.freeMemory()) / 1000000;

        embed.addField("Nerd Info", String.format(
                "**Total Threads:** `%s`\n**Servers:** `%s`\n**Total Shards:** `%s`\n**Memory:** `%s / %s MB`",
                totalThreads,
                totalGuilds,
                totalShards,
                memory,
                runtime.totalMemory() / 100000
        ), true);

        final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        final long upTime = bean.getUptime();

        final long days = TimeUnit.MILLISECONDS.toDays(upTime);
        final long hours = TimeUnit.MILLISECONDS.toHours(upTime);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(upTime);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(upTime);

        embed.addField("Uptime", String.format(
                "`%s` days\n`%s` hours\n`%s` minutes\n`%s` seconds",
                days, hours, minutes, seconds
        ), true);

        embed.setDescription(String.format("**JDA Version:** `%s`\n**JVM Version:** `%s`",
                JDAInfo.VERSION,
                System.getProperty("java.version")
                )
        );

        sender.reply(embed).queue();
    }
}
