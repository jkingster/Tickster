package io.jking.untitled.command.impl.utility;

import io.jking.untitled.command.Category;
import io.jking.untitled.command.Command;
import io.jking.untitled.command.CommandContext;

import java.util.concurrent.TimeUnit;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", "Shows network reachability.", Category.UTILITY);
    }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("\uD83C\uDFD3 Pong!")
                .flatMap(hook -> hook.getJDA().getRestPing())
                .delay(3, TimeUnit.SECONDS)
                .flatMap(ping -> ctx.getHook().editOriginalFormat("**Rest Ping:** `%sms`", ping))
                .delay(10, TimeUnit.SECONDS)
                .flatMap(__ -> ctx.getHook().deleteOriginal())
                .queue();
    }
}
