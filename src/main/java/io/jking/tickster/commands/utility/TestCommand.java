package io.jking.tickster.commands.utility;

import io.jking.tickster.interaction.command.AbstractCommand;
import io.jking.tickster.interaction.command.CommandCategory;
import io.jking.tickster.interaction.core.impl.SlashContext;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;

public class TestCommand extends AbstractCommand {

    public TestCommand() {
        super("test", "Checks to see if I'm working... of course...", CommandCategory.UTILITY);
    }

    @Override
    public void onSlashCommand(SlashContext context) {
        context.reply("I'm working... maybe?")
                .addActionRow(Button.secondary("button:garbage", Emoji.fromUnicode("\uD83D\uDDD1")))
                .queue();
    }
}

