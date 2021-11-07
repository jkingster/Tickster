package io.jking.tickster.commands.utility;

import io.jking.tickster.objects.command.*;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", "A command to see if I'm working.", Category.UTILITY);
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        ctx.reply("I am working, leave me alone.").setEphemeral(true).queue();
    }
}
