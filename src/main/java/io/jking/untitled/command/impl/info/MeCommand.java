package io.jking.untitled.command.impl.info;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.utility.EmbedUtil;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class MeCommand extends Command {

    public MeCommand() {
        super("me", "Shows information about you.", Category.INFO);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final Member member = ctx.getMember();

        final EmbedBuilder embedBuilder = EmbedUtil.getDefault()
                .setFooter("ID: " + member.getId())
                .setThumbnail(member.getUser().getEffectiveAvatarUrl());

        final String nickname = member.getNickname() == null ? "No nickname." : member.getNickname();

        final StringBuilder stringBuilder = new StringBuilder()
                .append("**Username:** `").append(member.getUser().getAsTag()).append("`\n")
                .append("**Nickname:** `").append(nickname).append("`\n")
                .append("**Account Creation:** `").append(MiscUtil.getFormattedDate(member.getTimeCreated())).append("`\n")
                .append("**Date/Time Joined:** `").append(MiscUtil.getFormattedDate(member.getTimeJoined())).append("`\n")
                .append("**Account Age (Days):** `").append(MiscUtil.getAge(member.getTimeCreated())).append("`\n")
                .append("**Total Roles:** `").append(member.getRoles().size()).append("`\n");

        embedBuilder.setDescription(stringBuilder);

        ctx.reply(embedBuilder.build())
                .delay(20, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }
}
