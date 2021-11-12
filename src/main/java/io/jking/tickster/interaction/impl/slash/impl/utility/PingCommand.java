package io.jking.tickster.interaction.impl.slash.impl.utility;

import io.jking.tickster.interaction.context.CommandContext;
import io.jking.tickster.interaction.impl.slash.object.Category;
import io.jking.tickster.interaction.impl.slash.object.Command;
import io.jking.tickster.interaction.impl.slash.object.CommandError;

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
