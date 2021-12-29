package io.jking.tickster.interaction.command.impl.info;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class InfoCommand extends AbstractCommand {

    public InfoCommand() {
        super("info", "Grab info about a specific user/yourself.", CommandCategory.INFO);
        addOption(OptionType.USER, "user", "The user you want to see information about.", false);
    }

    @Override
    public void onSlashCommand(SlashSender context) {
        final User user = context.getUserOption("user");
        if (user == null) {
            context.replyEphemeral(EmbedUtil.getMemberInfo(context.getMember())).queue();
            return;
        }

        context.retrieveMember(user.getIdLong()).queue(member -> {
            context.replyEphemeral(EmbedUtil.getMemberInfo(member)).queue();
        }, error -> {
            context.replyEphemeral(EmbedUtil.getUserInfo(user)).queue();
        });
    }
}
