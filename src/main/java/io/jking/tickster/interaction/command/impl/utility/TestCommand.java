package io.jking.tickster.interaction.command.impl.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.impl.SlashSender;
import io.jking.tickster.utility.EmbedUtil;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.TimeFormat;

public class TestCommand extends AbstractCommand {

    public TestCommand() {
        super("test", "Checks to see if I'm working... of course...", CommandCategory.UTILITY);
    }

    @Override
    public void onSlashCommand(SlashSender sender) {
        final String buttonId = String.format("button:garbage:id:%s", sender.getMember().getIdLong());
        sender.reply(EmbedUtil.getDefault().setDescription("I'm working.. I think? " + TimeFormat.DATE_SHORT.now()))
                .addActionRow(Button.secondary(buttonId, Emoji.fromUnicode("\uD83D\uDDD1")))
                .queue();
    }
}

