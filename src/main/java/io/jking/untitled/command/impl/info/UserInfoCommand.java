package io.jking.untitled.command.impl.info;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.error.CommandError;
import io.jking.untitled.utility.EmbedUtil;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        super("userinfo", "Shows information about a user.", Category.INFO);
        addOption(OptionType.USER, "target", "The user you want to see info about.", true);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final Member target = ctx.getMemberOption("target");
        if (target == null) {
            ctx.replyError(CommandError.UNKNOWN)
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (target.getIdLong() == ctx.getAuthor().getIdLong()) {
            ctx.reply("Use /me......")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setFooter("ID: " + target.getId())
                .setThumbnail(target.getUser().getEffectiveAvatarUrl());

        final String nickname = target.getNickname() == null ? "No nickname." : target.getNickname();

        final StringBuilder stringBuilder = new StringBuilder()
                .append("**Username:** `").append(target.getUser().getAsTag()).append("`\n")
                .append("**Nickname:** `").append(nickname).append("`\n")
                .append("**Account Creation:** `").append(MiscUtil.getFormattedDate(target.getTimeCreated())).append("`\n")
                .append("**Date/Time Joined:** `").append(MiscUtil.getFormattedDate(target.getTimeJoined())).append("`\n")
                .append("**Account Age (Days):** `").append(MiscUtil.getAge(target.getTimeCreated())).append("`\n")
                .append("**Total Roles:** `").append(target.getRoles().size()).append("`\n");

        embedBuilder.setDescription(stringBuilder);

        ctx.reply(embedBuilder.build())
                .setEphemeral(true)
                .queue();
    }
}
