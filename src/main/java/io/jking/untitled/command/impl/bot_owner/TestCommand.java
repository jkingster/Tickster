package io.jking.untitled.command.impl.bot_owner;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "A testing command for the bot owners.", Category.BOT_OWNER);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("I am working... hopefully?")
                .setEphemeral(true)
                .delay(10, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }
}
