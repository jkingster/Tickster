package io.jking.untitled.command.impl.moderation;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.command.error.CommandError;
import io.jking.untitled.utility.EmbedUtil;
import io.jking.untitled.utility.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class KickCommand extends Command {

    /**
     * I don't like the idea that the possible options are "optional" (the user mentioned/target id),
     * but it allows the issuing member to use either rather than making two separate commands. Maybe eventually
     * we'll be able to choose between the two.
     */
    public KickCommand() {
        super("kick", "Kicks a member from the server. Provide an ID or mention.", Category.MODERATION, Permission.KICK_MEMBERS);
        addOptions(
                new OptionData(OptionType.USER, "target", "Mention the user you want kicked.", false),
                new OptionData(OptionType.INTEGER, "target_id", "Provide the users ID that you want kicked.", false),
                new OptionData(OptionType.STRING, "reason", "The reason this user was kicked.", false)
        );
    }

    @Override
    public void onCommand(CommandContext ctx) {
        final Member target = ctx.getMemberOption("target");
        final long targetId = ctx.getLongOption("target_id");

        if (target == null && targetId == 0L) {
            ctx.replyError(CommandError.ARGUMENTS)
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final String reason = ctx.getStringOption("reason");
        final String finalReason = reason == null ? "No reason provided." : reason;

        if (target != null) {
            onPunishment(ctx, target, ctx.getMember(), finalReason);
            return;
        }

        if (!MiscUtil.isSnowflake(targetId)) {
            ctx.replyError(CommandError.INVALID, targetId)
                    .setEphemeral(true)
                    .queue();
            return;
        }

        ctx.getGuild().retrieveMemberById(targetId).queue(member -> onPunishment(ctx, member, ctx.getMember(), finalReason),
                error -> ctx.replyError(CommandError.UNKNOWN, "Could not retrieve member with ID: " + targetId)
        );

    }

    private void onPunishment(CommandContext ctx, Member target, Member issuing, String reason) {
        final Member self = ctx.getSelf();
        if (!self.canInteract(target)) {
            ctx.replyError(CommandError.INTERACTION, self.getUser().getAsTag(), target.getUser().getAsTag())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (!issuing.canInteract(target)) {
            ctx.replyError(CommandError.INTERACTION, issuing.getUser().getAsTag(), target.getUser().getAsTag())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        final EmbedBuilder embed = EmbedUtil.getSuccess("`%s` was kicked by `%s`\n**Reason:** `%s`",
                target.getUser().getAsTag(), issuing.getUser().getAsTag(), reason)
                .setFooter("ID: " + target.getIdLong());

        target.kick(reason)
                .flatMap(success -> ctx.replySuccess(embed))
                .onErrorFlatMap(throwable -> ctx.replyError(CommandError.UNKNOWN, throwable))
                .queue();
    }
}
