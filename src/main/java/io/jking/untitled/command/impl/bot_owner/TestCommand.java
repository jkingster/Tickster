package io.jking.untitled.command.impl.bot_owner;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;


public class TestCommand extends Command {
    public TestCommand() {
        super("test", "A testing command for the bot owners.", Category.UTILITY);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("I am working.. I think?")
                .setEphemeral(true)
                .queue();
    }
}
