package io.jking.tickster.interaction.command.impl.bot_owner;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.impl.SlashSender;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

public class StatsCommand extends AbstractCommand {

    public StatsCommand() {
        super("stats", "Shows stats about me.", CommandCategory.UTILITY);
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        final long upTime = bean.getUptime();

        final long days = TimeUnit.MILLISECONDS.toDays(upTime);
        final long hours = TimeUnit.MILLISECONDS.toHours(upTime);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(upTime);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(upTime);

        sender.replyEphemeral(String.format("**Uptime:** `%s days (%02d:%02d:%02d)`",
                days, hours, minutes, seconds)).queue();
    }
}
