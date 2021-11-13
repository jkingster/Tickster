package io.jking.tickster.command.impl.utility;

import io.jking.tickster.command.Category;
import io.jking.tickster.command.Command;
import io.jking.tickster.command.CommandContext;
import io.jking.tickster.command.CommandError;

public class PingCommand extends Command {
    public PingCommand() {
        super("ping", "Shows my network reachability.", Category.UTILITY);
    }

    @Override
    public void onCommand(CommandContext ctx, CommandError err) {
        final long init = System.currentTimeMillis();
        ctx.getJda().getRestPing().map(String::valueOf)
                .flatMap(value -> ctx.replyFormatted("**Rest Ping:** %s\n**Gateway Ping:** %s\n**General Ping:** %s",
                        value,
                        ctx.getJda().getGatewayPing(),
                        System.currentTimeMillis() - init)
                        .setEphemeral(true)
                )
                .queue();
    }
}
