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
        final TextChannel channel = ctx.getChannel();
        final MessageData data = ctx.getMessageEvent().getMostRecentDeleted(channel.getIdLong());

        if (data == null) {
            ctx.reply("There are no deleted messages to snipe!")
                    .delay(15, TimeUnit.SECONDS)
                    .flatMap(InteractionHook::deleteOriginal)
                    .queue();
            return;
        }

        ctx.reply("**Message Sniper:** " + data)
                .delay(15, TimeUnit.SECONDS)
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }
}
