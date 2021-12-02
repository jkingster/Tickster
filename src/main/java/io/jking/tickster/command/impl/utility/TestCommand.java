package io.jking.tickster.command.impl.utility;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", "A command to see if I'm working.", Category.UTILITY);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("I am working, leave me alone.").setEphemeral(true).queue();
    }
}
