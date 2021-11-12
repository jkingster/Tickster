package io.jking.tickster.interaction.impl.slash.impl.utility;

import io.jking.tickster.interaction.context.CommandContext;
import io.jking.tickster.interaction.impl.slash.object.Category;
import io.jking.tickster.interaction.impl.slash.object.Command;
import io.jking.tickster.interaction.impl.slash.object.CommandError;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", "A command to see if I'm working.", Category.UTILITY);
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        ctx.reply("I am working, leave me alone.").setEphemeral(true).queue();
    }
}
