package io.jking.untitled.command.impl.info;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.core.Untitled;
import io.jking.untitled.core.UntitledInfo;
import io.jking.untitled.utility.EmbedUtil;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.OffsetDateTime;

public class InfoCommand extends Command {


    public InfoCommand() {
        super("info", "Shows information about me.", Category.INFO);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final String version = UntitledInfo.VERSION;
        final OffsetDateTime start = UntitledInfo.START_TIMESTAMP;

        final StringBuilder stringBuilder = new StringBuilder()
                .append("```ml\n")
                .append(StringUtils.rightPad("Version ", 22, " ")).append("'").append(version).append("'\n")
                .append(StringUtils.rightPad("Language ", 22, " ")).append("'").append("Java").append("'\n")
                .append(StringUtils.rightPad("Author ", 22, " ")).append("'").append("Jacob.#0000").append("'\n")
                .append(StringUtils.rightPad("Uptime ", 22, " ")).append("'").append(MiscUtil.getAge(start)).append(" days'\n")
                .append(StringUtils.rightPad("JVM Version ", 22, " ")).append("'").append(System.getProperty("java.version")).append("`\n")
                .append(StringUtils.rightPad("JDA Version ", 22, " ")).append("'").append(JDAInfo.VERSION).append("`\n")
                .append(StringUtils.rightPad("Total Guilds ", 22, " ")).append("'").append(ctx.getJDA().getGuildCache().size()).append("'\n")
                .append(StringUtils.rightPad("Total Threads", 22, " ")).append("'").append(ManagementFactory.getThreadMXBean().getThreadCount()).append("'\n")
                .append("```");


        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setDescription(stringBuilder)
                .setAuthor(Untitled.class.getSimpleName(), null, ctx.getSelfUser().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now())
                .setFooter("ID: " + ctx.getSelf().getId());

        ctx.reply(embedBuilder.build())
                .setEphemeral(true)
                .addActionRow(Button.link(UntitledInfo.GITHUB_REPO, "GitHub Repo"), Button.link("https://github.com/DV8FromTheWorld/JDA", "JDA"))
                .queue();
    }
}
