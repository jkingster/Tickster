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
    public void onSlashCommand(SlashSender sender) {
        final User user = sender.getUserOption("user");
        if (user == null) {
            sender.replyEphemeral(EmbedUtil.getMemberInfo(sender.getMember())).queue();
            return;
        }

        sender.retrieveMember(user.getIdLong()).queue(member -> {
            sender.replyEphemeral(EmbedUtil.getMemberInfo(member)).queue();
        }, error -> {
            sender.replyEphemeral(EmbedUtil.getUserInfo(user)).queue();
        });
    }
}
