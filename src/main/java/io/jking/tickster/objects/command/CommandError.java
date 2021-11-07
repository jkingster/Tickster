package io.jking.tickster.objects.command;

import io.jking.tickster.utility.EmbedFactory;

public class CommandError {

    private final CommandContext ctx;

    public CommandError(CommandContext ctx) {
        this.ctx = ctx;
    }

    public void reply(ErrorType errorType, Object... objects) {
        ctx.reply(EmbedFactory.getError(errorType, objects))
                .setEphemeral(true)
                .queue();
    }

    public void replyUnknown() {
        reply(ErrorType.UNKNOWN);
    }

}
