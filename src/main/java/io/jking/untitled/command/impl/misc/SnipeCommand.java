package io.jking.untitled.command.impl.misc;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;
import io.jking.untitled.data.MessageData;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class SnipeCommand extends Command {

    public SnipeCommand() {
        super("snipe", "Snipes the last deleted message.", Category.MISC);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        // TODO: REVAMP.
    }
}
